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
import traincraft.liquids.TCLiquids;

/**
 * Base class for diesel-powered locomotives.
 * Unlike the steam variant, a diesel locomotive only needs a diesel/refined-fuel tank; there is no
 * firebox or water boiler. Each consumed millibucket of fuel contributes a fixed number of "burn ticks",
 * during which the locomotive can apply its throttle.
 */
public abstract class LocomotiveDiesel<A extends LocomotiveDiesel<A>> extends AbstractRollingStock<A> {

    public static final int INVENTORY_SIZE = 6;
    /** Burn ticks produced by consuming 1 mB of diesel fuel. */
    public static final int BURN_TICKS_PER_MB = 2;

    private final ItemStackHandler inventory = new ItemStackHandler(INVENTORY_SIZE);

    private final FluidTank fuelTank = new FluidTank(getFuelTankCapacity()) {
        @Override
        public boolean isFluidValid(@NotNull FluidStack stack) {
            return isFuelValid(stack);
        }
    };

    public int burnTime = 0;

    protected LocomotiveDiesel(EntityType<?> type, Level level) {
        super(type, level);
    }

    /** The fuel tank capacity in mB. Subclasses may override. */
    public int getFuelTankCapacity() {
        return 16000;
    }

    /**
     * Whether the given fluid is acceptable as fuel. Defaults to diesel/refined_fuel.
     */
    protected boolean isFuelValid(FluidStack stack) {
        return stack.getFluid() == TCLiquids.DIESEL.get() || stack.getFluid() == TCLiquids.REFINED_FUEL.get();
    }

    public int getBurnTime() {
        return this.burnTime;
    }

    @Override
    public boolean canApplyThrottle() {
        // Engine is running while burnTime is ticking down, or it's ready to consume fuel from the tank.
        return this.burnTime > 0 || this.fuelTank.getFluidAmount() > 0;
    }

    @Override
    public IItemHandler getInventory(@Nullable Direction side) {
        return this.inventory;
    }

    @Override
    public IFluidHandler getFluidTank(@Nullable Direction side) {
        return this.fuelTank;
    }

    @Override
    public void tick() {
        super.tick();
        if (this.level().isClientSide) return;

        // Only consume fuel when the locomotive is actually being driven forward or backward.
        double speed = this.getDeltaMovement().horizontalDistance();
        if (speed > 0.01) {
            if (this.burnTime > 0) {
                this.burnTime--;
            } else if (this.fuelTank.getFluidAmount() > 0) {
                this.fuelTank.drain(1, IFluidHandler.FluidAction.EXECUTE);
                this.burnTime = BURN_TICKS_PER_MB;
            }

            // Periodically loop the engine sound while the loco is running.
            if (this.burnTime > 0 && this.tickCount % 30 == 0) {
                float volume = (float) Math.min(0.3 + speed * 0.5, 1.0);
                this.level().playSound(null, getX(), getY(), getZ(),
                    traincraft.TCSounds.LOCOMOTIVE_DIESEL_ENGINE.get(),
                    net.minecraft.sounds.SoundSource.NEUTRAL, volume, 1.0f);
            }
        }
    }

    @Override
    public boolean handlePlayerClickWithItem(net.minecraft.world.entity.player.Player player,
                                             net.minecraft.world.InteractionHand hand,
                                             net.minecraft.world.item.ItemStack stack,
                                             net.minecraft.world.phys.Vec3 hitVector) {
        // Refuel the locomotive from a Traincraft canister holding diesel or refined fuel.
        if (stack.getItem() instanceof traincraft.items.ItemCanister) {
            FluidStack stored = traincraft.items.ItemCanister.getStored(stack);
            if (!stored.isEmpty() && isFuelValid(stored)) {
                int space = this.fuelTank.getCapacity() - this.fuelTank.getFluidAmount();
                if (this.fuelTank.getFluidAmount() > 0
                    && this.fuelTank.getFluid().getFluid() != stored.getFluid()) {
                    return false;
                }
                int transfer = Math.min(stored.getAmount(), space);
                if (transfer > 0) {
                    this.fuelTank.fill(new FluidStack(stored.getFluid(), transfer), IFluidHandler.FluidAction.EXECUTE);
                    traincraft.items.ItemCanister.drain(stack, transfer);
                    return true;
                }
            }
        }
        return super.handlePlayerClickWithItem(player, hand, stack, hitVector);
    }

    @Override
    protected void readFromNBT(CompoundTag nbt, SyncState state) {
        super.readFromNBT(nbt, state);
        this.burnTime = nbt.getInt("burnTime");

        CompoundTag invTag = nbt.getCompound("locoInventory");
        this.inventory.deserializeNBT(this.registryAccess(), invTag);

        CompoundTag fluidTag = nbt.getCompound("locoFuelTank");
        this.fuelTank.readFromNBT(this.registryAccess(), fluidTag);
    }

    @Override
    protected void writeToNBT(CompoundTag nbt, SyncState state) {
        super.writeToNBT(nbt, state);
        nbt.putInt("burnTime", this.burnTime);

        CompoundTag invTag = this.inventory.serializeNBT(this.registryAccess());
        nbt.put("locoInventory", invTag);

        CompoundTag fluidTag = this.fuelTank.writeToNBT(this.registryAccess(), new CompoundTag());
        nbt.put("locoFuelTank", fluidTag);
    }
}
