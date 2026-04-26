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
import traincraft.recipe.AssemblyTableRecipe;
import traincraft.recipe.NumberedIngredient;

public class AssemblyTableRecipeCategory implements IRecipeCategory<AssemblyTableRecipe> {
    
    public static final ResourceLocation UID = ResourceLocation.fromNamespaceAndPath(Traincraft.MOD_ID, "assembly_table");
    public static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(Traincraft.MOD_ID, "textures/gui/assembly_table_jei.png");
    public static final RecipeType<AssemblyTableRecipe> TYPE = new RecipeType<>(UID, AssemblyTableRecipe.class);
    
    private final IDrawable background;
    private final IDrawable icon;
    private final Component title;
    
    public AssemblyTableRecipeCategory(IGuiHelper guiHelper) {
        this.background = guiHelper.createDrawable(TEXTURE, 0, 0, 166, 130);
        this.icon = guiHelper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(TCBlocks.ASSEMBLY_TABLE_I.get()));
        this.title = Component.translatable("gui.jei.category.assembly_table");
    }
    
    @Override
    public RecipeType<AssemblyTableRecipe> getRecipeType() {
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
        return 130;
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
    public void setRecipe(IRecipeLayoutBuilder builder, AssemblyTableRecipe recipe, IFocusGroup focuses) {
        // Add input slots (18 total)
        for (int i = 0; i < 18; i++) {
            NumberedIngredient ingredient = recipe.getIngredient(i);
            if (!ingredient.ingredient().isEmpty()) {
                builder.addSlot(RecipeIngredientRole.INPUT, 8 + (i % 6) * 18, 8 + (i / 6) * 18)
                    .addIngredients(ingredient.ingredient())
                    .addTooltipCallback((recipeSlotView, tooltip) -> {
                        if (ingredient.count() > 1) {
                            tooltip.add(Component.literal(ingredient.count() + "x"));
                        }
                    });
            }
        }
        
        // Add output slot
        builder.addSlot(RecipeIngredientRole.OUTPUT, 134, 35)
            .addIngredient(VanillaTypes.ITEM_STACK, recipe.getResultItem(null));
    }
}
