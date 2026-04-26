/*
 * Traincraft
 * Copyright (c) 2011-2024.
 */

package traincraft.items;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.RailBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.RailShape;
import net.minecraft.world.phys.Vec3;
import traincraft.blocks.TCBlocks;
import traincraft.blocks.rail.TileTCCurvedRail;
import traincraft.curves.CurveData;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * Two-click track-laying tool. The first click on a rail records the start position and the
 * direction along which the curve should leave that block (derived from the rail shape). The
 * second click on a rail computes a smooth cubic Bezier from the start to the new end and
 * places curved-rail segments at each integer block position along the curve.
 * <p>
 * The selection is persisted on the item via the CUSTOM_DATA component so the player can step
 * away between clicks. Sneak + right-click in air clears the current selection.
 */
public class ItemTrackLayer extends Item {

    private static final String START_X = "start_x";
    private static final String START_Y = "start_y";
    private static final String START_Z = "start_z";
    private static final String START_DIR = "start_dir";

    public ItemTrackLayer() {
        super(new Properties().stacksTo(1).durability(256));
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        Player player = context.getPlayer();
        if (player == null) return InteractionResult.PASS;

        BlockPos pos = context.getClickedPos();
        BlockState state = level.getBlockState(pos);
        if (!(state.getBlock() instanceof RailBlock)) {
            // Allow targeting the block above as well in case the player is clicking the top of a slab/full block beneath the rail.
            BlockPos above = pos.above();
            BlockState aboveState = level.getBlockState(above);
            if (aboveState.getBlock() instanceof RailBlock) {
                pos = above;
                state = aboveState;
            } else {
                return InteractionResult.PASS;
            }
        }

        ItemStack stack = context.getItemInHand();

        // Sneak + right-click on a curved rail removes every block belonging to that curve.
        if (player.isShiftKeyDown() && state.getBlock() instanceof traincraft.blocks.rail.BlockTCCurvedRail) {
            if (!level.isClientSide) {
                int removed = removeCurve(level, pos);
                player.displayClientMessage(Component.literal("Removed " + removed + " curve segments."), true);
            }
            return InteractionResult.sidedSuccess(level.isClientSide);
        }

        Direction startDir = railDirection(state);

        if (!hasStart(stack)) {
            // First click: store the anchor.
            setStart(stack, pos, startDir);
            if (!level.isClientSide) {
                player.displayClientMessage(Component.literal(
                    "Track start at " + pos.toShortString() + " (heading " + startDir + "). Right-click another rail to lay the curve."), true);
            }
            return InteractionResult.sidedSuccess(level.isClientSide);
        }

        // Second click: build the curve.
        BlockPos start = readStart(stack);
        Direction startDirection = readStartDir(stack);
        clearStart(stack);

        if (start.equals(pos)) {
            if (!level.isClientSide) {
                player.displayClientMessage(Component.literal("Endpoints can't be the same block."), true);
            }
            return InteractionResult.FAIL;
        }

        Direction endDirection = railDirection(state);

        if (!level.isClientSide) {
            int placed = layCurve(level, player, start, startDirection, pos, endDirection);
            player.displayClientMessage(Component.literal("Laid " + placed + " curve segments."), true);
        }
        return InteractionResult.sidedSuccess(level.isClientSide);
    }

    @Override
    public net.minecraft.world.InteractionResultHolder<ItemStack> use(Level level, Player player, net.minecraft.world.InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (player.isShiftKeyDown() && hasStart(stack)) {
            clearStart(stack);
            if (!level.isClientSide) {
                player.displayClientMessage(Component.literal("Track-layer selection cleared."), true);
            }
            return net.minecraft.world.InteractionResultHolder.success(stack);
        }
        return net.minecraft.world.InteractionResultHolder.pass(stack);
    }

