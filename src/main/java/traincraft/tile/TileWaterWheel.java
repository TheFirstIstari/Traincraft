/*
 * Traincraft
 * Copyright (c) 2011-2024.
 */

package traincraft.tile;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class TileWaterWheel extends BlockEntity {

    private final ModifiableEnergyStorage energyStorage = new ModifiableEnergyStorage(1000, 100, 0, 0);

    private boolean hasWater = false;

    public TileWaterWheel(BlockPos pos, BlockState state) {
        super(TCTiles.WATER_WHEEL.get(), pos, state);
    }

    public ModifiableEnergyStorage getEnergyStorage() {
        return energyStorage;
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state, TileWaterWheel entity) {
        boolean nearWater = false;
        for (var dir : net.minecraft.core.Direction.values()) {
            var neighbor = level.getBlockState(pos.relative(dir));
            if (neighbor.getBlock() instanceof net.minecraft.world.level.block.LiquidBlock) {
                nearWater = true;
                break;
            }
        }

        if (nearWater != entity.hasWater) {
            entity.hasWater = nearWater;
            entity.setChanged();
        }

        if (entity.hasWater) {
            entity.energyStorage.receiveEnergy(10, false);
            entity.setChanged();
        }
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.putInt("energy", energyStorage.getEnergyStored());
        tag.putBoolean("hasWater", hasWater);
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        energyStorage.setEnergy(tag.getInt("energy"));
        hasWater = tag.getBoolean("hasWater");
    }
}
