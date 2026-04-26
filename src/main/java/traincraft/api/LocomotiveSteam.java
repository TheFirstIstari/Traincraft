/*
 * Traincraft
 * Copyright (c) 2011-2024.
 */

package traincraft.api;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.common.util.INBTSerializable;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.fluids.capability.templates.FluidTank;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import traincraft.TCSounds;

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
    public boolean canApplyThrottle() {
        // Need a hot boiler (fuel actively burning or recently burned) and water in the tank.
        return this.temperature > 373.15 && this.waterTank.getFluidAmount() > 0;
    }

    @Override
    public net.minecraft.network.chat.Component statusForDriver() {
        int waterPct = (int) (100.0 * this.waterTank.getFluidAmount() / this.waterTank.getCapacity());
        int tempC = (int) (this.temperature - 273.15);
        return net.minecraft.network.chat.Component.literal(
            String.format("Steam: %d°C  Water: %d%%  Burn: %d", tempC, waterPct, this.burnTime)
        );
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

        // Play engine sound when the locomotive is moving
        if (this.getDeltaMovement().length() > 0.01 && this.tickCount % 30 == 0) {
            // Throttle the engine loop to once every ~1.5s so we don't queue 20 events/second.
            double speed = this.getDeltaMovement().horizontalDistance();
            float volume = (float) Math.min(0.3 + (speed * 0.5), 1.0);
            this.level().playSound(null, this.getX(), this.getY(), this.getZ(),
                traincraft.TCSounds.LOCOMOTIVE_STEAM_ENGINE.get(), SoundSource.NEUTRAL,
                volume, 1.0f);
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

    
    /**
     * Plays the locomotive whistle sound.
     * This method can be called when the player activates the whistle.
     */
    public void playWhistleSound() {
        if (!this.level().isClientSide) {
            this.level().playSound(null, this.getX(), this.getY(), this.getZ(), 
                TCSounds.LOCOMOTIVE_STEAM_WHISTLE.get(), SoundSource.NEUTRAL, 
                1.0f, 1.0f);
        }
    }

    
    @Override
    public boolean handlePlayerClickWithItem(Player player, InteractionHand hand, ItemStack stack, Vec3 hitVector) {
        // 1) Refill the water tank from a vanilla water bucket.
        if (stack.is(net.minecraft.world.item.Items.WATER_BUCKET)) {
            int filled = this.waterTank.fill(new FluidStack(net.minecraft.world.level.material.Fluids.WATER, 1000),
                IFluidHandler.FluidAction.EXECUTE);
            if (filled > 0) {
                if (!player.getAbilities().instabuild) {
                    stack.shrink(1);
                    ItemStack empty = new ItemStack(net.minecraft.world.item.Items.BUCKET);
                    if (!player.getInventory().add(empty)) {
                        player.drop(empty, false);
                    }
                }
                return true;
            }
        }

        // 2) Refill the water tank from a Traincraft canister holding the loco's water fluid.
        if (stack.getItem() instanceof traincraft.items.ItemCanister) {
            FluidStack stored = traincraft.items.ItemCanister.getStored(stack);
            if (!stored.isEmpty() && stored.getFluid() == getWaterFluid()) {
                int space = this.waterTank.getCapacity() - this.waterTank.getFluidAmount();
                int transfer = Math.min(stored.getAmount(), space);
                if (transfer > 0) {
                    this.waterTank.fill(new FluidStack(getWaterFluid(), transfer), IFluidHandler.FluidAction.EXECUTE);
                    traincraft.items.ItemCanister.drain(stack, transfer);
                    return true;
                }
            }
        }

        // 3) Push coal or charcoal into the burn slot.
        if (stack.is(net.minecraft.world.item.Items.COAL) || stack.is(net.minecraft.world.item.Items.CHARCOAL)) {
            ItemStack burn = this.inventory.getStackInSlot(BURN_SLOT);
            if (burn.isEmpty()) {
                ItemStack one = stack.copy();
                one.setCount(1);
                this.inventory.setStackInSlot(BURN_SLOT, one);
                if (!player.getAbilities().instabuild) stack.shrink(1);
                return true;
            }
        }

        // 4) Otherwise, sneak + click plays the whistle.
        if (player.isShiftKeyDown()) {
            playWhistleSound();
            return true;
        }

        return super.handlePlayerClickWithItem(player, hand, stack, hitVector);
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
