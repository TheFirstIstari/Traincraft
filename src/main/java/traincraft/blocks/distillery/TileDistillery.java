/*
 * Traincraft
 * Copyright (c) 2011-2024.
 */

package traincraft.blocks.distillery;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.fluids.capability.templates.FluidTank;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.jetbrains.annotations.Nullable;
import traincraft.TCSounds;
import traincraft.recipe.DistilleryRecipe;

public class TileDistillery extends BlockEntity implements MenuProvider {

    public static final int INPUT_SLOT = 0;
    public static final int BURN_SLOT = 1;
    public static final int OUTPUT_SLOT = 2;
    public static final int CONTAINER_INPUT_SLOT = 3;
    public static final int CONTAINER_OUTPUT_SLOT = 4;
    public static final int INVENTORY_SIZE = 5;
    public static final int FLUID_TANK_CAPACITY = 16000;

    private final ItemStackHandler itemHandler = createItemHandler();
    private final FluidTank fluidTank = new FluidTank(FLUID_TANK_CAPACITY);

    private int burnTime = 0;
    private int maxBurnTime = 0;
    private int recipeBurnTime = 0;
    private int maxRecipeBurnTime = 0;

    public TileDistillery(BlockPos pos, BlockState state) {
        super(traincraft.tile.TCTiles.DISTILLERY.get(), pos, state);
    }

    private ItemStackHandler createItemHandler() {
        return new ItemStackHandler(INVENTORY_SIZE) {
            @Override
            protected void onContentsChanged(int slot) {
                setChanged();
            }

            @Override
            public boolean isItemValid(int slot, ItemStack stack) {
                return switch (slot) {
                    case INPUT_SLOT -> true;
                    case BURN_SLOT -> stack.getBurnTime(null) > 0;
                    case OUTPUT_SLOT, CONTAINER_OUTPUT_SLOT -> false;
                    default -> true;
                };
            }
        };
    }

        public static void serverTick(Level level, BlockPos pos, BlockState state, TileDistillery entity) {
        entity.burnTime--;
        if (entity.burnTime <= 0) {
            entity.burnTime = entity.maxBurnTime = 0;
        }

        if (entity.recipeBurnTime > 0) {
            entity.recipeBurnTime--;
            if (entity.recipeBurnTime <= 0) {
                entity.completeRecipe();
                entity.recipeBurnTime = entity.maxRecipeBurnTime = 0;
                level.setBlock(pos, state.setValue(BlockDistillery.ACTIVE, false), 3);
            }
        }

        if (entity.burnTime <= 0) {
            entity.tryConsumeFuel();
        }

        if (entity.burnTime > 0 && entity.recipeBurnTime <= 0) {
            entity.tryStartRecipe();
        }

        // Move fluid from the internal tank into a player-provided canister or bucket.
        entity.tryFillContainer();

        // Play distillery processing sound periodically
        if (entity.burnTime > 0 && level.getGameTime() % 20 == 0) {
            level.playSound(null, pos, TCSounds.DISTILLERY_PROCESS.get(), 
                net.minecraft.sounds.SoundSource.BLOCKS, 0.3f, 1.0f);
        }

        entity.setChanged();
    }

    public static void clientTick(Level level, BlockPos pos, BlockState state, TileDistillery entity) {
    }

    private void tryConsumeFuel() {
        ItemStack burnStack = itemHandler.getStackInSlot(BURN_SLOT);
        if (!burnStack.isEmpty()) {
            int fuelTime = burnStack.getBurnTime(null);
            if (fuelTime > 0) {
                this.burnTime = fuelTime;
                this.maxBurnTime = fuelTime;
                burnStack.shrink(1);
                itemHandler.setStackInSlot(BURN_SLOT, burnStack);
                setChanged();
            }
        }
    }

    private void tryStartRecipe() {
        if (level == null) return;
        ItemStack input = itemHandler.getStackInSlot(INPUT_SLOT);
        if (input.isEmpty()) return;

        var recipeManager = level.getRecipeManager();
        for (RecipeHolder<DistilleryRecipe> holder : recipeManager.getAllRecipesFor(DistilleryRecipe.Type.INSTANCE)) {
            DistilleryRecipe recipe = holder.value();
            if (recipe.matches(input, level)) {
                ItemStack output = recipe.getResultItem(level.registryAccess());
                ItemStack currentOutput = itemHandler.getStackInSlot(OUTPUT_SLOT);
                if (!currentOutput.isEmpty() && !ItemStack.isSameItem(currentOutput, output)) continue;
                if (currentOutput.getCount() + output.getCount() > output.getMaxStackSize()) continue;

                FluidStack fluidOutput = recipe.getFluidOutput();
                if (fluidOutput != null && !fluidOutput.isEmpty()) {
                    if (!fluidTank.isEmpty() && !fluidTank.getFluid().isFluidEqual(fluidOutput)) continue;
                    if (fluidTank.getFluidAmount() + fluidOutput.getAmount() > fluidTank.getCapacity()) continue;
                }

                input.shrink(1);
                itemHandler.setStackInSlot(INPUT_SLOT, input);

                if (currentOutput.isEmpty()) {
                    itemHandler.setStackInSlot(OUTPUT_SLOT, output.copy());
                } else {
                    currentOutput.grow(output.getCount());
                }

                if (fluidOutput != null && !fluidOutput.isEmpty()) {
                    fluidTank.fill(fluidOutput, IFluidHandler.FluidAction.EXECUTE);
                }

                this.recipeBurnTime = recipe.getBurnTime();
                this.maxRecipeBurnTime = recipe.getBurnTime();
                level.setBlock(worldPosition, getBlockState().setValue(BlockDistillery.ACTIVE, true), 3);
                setChanged();
                return;
            }
        }
    }

