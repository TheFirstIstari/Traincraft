/*
 * Traincraft
 * Copyright (c) 2011-2024.
 */

package traincraft.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.DatapackBuiltinEntriesProvider;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import traincraft.Traincraft;
import traincraft.world.TCConfiguredFeatures;
import traincraft.world.TCPlacedFeatures;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class TCDatapackGen extends DatapackBuiltinEntriesProvider {

    public static final RegistrySetBuilder BUILDER = new RegistrySetBuilder()
        .add(Registries.CONFIGURED_FEATURE, TCConfiguredFeatures::bootstrap)
        .add(Registries.PLACED_FEATURE, TCPlacedFeatures::bootstrap);

    public TCDatapackGen(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries, BUILDER, Set.of(Traincraft.MOD_ID));
    }
}
