/*
 * Traincraft
 * Copyright (c) 2011-2024.
 */

package traincraft.blocks.trainworkbench;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.jetbrains.annotations.Nullable;
import traincraft.recipe.TCRecipes;
import traincraft.recipe.TrainWorkbenchRecipe;

public class TileTrainWorkbench extends BlockEntity implements MenuProvider {

    public static final int GRID_SIZE = 9;
    public static final int OUTPUT_SLOT = 9;
    public static final int INVENTORY_SIZE = 10;

    @Nullable
    private TrainWorkbenchRecipe currentRecipe;

    private final ItemStackHandler itemHandler = new ItemStackHandler(INVENTORY_SIZE) {
        @Override
        protected void onContentsChanged(int slot) {
            // Re-evaluate the recipe whenever the input grid is changed.
            // The output slot is recomputed each time and never directly persisted as recipe state.
            if (slot >= 0 && slot < GRID_SIZE) {
                refreshRecipe();
            }
            setChanged();
        }

        @Override
        public boolean isItemValid(int slot, ItemStack stack) {
            // Player should not be able to insert into the output slot.
            return slot != OUTPUT_SLOT;
        }
    };

    public TileTrainWorkbench(BlockPos pos, BlockState state) {
        super(traincraft.tile.TCTiles.TRAIN_WORKBENCH.get(), pos, state);
    }

    public ItemStackHandler getItemHandler() {
        return itemHandler;
    }

    private void refreshRecipe() {
        if (level == null) return;
        currentRecipe = null;
        for (var holder : level.getRecipeManager().getAllRecipesFor(TCRecipes.TRAIN_WORKBENCH_TYPE.get())) {
            TrainWorkbenchRecipe recipe = holder.value();
            if (recipe.matches(itemHandler)) {
                currentRecipe = recipe;
                break;
            }
        }
        ItemStack preview = currentRecipe == null
            ? ItemStack.EMPTY
            : currentRecipe.getResultItem(level.registryAccess()).copy();
        // Set silently — onContentsChanged is fired but slot index is OUTPUT_SLOT so we won't recurse.
        itemHandler.setStackInSlot(OUTPUT_SLOT, preview);
    }

    /**
     * Consume one of each ingredient in the grid. Called by the output slot when the player takes the result.
     */
    public void consumeIngredients() {
        if (currentRecipe == null) return;
        for (int i = 0; i < GRID_SIZE; i++) {
            if (!currentRecipe.ingredient(i).isEmpty()) {
                itemHandler.extractItem(i, 1, false);
            }
        }
        // Re-evaluate; if grid still matches, output slot will be repopulated for the next craft.
        refreshRecipe();
    }

    @Nullable
    public TrainWorkbenchRecipe getCurrentRecipe() {
        return currentRecipe;
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.put("inventory", itemHandler.serializeNBT(registries));
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        itemHandler.deserializeNBT(registries, tag.getCompound("inventory"));
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("block.traincraft.train_workbench");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int containerId, Inventory playerInventory, Player player) {
        return new ContainerTrainWorkbench(containerId, playerInventory, this);
    }
}
