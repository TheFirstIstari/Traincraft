/*
 * Traincraft
 * Copyright (c) 2011-2024.
 */

package traincraft.blocks.openhearthfurnace;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

public class BlockOpenHearthFurnace extends BaseEntityBlock {

    public static final BooleanProperty ACTIVE = BooleanProperty.create("active");

    public BlockOpenHearthFurnace(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(ACTIVE, false).setValue(BlockStateProperties.HORIZONTAL_FACING, Direction.NORTH));
    }

    public static final MapCodec<BlockOpenHearthFurnace> CODEC = simpleCodec(BlockOpenHearthFurnace::new);

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<net.minecraft.world.level.block.Block, BlockState> builder) {
        builder.add(ACTIVE, BlockStateProperties.HORIZONTAL_FACING);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new TileOpenHearthFurnace(pos, state);
    }

    @Override
    protected RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        if (!level.isClientSide) {
            BlockEntity blockEntity = level.getBlockEntity(pos);
            if (blockEntity instanceof TileOpenHearthFurnace tile) {
                player.openMenu(tile);
            }
        }
        return InteractionResult.SUCCESS;
    }

    @Nullable
    @Override
    @SuppressWarnings("unchecked")
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        if (type != traincraft.tile.TCTiles.OPEN_HEARTH_FURNACE.get()) {
            return null;
        }
        BlockEntityTicker<TileOpenHearthFurnace> ticker = level.isClientSide ? TileOpenHearthFurnace::clientTick : TileOpenHearthFurnace::serverTick;
        return (BlockEntityTicker<T>) ticker;
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        if (state.getValue(ACTIVE)) {
            double d0 = (double) pos.getX() + 0.5;
            double d1 = pos.getY();
            double d2 = (double) pos.getZ() + 0.5;
            if (random.nextDouble() < 0.1) {
                level.playLocalSound(d0, d1, d2, SoundEvents.FURNACE_FIRE_CRACKLE, SoundSource.BLOCKS, 1.0f, 1.0f, false);
            }

            Direction direction = state.getValue(BlockStateProperties.HORIZONTAL_FACING);
            Direction.Axis axis = direction.getAxis();
            double d3 = 0.52;
            double d4 = random.nextDouble() * 0.6 - 0.3;
            if (axis == Direction.Axis.X) {
                if (random.nextDouble() < 0.1) {
                    level.addParticle(ParticleTypes.SMOKE, d0 + direction.getStepX() * 0.52, d1 + 1.1, d2 + d4, 0.0, 0.0, 0.0);
                    level.addParticle(ParticleTypes.FLAME, d0 + direction.getStepX() * 0.52, d1 + 1.1, d2 + d4, 0.0, 0.0, 0.0);
                }
            } else if (axis == Direction.Axis.Z) {
                if (random.nextDouble() < 0.1) {
                    level.addParticle(ParticleTypes.SMOKE, d0 + d4, d1 + 1.1, d2 + direction.getStepZ() * 0.52, 0.0, 0.0, 0.0);
                    level.addParticle(ParticleTypes.FLAME, d0 + d4, d1 + 1.1, d2 + direction.getStepZ() * 0.52, 0.0, 0.0, 0.0);
                }
            }
        }
    }
}
