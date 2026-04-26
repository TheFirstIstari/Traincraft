/*
 * Traincraft
 * Copyright (c) 2011-2024.
 */

package traincraft.blocks.openhearthfurnace;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.jetbrains.annotations.Nullable;

public class TileOpenHearthFurnace extends BlockEntity implements MenuProvider {

    public static final int INPUT_SLOT = 0;
    public static final int FUEL_SLOT = 1;
    public static final int OUTPUT_SLOT = 2;
    public static final int INVENTORY_SIZE = 3;

    private final ItemStackHandler itemHandler = new ItemStackHandler(INVENTORY_SIZE) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
        }

        @Override
        public boolean isItemValid(int slot, ItemStack stack) {
            return switch (slot) {
                case INPUT_SLOT -> true;
                case FUEL_SLOT -> stack.getBurnTime(null) > 0;
                case OUTPUT_SLOT -> false;
                default -> true;
            };
        }
    };

    private int burnTime = 0;
    private int maxBurnTime = 0;
    private int cookTime = 0;
    private int maxCookTime = 200;

    public TileOpenHearthFurnace(BlockPos pos, BlockState state) {
        super(traincraft.tile.TCTiles.OPEN_HEARTH_FURNACE.get(), pos, state);
    }

    public ItemStackHandler getItemHandler() {
        return itemHandler;
    }

    public int getBurnTime() { return burnTime; }
    public int getMaxBurnTime() { return maxBurnTime; }
    public int getCookTime() { return cookTime; }
    public int getMaxCookTime() { return maxCookTime; }

    public static void serverTick(Level level, BlockPos pos, BlockState state, TileOpenHearthFurnace entity) {
        boolean dirty = false;

        if (entity.burnTime > 0) {
            entity.burnTime--;
            dirty = true;
        }

        if (entity.burnTime <= 0) {
            ItemStack fuel = entity.itemHandler.getStackInSlot(FUEL_SLOT);
            if (!fuel.isEmpty()) {
                int fuelTime = fuel.getBurnTime(null);
                if (fuelTime > 0) {
                    entity.burnTime = fuelTime;
                    entity.maxBurnTime = fuelTime;
                    fuel.shrink(1);
                    entity.itemHandler.setStackInSlot(FUEL_SLOT, fuel);
                    dirty = true;
                }
            }
        }

        if (entity.burnTime > 0 && entity.canCraft()) {
            entity.cookTime++;
            if (entity.cookTime >= entity.maxCookTime) {
                entity.cookTime = 0;
                entity.maxCookTime = 200;
                entity.craftItem();
            }
            dirty = true;
        } else {
            if (entity.cookTime != 0) {
                entity.cookTime = 0;
                dirty = true;
            }
        }

        boolean wasActive = state.getValue(BlockOpenHearthFurnace.ACTIVE);
        boolean isActive = entity.burnTime > 0;
        if (wasActive != isActive) {
            level.setBlock(pos, state.setValue(BlockOpenHearthFurnace.ACTIVE, isActive), 3);
        }

        if (dirty) {
            entity.setChanged();
        }
    }

    public static void clientTick(Level level, BlockPos pos, BlockState state, TileOpenHearthFurnace entity) {
    }

    private boolean canCraft() {
        ItemStack input = itemHandler.getStackInSlot(INPUT_SLOT);
        if (input.isEmpty()) return false;

        ItemStack result = getCookingResult(input);
        if (result.isEmpty()) return false;

        ItemStack output = itemHandler.getStackInSlot(OUTPUT_SLOT);
        if (output.isEmpty()) return true;
        if (!ItemStack.isSameItem(output, result)) return false;
        return output.getCount() + result.getCount() <= output.getMaxStackSize();
    }

    private void craftItem() {
        ItemStack input = itemHandler.getStackInSlot(INPUT_SLOT);
        ItemStack result = getCookingResult(input);
        if (result.isEmpty()) return;

        input.shrink(1);
        itemHandler.setStackInSlot(INPUT_SLOT, input);

        ItemStack output = itemHandler.getStackInSlot(OUTPUT_SLOT);
        if (output.isEmpty()) {
            itemHandler.setStackInSlot(OUTPUT_SLOT, result.copy());
        } else {
            output.grow(result.getCount());
            itemHandler.setStackInSlot(OUTPUT_SLOT, output);
        }
    }

    private ItemStack getCookingResult(ItemStack input) {
        if (level == null) return ItemStack.EMPTY;
        var recipes = level.getRecipeManager().getRecipesFor(RecipeType.SMELTING, new SingleRecipeInput(input), level);
        for (RecipeHolder<?> holder : recipes) {
            if (holder.value() instanceof net.minecraft.world.item.crafting.SmokingRecipe smokingRecipe) {
                return smokingRecipe.getResultItem(level.registryAccess());
            }
            if (holder.value() instanceof net.minecraft.world.item.crafting.SmeltingRecipe smeltingRecipe) {
                return smeltingRecipe.getResultItem(level.registryAccess());
            }
        }
        return ItemStack.EMPTY;
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.put("inventory", itemHandler.serializeNBT(registries));
        tag.putInt("burnTime", burnTime);
        tag.putInt("maxBurnTime", maxBurnTime);
        tag.putInt("cookTime", cookTime);
        tag.putInt("maxCookTime", maxCookTime);
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        itemHandler.deserializeNBT(registries, tag.getCompound("inventory"));
        burnTime = tag.getInt("burnTime");
        maxBurnTime = tag.getInt("maxBurnTime");
        cookTime = tag.getInt("cookTime");
        maxCookTime = tag.getInt("maxCookTime");
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("block.traincraft.open_hearth_furnace");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int containerId, Inventory playerInventory, Player player) {
        return new ContainerOpenHearthFurnace(containerId, playerInventory, this);
    }
}
