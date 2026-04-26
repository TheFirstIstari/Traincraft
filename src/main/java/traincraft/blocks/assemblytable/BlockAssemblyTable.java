/*
 * Traincraft
 * Copyright (c) 2011-2024.
 */

package traincraft.blocks.assemblytable;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;
import traincraft.TCSounds;

public class BlockAssemblyTable extends BaseEntityBlock {

    private final int tier;

    public BlockAssemblyTable(Properties properties, int tier) {
        super(properties);
        this.tier = tier;
    }

    public int getTier() {
        return this.tier;
    }

    public static final MapCodec<BlockAssemblyTable> CODEC = MapCodec.unit(() -> new BlockAssemblyTable(Properties.of(), 1));

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new TileAssemblyTable(pos, state);
    }

    @Override
    protected RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

        @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        if (!level.isClientSide) {
            BlockEntity blockEntity = level.getBlockEntity(pos);
            if (blockEntity instanceof TileAssemblyTable tile) {
                player.openMenu(tile);
                // Play sound when opening the assembly table
                level.playSound(null, pos, TCSounds.ASSEMBLY_TABLE_CRAFT.get(), SoundSource.BLOCKS, 0.3f, 1.0f);
            }
        }
        return InteractionResult.SUCCESS;
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return createTickerHelper(type, traincraft.tile.TCTiles.ASSEMBLY_TABLE_I.get(),
            (l, p, s, tile) -> {});
    }
}
