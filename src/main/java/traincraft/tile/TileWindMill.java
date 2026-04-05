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

public class TileWindMill extends BlockEntity {

    private final ModifiableEnergyStorage energyStorage = new ModifiableEnergyStorage(2000, 200, 0, 0);

    private int windStrength = 0;

    public TileWindMill(BlockPos pos, BlockState state) {
        super(TCTiles.WIND_MILL.get(), pos, state);
    }

    public ModifiableEnergyStorage getEnergyStorage() {
        return energyStorage;
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state, TileWindMill entity) {
        int skyAccess = level.getSkyDarken();
        int exposure = level.getMaxLocalRawBrightness(pos);
        entity.windStrength = Math.max(0, exposure - skyAccess);

        if (entity.windStrength > 0) {
            entity.energyStorage.receiveEnergy(entity.windStrength * 5, false);
            entity.setChanged();
        }
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.putInt("energy", energyStorage.getEnergyStored());
        tag.putInt("windStrength", windStrength);
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        energyStorage.setEnergy(tag.getInt("energy"));
        windStrength = tag.getInt("windStrength");
    }
}
