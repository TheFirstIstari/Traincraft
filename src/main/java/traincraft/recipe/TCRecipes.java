/*
 * Traincraft
 * Copyright (c) 2011-2024.
 */

package traincraft.recipe;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredHolder;
import traincraft.Traincraft;

public class TCRecipes {

    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create(Registries.RECIPE_SERIALIZER, Traincraft.MOD_ID);
    public static final DeferredRegister<RecipeType<?>> RECIPE_TYPES = DeferredRegister.create(Registries.RECIPE_TYPE, Traincraft.MOD_ID);

    public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<DistilleryRecipe>> DISTILLERY_SERIALIZER = RECIPE_SERIALIZERS.register("distillery",
        () -> DistilleryRecipe.Serializer.INSTANCE);

    public static final DeferredHolder<RecipeType<?>, RecipeType<DistilleryRecipe>> DISTILLERY_TYPE = RECIPE_TYPES.register("distillery",
        () -> DistilleryRecipe.Type.INSTANCE);

    public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<AssemblyTableRecipe>> ASSEMBLY_TABLE_SERIALIZER = RECIPE_SERIALIZERS.register("assembly_table",
        () -> AssemblyTableRecipe.Serializer.INSTANCE);

    public static final DeferredHolder<RecipeType<?>, RecipeType<AssemblyTableRecipe>> ASSEMBLY_TABLE_TYPE = RECIPE_TYPES.register("assembly_table",
        () -> AssemblyTableRecipe.Type.INSTANCE);

    public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<TrainWorkbenchRecipe>> TRAIN_WORKBENCH_SERIALIZER = RECIPE_SERIALIZERS.register("train_workbench",
        () -> TrainWorkbenchRecipe.Serializer.INSTANCE);

    public static final DeferredHolder<RecipeType<?>, RecipeType<TrainWorkbenchRecipe>> TRAIN_WORKBENCH_TYPE = RECIPE_TYPES.register("train_workbench",
        () -> TrainWorkbenchRecipe.Type.INSTANCE);
}
