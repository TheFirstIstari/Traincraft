/*
 * Traincraft
 * Copyright (c) 2011-2024.
 */

package traincraft.items.armor;

import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredHolder;
import traincraft.Traincraft;

public class TCArmor {

    public static final DeferredRegister<Item> ARMOR_ITEMS = DeferredRegister.createItems(Traincraft.MOD_ID);

    public static final DeferredHolder<Item, Item> OVERALLS = ARMOR_ITEMS.register("overalls",
        () -> new ArmorItem(TCArmorMaterials.GENERAL, ArmorItem.Type.LEGGINGS, new Item.Properties()));

    public static final DeferredHolder<Item, Item> JACKET = ARMOR_ITEMS.register("jacket",
        () -> new ArmorItem(TCArmorMaterials.GENERAL, ArmorItem.Type.CHESTPLATE, new Item.Properties()));

    public static final DeferredHolder<Item, Item> HAT = ARMOR_ITEMS.register("hat",
        () -> new ArmorItem(TCArmorMaterials.GENERAL, ArmorItem.Type.HELMET, new Item.Properties()));

    public static final DeferredHolder<Item, Item> TICKETMAN_JACKET = ARMOR_ITEMS.register("ticketman_jacket",
        () -> new ArmorItem(TCArmorMaterials.TICKETMAN, ArmorItem.Type.CHESTPLATE, new Item.Properties()));

    public static final DeferredHolder<Item, Item> TICKETMAN_PANTS = ARMOR_ITEMS.register("ticketman_pants",
        () -> new ArmorItem(TCArmorMaterials.TICKETMAN, ArmorItem.Type.LEGGINGS, new Item.Properties()));

    public static final DeferredHolder<Item, Item> TICKETMAN_HAT = ARMOR_ITEMS.register("ticketman_hat",
        () -> new ArmorItem(TCArmorMaterials.TICKETMAN, ArmorItem.Type.HELMET, new Item.Properties()));

    public static final DeferredHolder<Item, Item> DRIVER_JACKET = ARMOR_ITEMS.register("driver_jacket",
        () -> new ArmorItem(TCArmorMaterials.DRIVER, ArmorItem.Type.CHESTPLATE, new Item.Properties()));

    public static final DeferredHolder<Item, Item> DRIVER_PANTS = ARMOR_ITEMS.register("driver_pants",
        () -> new ArmorItem(TCArmorMaterials.DRIVER, ArmorItem.Type.LEGGINGS, new Item.Properties()));

    public static final DeferredHolder<Item, Item> DRIVER_HAT = ARMOR_ITEMS.register("driver_hat",
        () -> new ArmorItem(TCArmorMaterials.DRIVER, ArmorItem.Type.HELMET, new Item.Properties()));

    public static final DeferredHolder<Item, Item> COMPOSITE_SUIT_HEAD = ARMOR_ITEMS.register("composite_suit_head",
        () -> new ArmorItem(TCArmorMaterials.COMPOSITE, ArmorItem.Type.HELMET, new Item.Properties()));

    public static final DeferredHolder<Item, Item> COMPOSITE_SUIT_CHEST = ARMOR_ITEMS.register("composite_suit_chest",
        () -> new ArmorItem(TCArmorMaterials.COMPOSITE, ArmorItem.Type.CHESTPLATE, new Item.Properties()));

    public static final DeferredHolder<Item, Item> COMPOSITE_SUIT_PANTS = ARMOR_ITEMS.register("composite_suit_pants",
        () -> new ArmorItem(TCArmorMaterials.COMPOSITE, ArmorItem.Type.LEGGINGS, new Item.Properties()));

    public static final DeferredHolder<Item, Item> COMPOSITE_SUIT_FEET = ARMOR_ITEMS.register("composite_suit_feet",
        () -> new ArmorItem(TCArmorMaterials.COMPOSITE, ArmorItem.Type.BOOTS, new Item.Properties()));
}
