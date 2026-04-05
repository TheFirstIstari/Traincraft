/*
 * Traincraft
 * Copyright (c) 2011-2024.
 */

package traincraft.blocks.assemblytable;

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
import traincraft.recipe.AssemblyTableRecipe;
import traincraft.tile.TCTiles;

public class TileAssemblyTable extends BlockEntity implements MenuProvider {

    public static final int CRAFTING_START = 0;
    public static final int CRAFTING_END = 9;
    public static final int STORAGE_START = 10;
    public static final int STORAGE_END = 17;
    public static final int OUTPUT_START = 18;
    public static final int OUTPUT_END = 25;
    public static final int INVENTORY_SIZE = 26;

    private final ItemStackHandler itemHandler = new ItemStackHandler(INVENTORY_SIZE) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
            if (slot >= CRAFTING_START && slot <= CRAFTING_END) {
                checkForRecipe();
            }
        }

        @Override
        public int getSlotLimit(int slot) {
            return 64;
        }
    };

    @Nullable
    private AssemblyTableRecipe currentRecipe;

    public TileAssemblyTable(BlockPos pos, BlockState state) {
        super(TCTiles.ASSEMBLY_TABLE_I.get(), pos, state);
    }

    public int getTier() {
        if (level != null) {
            BlockState state = level.getBlockState(worldPosition);
            if (state.getBlock() instanceof BlockAssemblyTable block) {
                return block.getTier();
            }
        }
        return 1;
    }

    public ItemStackHandler getItemHandler() {
        return itemHandler;
    }

    @Nullable
    public AssemblyTableRecipe getCurrentRecipe() {
        return currentRecipe;
    }

    private void checkForRecipe() {
        if (level == null) return;

        for (int i = OUTPUT_START; i <= OUTPUT_END; i++) {
            itemHandler.setStackInSlot(i, ItemStack.EMPTY);
        }

        var recipeManager = level.getRecipeManager();
        for (var holder : recipeManager.getAllRecipesFor(traincraft.recipe.TCRecipes.ASSEMBLY_TABLE_TYPE.get())) {
            AssemblyTableRecipe recipe = holder.value();
            if (recipe.getTier() > getTier()) continue;

            boolean matches = true;
            for (int i = 0; i < 10; i++) {
                var ingredient = recipe.getIngredient(i);
                ItemStack stack = itemHandler.getStackInSlot(i);
                if (!ingredient.test(stack)) {
                    matches = false;
                    break;
                }
            }

            if (matches) {
                currentRecipe = recipe;
                ItemStack output = recipe.getResultItem(level.registryAccess());
                itemHandler.setStackInSlot(OUTPUT_START, output.copy());
                setChanged();
                return;
            }
        }
        currentRecipe = null;
    }

    public void onItemCrafted() {
        if (currentRecipe == null || level == null) return;

        for (int i = 0; i < 10; i++) {
            var ingredient = currentRecipe.getIngredient(i);
            itemHandler.extractItem(i, ingredient.count(), false);
        }

        currentRecipe = null;
        checkForRecipe();
        setChanged();
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
        return Component.translatable("block.traincraft.assembly_table_" + getTier());
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int containerId, Inventory playerInventory, Player player) {
        return new ContainerAssemblyTable(containerId, playerInventory, this);
    }
}
