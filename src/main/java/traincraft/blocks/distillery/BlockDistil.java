/*
 * Traincraft
 * Copyright (c) 2011-2024.
 *
 * This file ("BlockDistil.java") is part of the Traincraft mod for Minecraft.
 * It is distributed under LGPL-v3.0.
 */

package traincraft.blocks.distillery;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.material.Material;

import javax.annotation.Nullable;

public class BlockDistil extends HorizontalDirectionalBlock implements EntityBlock {

    public static final net.minecraft.world.level.block.state.BooleanProperty ACTIVE = 
            net.minecraft.world.level.block.state.BooleanProperty.create("active");

    public BlockDistil() {
        super(BlockBehaviour.Properties.of(Material.STONE).strength(3.5f));
        registerDefaultState(defaultBlockState().setValue(FACING, Direction.NORTH).setValue(ACTIVE, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, ACTIVE);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new TileDistillery(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(net.minecraft.world.level.Level level, BlockState state, BlockEntityType<T> type) {
        return level.isClientSide() ? null : (l, p, s, t) -> {
            if (t instanceof TileDistillery ticker) ticker.tick();
        };
    }
}