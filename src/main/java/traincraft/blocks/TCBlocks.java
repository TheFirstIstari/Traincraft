/*
 * Traincraft
 * Copyright (c) 2011-2024.
 */

package traincraft.blocks;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredHolder;
import traincraft.Traincraft;
import traincraft.blocks.assemblytable.BlockAssemblyTable;
import traincraft.blocks.distillery.BlockDistillery;
import traincraft.blocks.openhearthfurnace.BlockOpenHearthFurnace;
import traincraft.blocks.trainworkbench.BlockTrainWorkbench;

public class TCBlocks {

    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.createBlocks(Traincraft.MOD_ID);

    public static final DeferredHolder<Block, Block> OIL_SAND = BLOCKS.register("oil_sand",
        () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.SAND)));

    public static final DeferredHolder<Block, Block> PETROL_ORE = BLOCKS.register("petrol_ore",
        () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.STONE).strength(3.0f, 3.0f)));

    public static final DeferredHolder<Block, Block> COPPER_ORE = BLOCKS.register("copper_ore",
        () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.STONE).strength(3.0f, 3.0f)));

    public static final DeferredHolder<Block, Block> BALLAST = BLOCKS.register("ballast",
        () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.GRAVEL)));

    public static final DeferredHolder<Block, Block> LANTERN = BLOCKS.register("lantern",
        () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.LANTERN)));

    public static final DeferredHolder<Block, Block> BRIDGE_PILLAR = BLOCKS.register("bridge_pillar",
        () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.STONE_BRICKS).strength(2.0f, 6.0f)));

    public static final DeferredHolder<Block, Block> STOPPER = BLOCKS.register("stopper",
        () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK).strength(2.0f, 6.0f)));

    public static final DeferredHolder<Block, BlockDistillery> DISTILLERY = BLOCKS.register("distillery",
        () -> new BlockDistillery(BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK).strength(3.0f, 6.0f).noOcclusion()));

    public static final DeferredHolder<Block, BlockAssemblyTable> ASSEMBLY_TABLE_I = BLOCKS.register("assembly_table_i",
        () -> new BlockAssemblyTable(BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK).strength(3.0f, 6.0f).noOcclusion(), 1));

    public static final DeferredHolder<Block, BlockAssemblyTable> ASSEMBLY_TABLE_II = BLOCKS.register("assembly_table_ii",
        () -> new BlockAssemblyTable(BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK).strength(3.0f, 6.0f).noOcclusion(), 2));

    public static final DeferredHolder<Block, BlockAssemblyTable> ASSEMBLY_TABLE_III = BLOCKS.register("assembly_table_iii",
        () -> new BlockAssemblyTable(BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK).strength(3.0f, 6.0f).noOcclusion(), 3));

    public static final DeferredHolder<Block, BlockTrainWorkbench> TRAIN_WORKBENCH = BLOCKS.register("train_workbench",
        () -> new BlockTrainWorkbench(BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK).strength(3.0f, 6.0f).noOcclusion()));

    public static final DeferredHolder<Block, BlockOpenHearthFurnace> OPEN_HEARTH_FURNACE = BLOCKS.register("open_hearth_furnace",
        () -> new BlockOpenHearthFurnace(BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK).strength(3.0f, 6.0f).noOcclusion().lightLevel(s -> s.getValue(BlockOpenHearthFurnace.ACTIVE) ? 12 : 0)));

    public static final DeferredHolder<Block, Block> WATER_WHEEL = BLOCKS.register("water_wheel",
        () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_PLANKS).strength(2.0f, 3.0f)));

    public static final DeferredHolder<Block, Block> WIND_MILL = BLOCKS.register("wind_mill",
        () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_PLANKS).strength(2.0f, 3.0f)));

    public static final DeferredHolder<Block, Block> GENERATOR_DIESEL = BLOCKS.register("generator_diesel",
        () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK).strength(3.0f, 6.0f)));

    public static final DeferredHolder<Block, Block> BATTERY = BLOCKS.register("battery",
        () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK).strength(3.0f, 6.0f)));
}
