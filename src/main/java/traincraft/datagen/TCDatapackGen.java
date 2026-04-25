/*
 * Traincraft
 * Copyright (c) 2011-2024.
 */

package traincraft.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeOutput;
import net.neoforged.neoforge.common.data.DatapackBuiltinEntriesProvider;
import traincraft.Traincraft;
import traincraft.world.TCConfiguredFeatures;
import traincraft.world.TCPlacedFeatures;

import javax.annotation.Nullable;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class TCDatapackGen extends DatapackBuiltinEntriesProvider {

    public static final RegistrySetBuilder BUILDER = new RegistrySetBuilder()
        .add(Registries.CONFIGURED_FEATURE, TCConfiguredFeatures::bootstrap)
        .add(Registries.PLACED_FEATURE, TCPlacedFeatures::bootstrap);

    private final TCRecipeProvider recipeProvider;

    public TCDatapackGen(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries, BUILDER, Set.of(Traincraft.MOD_ID));
        this.recipeProvider = new TCRecipeProvider(output);
    }

    @Override
    public void run(HolderLookup.Provider registries) {
        super.run(registries);
        // Generate recipes after the registry data has been built
        recipeProvider.buildRecipes(new RecipeOutput() {
            @Override
            public void accept(net.minecraft.resources.ResourceLocation key, net.minecraft.world.item.crafting.Recipe<?> recipe, @Nullable net.minecraft.advancements.AdvancementHolder advancement) {
                TCDatapackGen.this.output.accept(key, recipe, advancement);
            }
        });
    }
}
