/*
 * Traincraft
 * Copyright (c) 2011-2024.
 */

package traincraft.world;

import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.placement.BiomeFilter;
import net.minecraft.world.level.levelgen.placement.CountPlacement;
import net.minecraft.world.level.levelgen.placement.HeightRangePlacement;
import net.minecraft.world.level.levelgen.placement.InSquarePlacement;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import traincraft.Traincraft;

import java.util.List;

public class TCPlacedFeatures {

    public static final ResourceKey<PlacedFeature> COPPER_ORE_PLACED_KEY = createKey("copper_ore");
    public static final ResourceKey<PlacedFeature> PETROL_ORE_PLACED_KEY = createKey("petrol_ore");
    public static final ResourceKey<PlacedFeature> OIL_SAND_PLACED_KEY = createKey("oil_sand");

    private static ResourceKey<PlacedFeature> createKey(String name) {
        return ResourceKey.create(Registries.PLACED_FEATURE, ResourceLocation.fromNamespaceAndPath(Traincraft.MOD_ID, name));
    }

    public static void bootstrap(BootstrapContext<PlacedFeature> context) {
        var configuredFeatures = context.lookup(Registries.CONFIGURED_FEATURE);

        List<net.minecraft.world.level.levelgen.placement.PlacementModifier> copperPlacements = orePlacement(4, 5, 50);
        List<net.minecraft.world.level.levelgen.placement.PlacementModifier> petrolPlacements = orePlacement(3, 10, 50);
        List<net.minecraft.world.level.levelgen.placement.PlacementModifier> oilSandPlacements = orePlacement(2, 25, 75);

        PlacementUtils.register(context, COPPER_ORE_PLACED_KEY,
            configuredFeatures.getOrThrow(TCConfiguredFeatures.COPPER_ORE_KEY),
            copperPlacements);

        PlacementUtils.register(context, PETROL_ORE_PLACED_KEY,
            configuredFeatures.getOrThrow(TCConfiguredFeatures.PETROL_ORE_KEY),
            petrolPlacements);

        PlacementUtils.register(context, OIL_SAND_PLACED_KEY,
            configuredFeatures.getOrThrow(TCConfiguredFeatures.OIL_SAND_KEY),
            oilSandPlacements);
    }

    private static List<net.minecraft.world.level.levelgen.placement.PlacementModifier> orePlacement(int count, int minY, int maxY) {
        return List.of(
            CountPlacement.of(count),
            InSquarePlacement.spread(),
            HeightRangePlacement.triangle(VerticalAnchor.absolute(minY), VerticalAnchor.absolute(maxY)),
            BiomeFilter.biome()
        );
    }
}
