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
import traincraft.Traincraft;
import traincraft.blocks.TCBlocks;
import traincraft.recipe.DistilleryRecipe;

public class DistilleryRecipeCategory implements IRecipeCategory<DistilleryRecipe> {
    
    public static final ResourceLocation UID = ResourceLocation.fromNamespaceAndPath(Traincraft.MOD_ID, "distillery");
    public static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(Traincraft.MOD_ID, "textures/gui/distillery_jei.png");
    public static final RecipeType<DistilleryRecipe> TYPE = new RecipeType<>(UID, DistilleryRecipe.class);
    
    private final IDrawable background;
    private final IDrawable icon;
    private final Component title;
    
    public DistilleryRecipeCategory(IGuiHelper guiHelper) {
        this.background = guiHelper.createDrawable(TEXTURE, 0, 0, 166, 60);
        this.icon = guiHelper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(TCBlocks.DISTILLERY.get()));
        this.title = Component.translatable("gui.jei.category.distillery");
    }
    
    @Override
    public RecipeType<DistilleryRecipe> getRecipeType() {
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
        return 60;
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
    public void setRecipe(IRecipeLayoutBuilder builder, DistilleryRecipe recipe, IFocusGroup focuses) {
        // Add input slot
        builder.addSlot(RecipeIngredientRole.INPUT, 30, 20)
            .addIngredients(recipe.getIngredients().get(0));
        
        // Add output slot
        builder.addSlot(RecipeIngredientRole.OUTPUT, 90, 20)
            .addIngredient(VanillaTypes.ITEM_STACK, recipe.getResultItem(null));
    }
}
