/*
 * Traincraft
 * Copyright (c) 2011-2024.
 */

package traincraft.datagen;

import com.google.gson.JsonObject;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.common.crafting.conditions.IConditionBuilder;
import net.minecraftforge.registries.ForgeRegistries;
import traincraft.Traincraft;
import traincraft.items.TCItems;
import traincraft.blocks.TCBlocks;
import traincraft.recipe.AssemblyTableRecipe;
import traincraft.recipe.DistilleryRecipe;
import traincraft.recipe.NumberedIngredient;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

public class TCRecipeProvider extends RecipeProvider implements IConditionBuilder {
    
    public TCRecipeProvider(PackOutput output) {
        super(output);
    }
    
    @Override
    protected void buildRecipes(RecipeOutput consumer) {
        // Generate standard crafting table recipes
        generateCraftingTableRecipes(consumer);
        
        // Generate train workbench recipes
        generateTrainWorkbenchRecipes(consumer);
        
        // Generate assembly table recipes
        generateAssemblyTableRecipes(consumer);
        
        // Generate distillery recipes
        generateDistilleryRecipes(consumer);
    }
    
    private void generateCraftingTableRecipes(RecipeOutput consumer) {
        // Basic item recipes that can be crafted in a standard crafting table
        // Steel Ingot from Iron Ingot and Coal in Furnace
        ShapedRecipeBuilder.shaped(TCItems.STEEL_INGOT.get())
            .pattern("ICI")
            .pattern("CIC")
            .pattern("ICI")
            .define('I', Items.IRON_INGOT)
            .define('C', Items.COAL)
            .unlockedBy("has_iron", has(Items.IRON_INGOT))
            .save(consumer, name(TCItems.STEEL_INGOT.get()));
            
        // Steel Dust from Iron Ingot and Coal in Furnace
        ShapedRecipeBuilder.shaped(TCItems.STEEL_DUST.get())
            .pattern(" C ")
            .pattern("CIC")
            .pattern(" C ")
            .define('I', Items.IRON_INGOT)
            .define('C', Items.COAL)
            .unlockedBy("has_iron", has(Items.IRON_INGOT))
            .save(consumer, name(TCItems.STEEL_DUST.get()) + "_from_iron");
            
        // Graphite from Coal in Furnace
        ShapedRecipeBuilder.shaped(TCItems.GRAPHITE.get())
            .pattern("CCC")
            .pattern("CCC")
            .pattern("CCC")
            .define('C', Items.COAL)
            .unlockedBy("has_coal", has(Items.COAL))
            .save(consumer, name(TCItems.GRAPHITE.get()));
            
        // Wrench
        ShapedRecipeBuilder.shaped(TCItems.WRENCH.get())
            .pattern(" I ")
            .pattern(" II")
            .pattern("  I")
            .define('I', TCItems.STEEL_INGOT.get())
            .unlockedBy("has_steel", has(TCItems.STEEL_INGOT.get()))
            .save(consumer, name(TCItems.WRENCH.get()));
            
        // Canister
        ShapedRecipeBuilder.shaped(TCItems.CANISTER.get())
            .pattern("SSS")
            .pattern("L L")
            .pattern("LLL")
            .define('S', Items.STICK)
            .define('L', Items.LEATHER)
            .unlockedBy("has_leather", has(Items.LEATHER))
            .save(consumer, name(TCItems.CANISTER.get()));
    }
    
    private void generateTrainWorkbenchRecipes(RecipeOutput consumer) {
        // Train workbench recipes would typically be crafting recipes for train parts
        // For now we'll add a placeholder for the workbench itself
        ShapedRecipeBuilder.shaped(TCBlocks.TRAIN_WORKBENCH.get())
            .pattern("SSS")
            .pattern("P P")
            .pattern("PPP")
            .define('S', Items.STICK)
            .define('P', ItemTags.PLANKS)
            .unlockedBy("has_planks", has(ItemTags.PLANKS))
            .save(consumer, name(TCBlocks.TRAIN_WORKBENCH.get()));
    }
    