    /** Build a Bezier from start->end along the natural rail directions, place segment blocks. */
    private int layCurve(Level level, Player player, BlockPos start, Direction startDir, BlockPos end, Direction endDir) {
        Vec3 p0 = Vec3.atCenterOf(start);
        Vec3 p3 = Vec3.atCenterOf(end);

        // Control points are pushed along the rail tangent at each end. The handle distance is a
        // third of the chord length so the curve is gentle and doesn't overshoot wildly.
        double chord = p0.distanceTo(p3);
        double handle = Math.max(2.0, chord / 3.0);

        Vec3 d0 = direction(startDir);
        Vec3 d1 = direction(endDir.getOpposite());
        // Handles stay horizontal (the rail tangent has no vertical component) but their y is
        // anchored to each endpoint so the curve interpolates smoothly between rails at different
        // heights — the cubic Bezier blend functions naturally produce a vertical S that respects
        // both endpoints' altitudes without bowing above or below them.
        Vec3 p1 = p0.add(d0.scale(handle));
        Vec3 p2 = p3.add(d1.scale(handle));

        CurveData curve = new CurveData(p0, p1, p2, p3);
        UUID curveId = UUID.randomUUID();

        // Sample the curve densely; group samples by integer block position so a single block
        // covers a [tStart, tEnd] interval. We choose sample count proportional to arc length.
        double length = curve.arcLength(64);
        int samples = Math.max(64, (int) (length * 8));

        Set<BlockPos> placed = new HashSet<>();
        java.util.Map<BlockPos, double[]> ranges = new java.util.LinkedHashMap<>();
        for (int i = 0; i <= samples; i++) {
            double t = (double) i / samples;
            Vec3 v = curve.evaluate(t);
            BlockPos bp = BlockPos.containing(v.x, v.y, v.z);
            ranges.compute(bp, (k, existing) -> {
                if (existing == null) return new double[] {t, t};
                existing[1] = t;
                return existing;
            });
        }

        // Bail if the path crosses solid blocks the player can't replace, except for vanilla rails
        // (which we'll overwrite) and air.
        int placedCount = 0;
        for (var entry : ranges.entrySet()) {
            BlockPos bp = entry.getKey();
            BlockState existing = level.getBlockState(bp);
            if (!existing.canBeReplaced() && !(existing.getBlock() instanceof RailBlock)) {
                if (!level.isClientSide) {
                    player.displayClientMessage(Component.literal(
                        "Path obstructed at " + bp.toShortString() + "; aborting (" + placedCount + " segments laid)."), true);
                }
                return placedCount;
            }
            BlockState rail = TCBlocks.CURVED_RAIL.get().defaultBlockState();
            level.setBlock(bp, rail, 3);
            if (level.getBlockEntity(bp) instanceof TileTCCurvedRail tile) {
                double[] tr = entry.getValue();
                tile.setCurve(curve, tr[0], tr[1], curveId);
            }
            placed.add(bp);
            placedCount++;
        }
        return placedCount;
    }

