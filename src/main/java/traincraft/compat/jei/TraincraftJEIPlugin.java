/*
 * Traincraft
 * Copyright (c) 2011-2024.
 */
package traincraft.compat.jei;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JEIPlugin;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.constants.VanillaTypes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.Minecraft;
import net.minecraft.world.item.crafting.RecipeType;
import traincraft.Traincraft;
import traincraft.blocks.TCBlocks;
import traincraft.recipe.AssemblyTableRecipe;
import traincraft.recipe.DistilleryRecipe;
import traincraft.compat.jei.categories.AssemblyTableRecipeCategory;
import traincraft.compat.jei.categories.DistilleryRecipeCategory;
import traincraft.compat.jei.categories.TrainWorkbenchRecipeCategory;

import java.util.List;
import java.util.ArrayList;

@JEIPlugin
public class TraincraftJEIPlugin implements IModPlugin {
    
    public static final String MOD_ID = "traincraft";
    
    @Override
    public ResourceLocation getPluginUid() {
        return new ResourceLocation(MOD_ID, "jei_plugin");
    }
    
    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        registration.addRecipeCategories(
            new AssemblyTableRecipeCategory(registration.getJeiHelpers().getGuiHelper()),
            new DistilleryRecipeCategory(registration.getJeiHelpers().getGuiHelper()),
            new TrainWorkbenchRecipeCategory(registration.getJeiHelpers().getGuiHelper())
        );
    }
    
    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        // Register assembly table recipes
        List<AssemblyTableRecipe> assemblyRecipes = new ArrayList<>();
        Minecraft.getInstance().level.getRecipeManager()
            .getAllRecipesFor(AssemblyTableRecipe.Type.INSTANCE)
            .forEach(recipe -> assemblyRecipes.add((AssemblyTableRecipe) recipe));
        registration.addRecipes(AssemblyTableRecipeCategory.TYPE, assemblyRecipes);
        
        // Register distillery recipes
        List<DistilleryRecipe> distilleryRecipes = new ArrayList<>();
        Minecraft.getInstance().level.getRecipeManager()
            .getAllRecipesFor(DistilleryRecipe.Type.INSTANCE)
            .forEach(recipe -> distilleryRecipes.add((DistilleryRecipe) recipe));
        registration.addRecipes(DistilleryRecipeCategory.TYPE, distilleryRecipes);
        
        // For train workbench, we'll need to handle vanilla crafting recipes
        // or create a custom recipe system if needed
    }
    
    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        // Register recipe catalysts (blocks that can process recipes)
        registration.addRecipeCatalyst(new ItemStack(TCBlocks.ASSEMBLY_TABLE_I.get()), AssemblyTableRecipeCategory.TYPE);
        registration.addRecipeCatalyst(new ItemStack(TCBlocks.ASSEMBLY_TABLE_II.get()), AssemblyTableRecipeCategory.TYPE);
        registration.addRecipeCatalyst(new ItemStack(TCBlocks.ASSEMBLY_TABLE_III.get()), AssemblyTableRecipeCategory.TYPE);
        registration.addRecipeCatalyst(new ItemStack(TCBlocks.DISTILLERY.get()), DistilleryRecipeCategory.TYPE);
        registration.addRecipeCatalyst(new ItemStack(TCBlocks.TRAIN_WORKBENCH.get()), TrainWorkbenchRecipeCategory.TYPE);
    }
}
