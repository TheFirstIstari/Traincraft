/*
 * Traincraft
 * Copyright (c) 2011-2024.
 */

package traincraft.blocks.rail;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.RailBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

/**
 * Multi-block Bezier-curve rail. Each placed block is one segment along a single curve and
 * carries the curve's full definition plus its own [tStart, tEnd] range via {@link TileTCCurvedRail}.
 * <p>
 * For vanilla compatibility the block still exposes a {@link net.minecraft.world.level.block.state.properties.RailShape}
 * property; the actual smooth motion comes from the rolling-stock physics overriding {@code moveAlongTrack}
 * to follow the curve geometry instead of snapping to the rail-shape lattice.
 */
public class BlockTCCurvedRail extends RailBlock implements EntityBlock {

    private final double speedMultiplier;

    public BlockTCCurvedRail(BlockBehaviour.Properties properties, double speedMultiplier) {
        super(properties);
        this.speedMultiplier = speedMultiplier;
    }

    public double getSpeedMultiplier() {
        return speedMultiplier;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new TileTCCurvedRail(pos, state);
    }

    /** Convenience: fetch the curved-rail tile at the given position, or null if absent. */
    @Nullable
    public static TileTCCurvedRail getTile(BlockGetter level, BlockPos pos) {
        BlockEntity be = level.getBlockEntity(pos);
        return be instanceof TileTCCurvedRail tile ? tile : null;
    }
}
