/*
 * Traincraft
 * Copyright (c) 2011-2024.
 */

package traincraft.items;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.minecraft.world.item.Item;
import traincraft.Traincraft;

import java.util.function.Supplier;

public class TCItems {

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(
            net.minecraft.core.registries.Registries.ITEM, Traincraft.MOD_ID
    );

    public static final Supplier<Item> COPPER_INGOT = ITEMS.register("copper_ingot", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> STEEL_INGOT = ITEMS.register("steel_ingot", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> STEEL_DUST = ITEMS.register("steel_dust", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> COAL_DUST = ITEMS.register("coal_dust", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> GRAPHITE = ITEMS.register("graphite", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> PLASTIC = ITEMS.register("plastic", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> STEEL_FIREBOX = ITEMS.register("steel_firebox", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> STEEL_BOGIE = ITEMS.register("steel_bogie", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> STEEL_FRAME = ITEMS.register("steel_frame", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> IRON_PLATE = ITEMS.register("iron_plate", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> STEEL_PLATE = ITEMS.register("steel_plate", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> COPPER_PLATE = ITEMS.register("copper_plate", () -> new Item(new Item.Properties()));

    public static void register(IEventBus bus) {
        ITEMS.register(bus);
    }
}