/*
 * Traincraft
 * Copyright (c) 2011-2024.
 */

package traincraft.tile;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.energy.EnergyStorage;
import net.neoforged.neoforge.items.ItemStackHandler;

public class TileDieselGenerator extends BlockEntity {

    private final ItemStackHandler itemHandler = new ItemStackHandler(1) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
        }
    };

    private final ModifiableEnergyStorage energyStorage = new ModifiableEnergyStorage(10000, 0, 1000, 0);

    private int burnTime = 0;
    private int maxBurnTime = 0;

    public TileDieselGenerator(BlockPos pos, BlockState state) {
        super(TCTiles.GENERATOR_DIESEL.get(), pos, state);
    }

    public ItemStackHandler getItemHandler() {
        return itemHandler;
    }

    public EnergyStorage getEnergyStorage() {
        return energyStorage;
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state, TileDieselGenerator entity) {
        if (entity.burnTime > 0) {
            entity.burnTime--;
            entity.energyStorage.receiveEnergy(40, false);
            entity.setChanged();
        }

        if (entity.burnTime <= 0) {
            ItemStack fuel = entity.itemHandler.getStackInSlot(0);
            if (!fuel.isEmpty()) {
                int fuelTime = fuel.getBurnTime(null);
                if (fuelTime > 0) {
                    entity.burnTime = fuelTime;
                    entity.maxBurnTime = fuelTime;
                    fuel.shrink(1);
                    entity.itemHandler.setStackInSlot(0, fuel);
                    entity.setChanged();
                }
            }
        }
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.put("inventory", itemHandler.serializeNBT(registries));
        tag.putInt("energy", energyStorage.getEnergyStored());
        tag.putInt("burnTime", burnTime);
        tag.putInt("maxBurnTime", maxBurnTime);
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        itemHandler.deserializeNBT(registries, tag.getCompound("inventory"));
        energyStorage.setEnergy(tag.getInt("energy"));
        burnTime = tag.getInt("burnTime");
        maxBurnTime = tag.getInt("maxBurnTime");
    }
}
