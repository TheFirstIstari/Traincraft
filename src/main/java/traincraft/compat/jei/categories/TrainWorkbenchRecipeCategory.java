/*
 * Traincraft
 * Copyright (c) 2011-2024.
 */
package traincraft.compat.jei.categories;

import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.RecipeType;
import traincraft.Traincraft;
import traincraft.blocks.TCBlocks;

public class TrainWorkbenchRecipeCategory implements IRecipeCategory<CraftingRecipe> {
    
    public static final ResourceLocation UID = new ResourceLocation(Traincraft.MOD_ID, "train_workbench");
    public static final ResourceLocation TEXTURE = new ResourceLocation(Traincraft.MOD_ID, "textures/gui/train_workbench_jei.png");
    public static final RecipeType<CraftingRecipe> TYPE = new RecipeType<>(UID, CraftingRecipe.class);
    
    private final IDrawable background;
    private final IDrawable icon;
    private final Component title;
    
    public TrainWorkbenchRecipeCategory(IGuiHelper guiHelper) {
        this.background = guiHelper.createDrawable(TEXTURE, 0, 0, 166, 100);
        this.icon = guiHelper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(TCBlocks.TRAIN_WORKBENCH.get()));
        this.title = Component.translatable("gui.jei.category.train_workbench");
    }
    
    @Override
    public RecipeType<CraftingRecipe> getRecipeType() {
        return TYPE;
    }
    
    @Override
    public Component getTitle() {
        return this.title;
    }
    
    @Override
    public int getWidth() {
        return 166;
    }
    
    @Override
    public int getHeight() {
        return 100;
    }
    
    @Override
    public IDrawable getBackground() {
        return this.background;
    }
    
    @Override
    public IDrawable getIcon() {
        return this.icon;
    }
    
    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, CraftingRecipe recipe, IFocusGroup focuses) {
        // Add 3x3 crafting grid
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                int index = row * 3 + col;
                if (index < recipe.getIngredients().size()) {
                    builder.addSlot(RecipeIngredientRole.INPUT, 30 + col * 18, 16 + row * 18)
                        .addIngredients(recipe.getIngredients().get(index));
                }
            }
        }
        
        // Add output slot
        builder.addSlot(RecipeIngredientRole.OUTPUT, 124, 34)
            .addIngredient(VanillaTypes.ITEM_STACK, recipe.getResultItem(null));
    }
}
