/*
 * Traincraft
 * Copyright (c) 2011-2024.
 */

package traincraft.tile;

import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredHolder;
import traincraft.Traincraft;
import traincraft.blocks.TCBlocks;
import traincraft.blocks.assemblytable.TileAssemblyTable;
import traincraft.blocks.distillery.TileDistillery;
import traincraft.blocks.trainworkbench.TileTrainWorkbench;
import traincraft.tile.TileSignal;
import traincraft.tile.TileSwitchStand;
import traincraft.blocks.openhearthfurnace.TileOpenHearthFurnace;

import java.util.function.Supplier;

public class TCTiles {

    public static final DeferredRegister<BlockEntityType<?>> TILES = DeferredRegister.create(
        net.minecraft.core.registries.Registries.BLOCK_ENTITY_TYPE, Traincraft.MOD_ID);

    private static <T extends BlockEntityType<?>> DeferredHolder<BlockEntityType<?>, T> register(String name, Supplier<T> builder) {
        return TILES.register(name, builder);
    }

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<TileDistillery>> DISTILLERY = register("distillery",
        () -> BlockEntityType.Builder.of(TileDistillery::new, TCBlocks.DISTILLERY.get()).build(null));

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<TileAssemblyTable>> ASSEMBLY_TABLE_I = register("assembly_table_i",
        () -> BlockEntityType.Builder.of(TileAssemblyTable::new, TCBlocks.ASSEMBLY_TABLE_I.get()).build(null));

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<TileAssemblyTable>> ASSEMBLY_TABLE_II = register("assembly_table_ii",
        () -> BlockEntityType.Builder.of(TileAssemblyTable::new, TCBlocks.ASSEMBLY_TABLE_II.get()).build(null));

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<TileAssemblyTable>> ASSEMBLY_TABLE_III = register("assembly_table_iii",
        () -> BlockEntityType.Builder.of(TileAssemblyTable::new, TCBlocks.ASSEMBLY_TABLE_III.get()).build(null));

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<TileTrainWorkbench>> TRAIN_WORKBENCH = TILES.register("train_workbench",
        () -> {
            var block = TCBlocks.TRAIN_WORKBENCH.get();
            return BlockEntityType.Builder.of(TileTrainWorkbench::new, block).build(null);
        });

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<TileOpenHearthFurnace>> OPEN_HEARTH_FURNACE = TILES.register("open_hearth_furnace",
        () -> {
            var block = TCBlocks.OPEN_HEARTH_FURNACE.get();
            return BlockEntityType.Builder.of(TileOpenHearthFurnace::new, block).build(null);
        });

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<TileWaterWheel>> WATER_WHEEL = TILES.register("water_wheel",
        () -> BlockEntityType.Builder.of(TileWaterWheel::new, TCBlocks.WATER_WHEEL.get()).build(null));

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<TileWindMill>> WIND_MILL = TILES.register("wind_mill",
        () -> BlockEntityType.Builder.of(TileWindMill::new, TCBlocks.WIND_MILL.get()).build(null));

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<TileDieselGenerator>> GENERATOR_DIESEL = TILES.register("generator_diesel",
        () -> BlockEntityType.Builder.of(TileDieselGenerator::new, TCBlocks.GENERATOR_DIESEL.get()).build(null));

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<TileBattery>> BATTERY = TILES.register("battery",
        () -> BlockEntityType.Builder.of(TileBattery::new, TCBlocks.BATTERY.get()).build(null));
        
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<TileSignal>> SIGNAL = TILES.register("signal",
        () -> BlockEntityType.Builder.of(TileSignal::new, TCBlocks.SIGNAL.get()).build(null));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<TileSwitchStand>> SWITCH_STAND = TILES.register("switch_stand",
        () -> BlockEntityType.Builder.of(TileSwitchStand::new, TCBlocks.SWITCH_STAND.get()).build(null));

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<traincraft.blocks.rail.TileTCCurvedRail>> CURVED_RAIL = TILES.register("curved_rail",
        () -> BlockEntityType.Builder.of(traincraft.blocks.rail.TileTCCurvedRail::new, TCBlocks.CURVED_RAIL.get()).build(null));
}