    private void completeRecipe() {
    }

    /**
     * Move up to one canister/bucket worth of fluid out of the tank into the container slot.
     * <p>
     * Supports two container types:
     * <ul>
     *     <li>An empty Traincraft canister: filled with up to its remaining capacity from the tank.</li>
     *     <li>A vanilla empty bucket: when the tank holds water, swap for a water bucket.</li>
     * </ul>
     * The result is placed in {@link #CONTAINER_OUTPUT_SLOT}, leaving the input slot empty.
     */
    private void tryFillContainer() {
        if (fluidTank.isEmpty()) return;
        ItemStack input = itemHandler.getStackInSlot(CONTAINER_INPUT_SLOT);
        if (input.isEmpty()) return;
        ItemStack outputSlot = itemHandler.getStackInSlot(CONTAINER_OUTPUT_SLOT);

        // Canister handling.
        if (input.getItem() instanceof traincraft.items.ItemCanister) {
            // Either move the existing canister forward (single-item slot) or copy it after filling.
            // Only fill if the output slot is empty so we don't lose work-in-progress canisters.
            if (!outputSlot.isEmpty()) return;
            ItemStack toFill = input.copy();
            int filled = traincraft.items.ItemCanister.fill(toFill, fluidTank.getFluid(),
                Math.min(fluidTank.getFluidAmount(), 1000));
            if (filled <= 0) return;
            fluidTank.drain(filled, IFluidHandler.FluidAction.EXECUTE);
            input.shrink(1);
            itemHandler.setStackInSlot(CONTAINER_INPUT_SLOT, input);
            itemHandler.setStackInSlot(CONTAINER_OUTPUT_SLOT, toFill);
            setChanged();
            return;
        }

        // Vanilla bucket -> water bucket conversion.
        if (input.is(net.minecraft.world.item.Items.BUCKET)
            && fluidTank.getFluid().getFluid() == net.minecraft.world.level.material.Fluids.WATER
            && fluidTank.getFluidAmount() >= 1000
            && outputSlot.isEmpty()) {
            fluidTank.drain(1000, IFluidHandler.FluidAction.EXECUTE);
            input.shrink(1);
            itemHandler.setStackInSlot(CONTAINER_INPUT_SLOT, input);
            itemHandler.setStackInSlot(CONTAINER_OUTPUT_SLOT, new ItemStack(net.minecraft.world.item.Items.WATER_BUCKET));
            setChanged();
        }
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.put("inventory", itemHandler.serializeNBT(registries));
        tag.put("fluidTank", fluidTank.writeToNBT(registries, new CompoundTag()));
        tag.putInt("burnTime", burnTime);
        tag.putInt("maxBurnTime", maxBurnTime);
        tag.putInt("recipeBurnTime", recipeBurnTime);
        tag.putInt("maxRecipeBurnTime", maxRecipeBurnTime);
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        itemHandler.deserializeNBT(registries, tag.getCompound("inventory"));
        fluidTank.readFromNBT(registries, tag.getCompound("fluidTank"));
        burnTime = tag.getInt("burnTime");
        maxBurnTime = tag.getInt("maxBurnTime");
        recipeBurnTime = tag.getInt("recipeBurnTime");
        maxRecipeBurnTime = tag.getInt("maxRecipeBurnTime");
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("block.traincraft.distillery");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int containerId, Inventory playerInventory, Player player) {
        return new ContainerDistillery(containerId, playerInventory, this);
    }

    public IItemHandler getItemHandler() {
        return itemHandler;
    }

    public FluidTank getFluidTank() {
        return fluidTank;
    }

    public int getBurnTime() { return burnTime; }
    public int getMaxBurnTime() { return maxBurnTime; }
    public int getRecipeBurnTime() { return recipeBurnTime; }
    public int getMaxRecipeBurnTime() { return maxRecipeBurnTime; }
}
