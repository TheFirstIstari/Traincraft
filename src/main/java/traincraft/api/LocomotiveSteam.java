/*
 * Traincraft
 * Copyright (c) 2011-2024.
 */

package traincraft.api;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.util.INBTSerializable;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.fluids.capability.templates.FluidTank;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class LocomotiveSteam<A extends LocomotiveSteam<A>> extends AbstractRollingStock<A> {

    public static final int BURN_SLOT = 0;
    public static final int WATER_SLOT = 1;
    public static final int INVENTORY_SIZE = 11;

    private final ItemStackHandler inventory = new ItemStackHandler(INVENTORY_SIZE) {
        @Override
        public boolean isItemValid(int slot, @NotNull ItemStack stack) {
            return LocomotiveSteam.this.isItemValidForInventory(slot, stack);
        }

        @Override
        public int getSlotLimit(int slot) {
            return slot == BURN_SLOT ? 1 : 64;
        }
    };

    private final FluidTank waterTank = new FluidTank(getWaterTankCapacity()) {
        @Override
        public boolean isFluidValid(@NotNull FluidStack stack) {
            return stack.getFluid() == getWaterFluid();
        }
    };

    public int maxBurnTime = 0;
    public int burnTime = 0;
    public double temperature = getDefaultTemperature();

    protected LocomotiveSteam(EntityType<?> type, Level level) {
        super(type, level);
    }

    public double getTemperature() {
        return this.temperature;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    public double getMaximumTemperature() {
        return 473.15;
    }

    public double getMinimumTemperature() {
        return 253.15;
    }

    public double getDefaultTemperature() {
        return 293.15;
    }

    public int getWaterTankCapacity() {
        return 16000;
    }

    protected net.minecraft.world.level.material.Fluid getWaterFluid() {
        return net.minecraft.world.level.material.Fluids.WATER;
    }

    protected boolean isItemValidForInventory(int slot, ItemStack stack) {
        return false;
    }

    @Override
    public IItemHandler getInventory(@Nullable Direction side) {
        return this.inventory;
    }

    @Override
    public IFluidHandler getFluidTank(@Nullable Direction side) {
        return this.waterTank;
    }

    @Override
    public void tick() {
        super.tick();

        if (this.level().isClientSide) {
            return;
        }

        if (this.burnTime > 0) {
            this.burnTime--;
            this.temperature = Math.min(this.temperature + 0.5, getMaximumTemperature());
        } else {
            this.temperature = Math.max(this.temperature - 0.1, getDefaultTemperature());
        }

        if (this.burnTime <= 0) {
            ItemStack burnStack = this.inventory.getStackInSlot(BURN_SLOT);
            if (!burnStack.isEmpty()) {
                int burnTime = burnStack.getBurnTime(null);
                if (burnTime > 0) {
                    this.maxBurnTime = burnTime;
                    this.burnTime = burnTime;
                    burnStack.shrink(1);
                    this.inventory.setStackInSlot(BURN_SLOT, burnStack);
                }
            }
        }

        if (this.temperature > 373.15 && this.waterTank.getFluidAmount() > 0) {
            this.waterTank.drain(1, IFluidHandler.FluidAction.EXECUTE);
        }
    }

    @Override
    protected void readFromNBT(CompoundTag nbt, SyncState state) {
        super.readFromNBT(nbt, state);
        this.burnTime = nbt.getInt("burnTime");
        this.maxBurnTime = nbt.getInt("maxBurnTime");
        this.temperature = nbt.getDouble("temperature");

        CompoundTag invTag = nbt.getCompound("locoInventory");
        this.inventory.deserializeNBT(this.registryAccess(), invTag);

        CompoundTag fluidTag = nbt.getCompound("locoWaterTank");
        this.waterTank.readFromNBT(this.registryAccess(), fluidTag);
    }

    @Override
    protected void writeToNBT(CompoundTag nbt, SyncState state) {
        super.writeToNBT(nbt, state);
        nbt.putInt("burnTime", this.burnTime);
        nbt.putInt("maxBurnTime", this.maxBurnTime);
        nbt.putDouble("temperature", this.temperature);

        CompoundTag invTag = this.inventory.serializeNBT(this.registryAccess());
        nbt.put("locoInventory", invTag);

        CompoundTag fluidTag = this.waterTank.writeToNBT(this.registryAccess(), new CompoundTag());
        nbt.put("locoWaterTank", fluidTag);
    }
}
