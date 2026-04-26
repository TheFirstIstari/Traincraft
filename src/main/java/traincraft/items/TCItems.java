/*
 * Traincraft
 * Copyright (c) 2011-2024.
 */

package traincraft.items;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import traincraft.Traincraft;
import traincraft.blocks.TCBlocks;
import traincraft.entity.TCEntities;

import java.util.function.Supplier;
import traincraft.items.ItemGuide;

public class TCItems {

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.createItems(Traincraft.MOD_ID);

    private static DeferredHolder<Item, Item> registerBlockItem(DeferredHolder<Block, ? extends Block> blockHolder) {
        return ITEMS.register(blockHolder.getId().getPath(), () -> new BlockItem(blockHolder.get(), new Item.Properties()));
    }

    private static DeferredHolder<Item, Item> registerSimpleItem(String name) {
        return ITEMS.register(name, () -> new Item(new Item.Properties()));
    }

    public static final DeferredHolder<Item, Item> OIL_SAND = registerBlockItem(TCBlocks.OIL_SAND);
    public static final DeferredHolder<Item, Item> PETROL_ORE = registerBlockItem(TCBlocks.PETROL_ORE);
    public static final DeferredHolder<Item, Item> COPPER_ORE = registerBlockItem(TCBlocks.COPPER_ORE);
    public static final DeferredHolder<Item, Item> BALLAST = registerBlockItem(TCBlocks.BALLAST);
    public static final DeferredHolder<Item, Item> LANTERN = registerBlockItem(TCBlocks.LANTERN);
    public static final DeferredHolder<Item, Item> BRIDGE_PILLAR = registerBlockItem(TCBlocks.BRIDGE_PILLAR);
    public static final DeferredHolder<Item, Item> STOPPER = registerBlockItem(TCBlocks.STOPPER);
    public static final DeferredHolder<Item, Item> DISTILLERY = registerBlockItem(TCBlocks.DISTILLERY);
    public static final DeferredHolder<Item, Item> ASSEMBLY_TABLE_I = registerBlockItem(TCBlocks.ASSEMBLY_TABLE_I);
    public static final DeferredHolder<Item, Item> ASSEMBLY_TABLE_II = registerBlockItem(TCBlocks.ASSEMBLY_TABLE_II);
    public static final DeferredHolder<Item, Item> ASSEMBLY_TABLE_III = registerBlockItem(TCBlocks.ASSEMBLY_TABLE_III);
    public static final DeferredHolder<Item, Item> TRAIN_WORKBENCH = registerBlockItem(TCBlocks.TRAIN_WORKBENCH);
    public static final DeferredHolder<Item, Item> OPEN_HEARTH_FURNACE = registerBlockItem(TCBlocks.OPEN_HEARTH_FURNACE);
    public static final DeferredHolder<Item, Item> WATER_WHEEL = registerBlockItem(TCBlocks.WATER_WHEEL);
    public static final DeferredHolder<Item, Item> WIND_MILL = registerBlockItem(TCBlocks.WIND_MILL);
    public static final DeferredHolder<Item, Item> GENERATOR_DIESEL = registerBlockItem(TCBlocks.GENERATOR_DIESEL);
    public static final DeferredHolder<Item, Item> BATTERY = registerBlockItem(TCBlocks.BATTERY);
    public static final DeferredHolder<Item, Item> SIGNAL = registerBlockItem(TCBlocks.SIGNAL);
    public static final DeferredHolder<Item, Item> SWITCH_STAND = registerBlockItem(TCBlocks.SWITCH_STAND);

    public static final DeferredHolder<Item, Item> STEEL_INGOT = registerSimpleItem("steel_ingot");
    public static final DeferredHolder<Item, Item> STEEL_DUST = registerSimpleItem("steel_dust");
    public static final DeferredHolder<Item, Item> COAL_DUST = registerSimpleItem("coal_dust");
    public static final DeferredHolder<Item, Item> GRAPHITE = registerSimpleItem("graphite");
    public static final DeferredHolder<Item, Item> STEEL_FIREBOX = registerSimpleItem("steel_firebox");
    public static final DeferredHolder<Item, Item> STEEL_BOGIE = registerSimpleItem("steel_bogie");
    public static final DeferredHolder<Item, Item> STEEL_FRAME = registerSimpleItem("steel_frame");
    public static final DeferredHolder<Item, Item> STEEL_CABIN = registerSimpleItem("steel_cabin");
    public static final DeferredHolder<Item, Item> STEEL_CHIMNEY = registerSimpleItem("steel_chimney");
    public static final DeferredHolder<Item, Item> PLASTIC = registerSimpleItem("plastic");
    public static final DeferredHolder<Item, Item> COPPER_INGOT = registerSimpleItem("copper_ingot");