    private void generateAssemblyTableRecipes(RecipeOutput consumer) {
        // Assembly Table Tier I
        ShapedRecipeBuilder.shaped(TCBlocks.ASSEMBLY_TABLE_I.get())
            .pattern("III")
            .pattern("PBP")
            .pattern("PPP")
            .define('I', Items.IRON_BLOCK)
            .define('P', ItemTags.PLANKS)
            .define('B', Items.IRON_BARS)
            .unlockedBy("has_iron", has(Items.IRON_BLOCK))
            .save(consumer, name(TCBlocks.ASSEMBLY_TABLE_I.get()));
            
        // Assembly Table Tier II
        ShapedRecipeBuilder.shaped(TCBlocks.ASSEMBLY_TABLE_II.get())
            .pattern("III")
            .pattern("PBP")
            .pattern("PPP")
            .define('I', Items.GOLD_BLOCK)
            .define('P', Items.IRON_BLOCK)
            .define('B', TCBlocks.ASSEMBLY_TABLE_I.get())
            .unlockedBy("has_assembly_i", has(TCBlocks.ASSEMBLY_TABLE_I.get()))
            .save(consumer, name(TCBlocks.ASSEMBLY_TABLE_II.get()));
            
        // Assembly Table Tier III
        ShapedRecipeBuilder.shaped(TCBlocks.ASSEMBLY_TABLE_III.get())
            .pattern("DDD")
            .pattern("PBP")
            .pattern("PPP")
            .define('D', Items.DIAMOND_BLOCK)
            .define('P', Items.NETHERITE_BLOCK)
            .define('B', TCBlocks.ASSEMBLY_TABLE_II.get())
            .unlockedBy("has_assembly_ii", has(TCBlocks.ASSEMBLY_TABLE_II.get()))
            .save(consumer, name(TCBlocks.ASSEMBLY_TABLE_III.get()));
            
        // Sample assembly table recipes for train components
        // Steel Frame
        List<NumberedIngredient> frameIngredients = new ArrayList<>();
        frameIngredients.add(new NumberedIngredient(Ingredient.of(Items.IRON_INGOT), 8));
        frameIngredients.add(new NumberedIngredient(Ingredient.of(TCItems.STEEL_INGOT.get()), 4));
        saveAssemblyRecipe(consumer, "steel_frame", 1, TCItems.STEEL_FRAME.get(), frameIngredients);
        
        // Steel Bogie
        List<NumberedIngredient> bogieIngredients = new ArrayList<>();
        bogieIngredients.add(new NumberedIngredient(Ingredient.of(Items.IRON_INGOT), 6));
        bogieIngredients.add(new NumberedIngredient(Ingredient.of(TCItems.STEEL_INGOT.get()), 2));
        bogieIngredients.add(new NumberedIngredient(Ingredient.of(Items.REDSTONE), 1));
        saveAssemblyRecipe(consumer, "steel_bogie", 1, TCItems.STEEL_BOGIE.get(), bogieIngredients);
        
        // Steel Firebox
        List<NumberedIngredient> fireboxIngredients = new ArrayList<>();
        fireboxIngredients.add(new NumberedIngredient(Ingredient.of(Items.IRON_INGOT), 5));
        fireboxIngredients.add(new NumberedIngredient(Ingredient.of(TCItems.STEEL_INGOT.get()), 3));
        fireboxIngredients.add(new NumberedIngredient(Ingredient.of(Items.FURNACE), 1));
        saveAssemblyRecipe(consumer, "steel_firebox", 1, TCItems.STEEL_FIREBOX.get(), fireboxIngredients);
        
        // Steel Cabin
        List<NumberedIngredient> cabinIngredients = new ArrayList<>();
        fireboxIngredients.add(new NumberedIngredient(Ingredient.of(Items.IRON_INGOT), 4));
        fireboxIngredients.add(new NumberedIngredient(Ingredient.of(TCItems.STEEL_INGOT.get()), 4));
        fireboxIngredients.add(new NumberedIngredient(Ingredient.of(Items.GLASS_PANE), 2));
        saveAssemblyRecipe(consumer, "steel_cabin", 1, TCItems.STEEL_CABIN.get(), cabinIngredients);
        
        // Steel Chimney
        List<NumberedIngredient> chimneyIngredients = new ArrayList<>();
        chimneyIngredients.add(new NumberedIngredient(Ingredient.of(Items.IRON_INGOT), 6));
        chimneyIngredients.add(new NumberedIngredient(Ingredient.of(TCItems.STEEL_INGOT.get()), 2));
        saveAssemblyRecipe(consumer, "steel_chimney", 1, TCItems.STEEL_CHIMNEY.get(), chimneyIngredients);
    }
    
