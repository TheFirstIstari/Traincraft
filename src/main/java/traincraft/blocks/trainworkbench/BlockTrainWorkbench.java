/*
 * Traincraft
 * Copyright (c) 2011-2024.
 */

package traincraft.blocks.trainworkbench;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

public class BlockTrainWorkbench extends BaseEntityBlock {

    public BlockTrainWorkbench(Properties properties) {
        super(properties);
    }

    public static final MapCodec<BlockTrainWorkbench> CODEC = MapCodec.unit(() -> new BlockTrainWorkbench(Properties.of()));

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new TileTrainWorkbench(pos, state);
    }

    @Override
    protected RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        if (!level.isClientSide) {
            BlockEntity blockEntity = level.getBlockEntity(pos);
            if (blockEntity instanceof TileTrainWorkbench tile) {
                player.openMenu(tile);
            }
        }
        return InteractionResult.SUCCESS;
    }
}
