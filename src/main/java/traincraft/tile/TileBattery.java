/*
 * Traincraft
 * Copyright (c) 2011-2024.
 */

package traincraft.tile;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.energy.EnergyStorage;

public class TileBattery extends BlockEntity {

    private final ModifiableEnergyStorage energyStorage = new ModifiableEnergyStorage(100000, 1000, 1000, 0);

    public TileBattery(BlockPos pos, BlockState state) {
        super(TCTiles.BATTERY.get(), pos, state);
    }

    public EnergyStorage getEnergyStorage() {
        return energyStorage;
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.putInt("energy", energyStorage.getEnergyStored());
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        energyStorage.setEnergy(tag.getInt("energy"));
    }

    private static class ModifiableEnergyStorage extends EnergyStorage {
        ModifiableEnergyStorage(int capacity, int maxReceive, int maxExtract, int energy) {
            super(capacity, maxReceive, maxExtract, energy);
        }
        void setEnergy(int energy) { this.energy = energy; }
    }
}