    /**
     * Walk the AABB of the curve attached to the clicked tile and remove every curved-rail block
     * that shares its curveId. Returns the number of blocks removed.
     */
    private int removeCurve(Level level, BlockPos clicked) {
        traincraft.blocks.rail.TileTCCurvedRail tile = traincraft.blocks.rail.BlockTCCurvedRail.getTile(level, clicked);
        if (tile == null || tile.getCurveId() == null || tile.getCurve() == null) return 0;
        UUID curveId = tile.getCurveId();
        CurveData curve = tile.getCurve();

        // Conservative bounding box: take the AABB of the four control points and pad by 2 blocks.
        Vec3 p0 = curve.p0(), p1 = curve.p1(), p2 = curve.p2(), p3 = curve.p3();
        int minX = (int) Math.floor(Math.min(Math.min(p0.x, p1.x), Math.min(p2.x, p3.x))) - 2;
        int minY = (int) Math.floor(Math.min(Math.min(p0.y, p1.y), Math.min(p2.y, p3.y))) - 2;
        int minZ = (int) Math.floor(Math.min(Math.min(p0.z, p1.z), Math.min(p2.z, p3.z))) - 2;
        int maxX = (int) Math.ceil(Math.max(Math.max(p0.x, p1.x), Math.max(p2.x, p3.x))) + 2;
        int maxY = (int) Math.ceil(Math.max(Math.max(p0.y, p1.y), Math.max(p2.y, p3.y))) + 2;
        int maxZ = (int) Math.ceil(Math.max(Math.max(p0.z, p1.z), Math.max(p2.z, p3.z))) + 2;

        int removed = 0;
        BlockPos.MutableBlockPos cursor = new BlockPos.MutableBlockPos();
        for (int x = minX; x <= maxX; x++) {
            for (int y = minY; y <= maxY; y++) {
                for (int z = minZ; z <= maxZ; z++) {
                    cursor.set(x, y, z);
                    BlockState s = level.getBlockState(cursor);
                    if (!(s.getBlock() instanceof traincraft.blocks.rail.BlockTCCurvedRail)) continue;
                    traincraft.blocks.rail.TileTCCurvedRail t =
                        traincraft.blocks.rail.BlockTCCurvedRail.getTile(level, cursor);
                    if (t == null || !curveId.equals(t.getCurveId())) continue;
                    level.setBlock(cursor, net.minecraft.world.level.block.Blocks.AIR.defaultBlockState(), 3);
                    removed++;
                }
            }
        }
        return removed;
    }

    private static Direction railDirection(BlockState state) {
        if (state.getBlock() instanceof RailBlock) {
            RailShape shape = state.getValue(RailBlock.SHAPE);
            return switch (shape) {
                case NORTH_SOUTH, ASCENDING_NORTH -> Direction.SOUTH;
                case ASCENDING_SOUTH -> Direction.NORTH;
                case EAST_WEST, ASCENDING_EAST -> Direction.WEST;
                case ASCENDING_WEST -> Direction.EAST;
                // Diagonal corners: pick the cardinal that points away from the bend.
                case NORTH_EAST -> Direction.SOUTH;
                case NORTH_WEST -> Direction.SOUTH;
                case SOUTH_EAST -> Direction.NORTH;
                case SOUTH_WEST -> Direction.NORTH;
            };
        }
        return Direction.SOUTH;
    }

    private static Vec3 direction(Direction d) {
        return new Vec3(d.getStepX(), 0, d.getStepZ());
    }

    private static CompoundTag tag(ItemStack stack) {
        CustomData data = stack.get(DataComponents.CUSTOM_DATA);
        return data != null ? data.copyTag() : new CompoundTag();
    }

    private static void writeTag(ItemStack stack, CompoundTag tag) {
        stack.set(DataComponents.CUSTOM_DATA, CustomData.of(tag));
    }

    public static boolean hasStart(ItemStack stack) {
        return tag(stack).contains(START_X, 99);
    }

    private static void setStart(ItemStack stack, BlockPos pos, Direction dir) {
        CompoundTag tag = tag(stack);
        tag.putInt(START_X, pos.getX());
        tag.putInt(START_Y, pos.getY());
        tag.putInt(START_Z, pos.getZ());
        tag.putInt(START_DIR, dir.get3DDataValue());
        writeTag(stack, tag);
    }

    private static BlockPos readStart(ItemStack stack) {
        CompoundTag t = tag(stack);
        return new BlockPos(t.getInt(START_X), t.getInt(START_Y), t.getInt(START_Z));
    }

    private static Direction readStartDir(ItemStack stack) {
        CompoundTag t = tag(stack);
        return Direction.from3DDataValue(t.getInt(START_DIR));
    }

    private static void clearStart(ItemStack stack) {
        CompoundTag t = tag(stack);
        t.remove(START_X);
        t.remove(START_Y);
        t.remove(START_Z);
        t.remove(START_DIR);
        writeTag(stack, t);
    }
}
