/*
 * Traincraft
 * Copyright (c) 2011-2024.
 */

package traincraft.world;

import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.TagMatchTest;
import traincraft.Traincraft;
import traincraft.blocks.TCBlocks;

import java.util.List;

public class TCConfiguredFeatures {

    public static final ResourceKey<ConfiguredFeature<?, ?>> COPPER_ORE_KEY = createKey("copper_ore");
    public static final ResourceKey<ConfiguredFeature<?, ?>> PETROL_ORE_KEY = createKey("petrol_ore");
    public static final ResourceKey<ConfiguredFeature<?, ?>> OIL_SAND_KEY = createKey("oil_sand");

    private static ResourceKey<ConfiguredFeature<?, ?>> createKey(String name) {
        return ResourceKey.create(Registries.CONFIGURED_FEATURE, ResourceLocation.fromNamespaceAndPath(Traincraft.MOD_ID, name));
    }

    public static void bootstrap(BootstrapContext<ConfiguredFeature<?, ?>> context) {
        RuleTest stoneReplaceables = new TagMatchTest(BlockTags.STONE_ORE_REPLACEABLES);
        RuleTest sandReplaceables = new TagMatchTest(BlockTags.SAND);

        List<OreConfiguration.TargetBlockState> copperTargets = List.of(
            OreConfiguration.target(stoneReplaceables, TCBlocks.COPPER_ORE.get().defaultBlockState())
        );

        List<OreConfiguration.TargetBlockState> petrolTargets = List.of(
            OreConfiguration.target(stoneReplaceables, TCBlocks.PETROL_ORE.get().defaultBlockState())
        );

        List<OreConfiguration.TargetBlockState> oilSandTargets = List.of(
            OreConfiguration.target(sandReplaceables, TCBlocks.OIL_SAND.get().defaultBlockState())
        );

        context.register(COPPER_ORE_KEY, new ConfiguredFeature<>(Feature.ORE, new OreConfiguration(copperTargets, 6)));
        context.register(PETROL_ORE_KEY, new ConfiguredFeature<>(Feature.ORE, new OreConfiguration(petrolTargets, 14)));
        context.register(OIL_SAND_KEY, new ConfiguredFeature<>(Feature.ORE, new OreConfiguration(oilSandTargets, 10)));
    }
}