    public static final DeferredHolder<Item, Item> BALLOON = registerSimpleItem("balloon");
    public static final DeferredHolder<Item, Item> BOGIE_IRON = registerSimpleItem("bogie_iron");
    public static final DeferredHolder<Item, Item> BOGIE_WOOD = registerSimpleItem("bogie_wood");
    public static final DeferredHolder<Item, Item> BOILER_IRON = registerSimpleItem("boiler_iron");
    public static final DeferredHolder<Item, Item> BOILER_STEEL = registerSimpleItem("boiler_steel");
    public static final DeferredHolder<Item, Item> CAB_IRON = registerSimpleItem("cab_iron");
    public static final DeferredHolder<Item, Item> CAB_WOOD = registerSimpleItem("cab_wood");
    public static final DeferredHolder<Item, Item> CAMSHAFT = registerSimpleItem("camshaft");
    public static final DeferredHolder<Item, Item> CHIMNEY_IRON = registerSimpleItem("chimney_iron");
    public static final DeferredHolder<Item, Item> CIRCUIT = registerSimpleItem("circuit");
    public static final DeferredHolder<Item, Item> CONTROLS = registerSimpleItem("controls");
    public static final DeferredHolder<Item, Item> CYLINDER = registerSimpleItem("cylinder");
    public static final DeferredHolder<Item, Item> ENGINE_DIESEL = registerSimpleItem("engine_diesel");
    public static final DeferredHolder<Item, Item> ENGINE_ELECTRIC = registerSimpleItem("engine_electric");
    public static final DeferredHolder<Item, Item> ENGINE_STEAM = registerSimpleItem("engine_steam");
    public static final DeferredHolder<Item, Item> FIBERGLASS_PLATE = registerSimpleItem("fiberglass_plate");
    public static final DeferredHolder<Item, Item> FIREBOX_IRON = registerSimpleItem("firebox_iron");
    public static final DeferredHolder<Item, Item> FRAME_IRON = registerSimpleItem("frame_iron");
    public static final DeferredHolder<Item, Item> FRAME_WOOD = registerSimpleItem("frame_wood");
    public static final DeferredHolder<Item, Item> PISTON = registerSimpleItem("piston");
    public static final DeferredHolder<Item, Item> PROPELLER = registerSimpleItem("propeller");
    public static final DeferredHolder<Item, Item> RAIL_COPPER = registerSimpleItem("rail_copper");
    public static final DeferredHolder<Item, Item> RAIL_STEEL = registerSimpleItem("rail_steel");
    public static final DeferredHolder<Item, Item> REINFORCED_PLATE = registerSimpleItem("reinforced_plate");
    public static final DeferredHolder<Item, Item> SEATS = registerSimpleItem("seats");
    public static final DeferredHolder<Item, Item> SIGNAL_PART = registerSimpleItem("signal_part");
    public static final DeferredHolder<Item, Item> TRANSFORMER = registerSimpleItem("transformer");
    public static final DeferredHolder<Item, Item> TRANSMISSION = registerSimpleItem("transmission");
    public static final DeferredHolder<Item, Item> FINE_COPPER_WIRE = registerSimpleItem("fine_copper_wire");

    public static final DeferredHolder<Item, Item> WRENCH = ITEMS.register("wrench",
        () -> new ItemWrench());
    public static final DeferredHolder<Item, Item> CANISTER = ITEMS.register("canister",
        () -> new ItemCanister());
    public static final DeferredHolder<Item, Item> CONNECTOR = ITEMS.register("connector",
        () -> new ItemConnector());
    public static final DeferredHolder<Item, Item> SKIN_CHANGER = ITEMS.register("skin_changer",
        () -> new ItemSkinChanger());
    public static final DeferredHolder<Item, Item> GUIDE = ITEMS.register("guide",
        () -> new ItemGuide());
    public static final DeferredHolder<Item, Item> CHUNK_LOADER_ACTIVATOR = ITEMS.register("chunk_loader_activator",
        () -> new ItemChunkLoaderActivator());
    public static final DeferredHolder<Item, Item> FUEL = registerSimpleItem("fuel");
    public static final DeferredHolder<Item, Item> ATO_CARD = ITEMS.register("ato_card",
        () -> new ItemATOCard());
    public static final DeferredHolder<Item, Item> WIRELESS_TRANSMITTER = ITEMS.register("wireless_transmitter",
        () -> new ItemWirelessTransmitter());

    public static final DeferredHolder<Item, Item> LOCOMOTIVE_STEAM_SMALL_SPAWN_EGG = ITEMS.register("locomotive_steam_small_spawn_egg",
        () -> new RollingStockSpawnEggItem(
            TCEntities.LOCOMOTIVE_STEAM_SMALL, new Item.Properties()));
    public static final DeferredHolder<Item, Item> FREIGHT_CART_SPAWN_EGG = ITEMS.register("freight_cart_spawn_egg",
        () -> new RollingStockSpawnEggItem(
            TCEntities.FREIGHT_CART, new Item.Properties()));
    public static final DeferredHolder<Item, Item> PASSENGER_CART_SPAWN_EGG = ITEMS.register("passenger_cart_spawn_egg",
        () -> new RollingStockSpawnEggItem(
            TCEntities.PASSENGER_CART, new Item.Properties()));
}