    private void generateDistilleryRecipes(RecipeOutput consumer) {
        // Distillery machine recipe
        ShapedRecipeBuilder.shaped(TCBlocks.DISTILLERY.get())
            .pattern("III")
            .pattern("FBF")
            .pattern("III")
            .define('I', Items.IRON_BLOCK)
            .define('F', Items.FURNACE)
            .define('B', Items.BUCKET)
            .unlockedBy("has_furnace", has(Items.FURNACE))
            .save(consumer, name(TCBlocks.DISTILLERY.get()));
            
        // Distillery recipes for processing materials
        // Coal Dust to Graphite
        saveDistilleryRecipe(consumer, "distillery_graphite", 
            Ingredient.of(TCItems.COAL_DUST.get()), 1, 
            new ItemStack(TCItems.GRAPHITE.get()), null, 200);
            
        // Coal to Coal Dust
        saveDistilleryRecipe(consumer, "distillery_coal_dust", 
            Ingredient.of(Items.COAL), 1, 
            new ItemStack(TCItems.COAL_DUST.get()), null, 200);
    }
    
    // Helper methods for saving custom recipes
    private void saveAssemblyRecipe(RecipeOutput consumer, String name, int tier, Item result, List<NumberedIngredient> ingredients) {
        // Create the JSON structure for the assembly table recipe
        JsonObject json = new JsonObject();
        json.addProperty("type", "traincraft:assembly_table");
        json.addProperty("tier", tier);
        
        JsonObject resultObj = new JsonObject();
        resultObj.addProperty("id", Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(result)).toString());
        resultObj.addProperty("count", 1);
        json.add("result", resultObj);
        
        // Add ingredients array manually since we can't use gson
        com.google.gson.JsonArray ingredientsArray = new com.google.gson.JsonArray();
        for (NumberedIngredient ingredient : ingredients) {
            ingredientsArray.add(ingredient.toJsonObject());
        }
        json.add("ingredients", ingredientsArray);
        
        consumer.accept(new ResourceLocation(Traincraft.MOD_ID, name), json, null);
    }
    
    private void saveDistilleryRecipe(RecipeOutput consumer, String name, Ingredient input, int inputAmount, ItemStack output, @Nullable ItemStack fluidOutput, int burnTime) {
        // Create the JSON structure for the distillery recipe
        JsonObject json = new JsonObject();
        json.addProperty("type", "traincraft:distillery");
        
        // Add input
        json.add("input", input.toJson());
        json.addProperty("input_amount", inputAmount);
        
        // Add output
        JsonObject outputObj = new JsonObject();
        outputObj.addProperty("id", Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(output.getItem())).toString());
        outputObj.addProperty("count", output.getCount());
        json.add("output", outputObj);
        
        // Add fluid output if present
        if (fluidOutput != null) {
            JsonObject fluidObj = new JsonObject();
            // Note: This is a simplified approach - in reality you'd need to get the fluid from the item
            fluidObj.addProperty("id", "minecraft:water"); // Placeholder
            fluidObj.addProperty("amount", fluidOutput.getCount());
            json.add("fluid_output", fluidObj);
        }
        
        json.addProperty("burn_time", burnTime);
        
        consumer.accept(new ResourceLocation(Traincraft.MOD_ID, name), json, null);
    }
    
    private String name(ItemLike item) {
        return Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(item.asItem())).getPath();
    }
    
    private String name(ItemLike item, String suffix) {
        return Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(item.asItem())).getPath() + "_" + suffix;
    }
}
