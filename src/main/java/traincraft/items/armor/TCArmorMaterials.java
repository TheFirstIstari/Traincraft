/*
 * Traincraft
 * Copyright (c) 2011-2024.
 */

package traincraft.items.armor;

import net.minecraft.Util;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.crafting.Ingredient;
import traincraft.Traincraft;
import traincraft.items.TCItems;

import java.util.EnumMap;
import java.util.List;
import java.util.function.Supplier;

public class TCArmorMaterials {

    public static final Holder<ArmorMaterial> GENERAL = register("general",
        Util.make(new EnumMap<>(ArmorItem.Type.class), map -> {
            map.put(ArmorItem.Type.BOOTS, 1);
            map.put(ArmorItem.Type.LEGGINGS, 4);
            map.put(ArmorItem.Type.CHESTPLATE, 5);
            map.put(ArmorItem.Type.HELMET, 2);
            map.put(ArmorItem.Type.BODY, 3);
        }),
        10,
        SoundEvents.ARMOR_EQUIP_LEATHER,
        0.0f,
        0.0f,
        () -> Ingredient.of(TCItems.STEEL_INGOT.get()));

    public static final Holder<ArmorMaterial> TICKETMAN = register("ticketman",
        Util.make(new EnumMap<>(ArmorItem.Type.class), map -> {
            map.put(ArmorItem.Type.BOOTS, 1);
            map.put(ArmorItem.Type.LEGGINGS, 4);
            map.put(ArmorItem.Type.CHESTPLATE, 5);
            map.put(ArmorItem.Type.HELMET, 2);
            map.put(ArmorItem.Type.BODY, 3);
        }),
        10,
        SoundEvents.ARMOR_EQUIP_LEATHER,
        0.0f,
        0.0f,
        () -> Ingredient.of(TCItems.STEEL_INGOT.get()));

    public static final Holder<ArmorMaterial> DRIVER = register("driver",
        Util.make(new EnumMap<>(ArmorItem.Type.class), map -> {
            map.put(ArmorItem.Type.BOOTS, 1);
            map.put(ArmorItem.Type.LEGGINGS, 4);
            map.put(ArmorItem.Type.CHESTPLATE, 5);
            map.put(ArmorItem.Type.HELMET, 2);
            map.put(ArmorItem.Type.BODY, 3);
        }),
        10,
        SoundEvents.ARMOR_EQUIP_LEATHER,
        0.0f,
        0.0f,
        () -> Ingredient.of(TCItems.STEEL_INGOT.get()));

    public static final Holder<ArmorMaterial> COMPOSITE = register("composite",
        Util.make(new EnumMap<>(ArmorItem.Type.class), map -> {
            map.put(ArmorItem.Type.BOOTS, 3);
            map.put(ArmorItem.Type.LEGGINGS, 6);
            map.put(ArmorItem.Type.CHESTPLATE, 8);
            map.put(ArmorItem.Type.HELMET, 3);
            map.put(ArmorItem.Type.BODY, 11);
        }),
        15,
        SoundEvents.ARMOR_EQUIP_IRON,
        2.0f,
        0.0f,
        () -> Ingredient.of(TCItems.REINFORCED_PLATE.get()));

    private static Holder<ArmorMaterial> register(String name, EnumMap<ArmorItem.Type, Integer> defense, int enchantmentValue, Holder<net.minecraft.sounds.SoundEvent> equipSound, float toughness, float knockbackResistance, Supplier<Ingredient> repairIngredient) {
        List<ArmorMaterial.Layer> layers = List.of(new ArmorMaterial.Layer(ResourceLocation.fromNamespaceAndPath(Traincraft.MOD_ID, name)));
        ArmorMaterial material = new ArmorMaterial(defense, enchantmentValue, equipSound, repairIngredient, layers, toughness, knockbackResistance);
        return Registry.registerForHolder(BuiltInRegistries.ARMOR_MATERIAL, ResourceLocation.fromNamespaceAndPath(Traincraft.MOD_ID, name), material);
    }
}
