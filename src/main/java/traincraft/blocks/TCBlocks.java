/*
 * Traincraft
 * Copyright (c) 2011-2024.
 *
 * This file ("TCBlocks.java") is part of the Traincraft mod for Minecraft.
 * It is distributed under LGPL-v3.0.
 * You can find the source code at https://github.com/Traincraft/Traincraft
 */

package traincraft.blocks;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.minecraft.world.level.block.Block;
import traincraft.Traincraft;

import java.util.function.Supplier;

public class TCBlocks {

    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(
            net.minecraft.core.registries.Registries.BLOCK, Traincraft.MOD_ID
    );

    public static final Supplier<Block> DISTILLERY = BLOCKS.register("distillery", BlockDistil::new);
    public static final Supplier<Block> ASSEMBLY_TABLE_I = BLOCKS.register("assembly_table_i", BlockAssemblyTableI::new);
    public static final Supplier<Block> ASSEMBLY_TABLE_II = BLOCKS.register("assembly_table_ii", BlockAssemblyTableII::new);
    public static final Supplier<Block> ASSEMBLY_TABLE_III = BLOCKS.register("assembly_table_iii", BlockAssemblyTableIII::new);
    public static final Supplier<Block> TRAIN_WORKBENCH = BLOCKS.register("train_workbench", BlockTrainWorkbench::new);
    public static final Supplier<Block> OPEN_HEARTH_FURNACE = BLOCKS.register("open_hearth_furnace", BlockOpenHearthFurnace::new);
    public static final Supplier<Block> WATER_WHEEL = BLOCKS.register("water_wheel", BlockWaterWheel::new);
    public static final Supplier<Block> WIND_MILL = BLOCKS.register("wind_mill", BlockWindMill::new);
    public static final Supplier<Block> GENERATOR_DIESEL = BLOCKS.register("generator_diesel", BlockGeneratorDiesel::new);
    public static final Supplier<Block> BATTERY = BLOCKS.register("battery", BlockBattery::new);
    public static final Supplier<Block> STOPPER = BLOCKS.register("stopper", BlockStopper::new);
    public static final Supplier<Block> BRIDGE_PILLAR = BLOCKS.register("bridge_pillar", BlockBridgePillar::new);
    public static final Supplier<Block> LANTERN = BLOCKS.register("lantern", BlockLantern::new);
    public static final Supplier<Block> BALLAST = BLOCKS.register("ballast", BlockBallast::new);
    public static final Supplier<Block> OIL_SAND = BLOCKS.register("oil_sand", BlockOilSand::new);
    public static final Supplier<Block> PETROL_ORE = BLOCKS.register("petrol_ore", BlockPetrolOre::new);
    public static final Supplier<Block> COPPER_ORE = BLOCKS.register("copper_ore", BlockCopperOre::new);

    public static void register(IEventBus bus) {
        BLOCKS.register(bus);
    }
}