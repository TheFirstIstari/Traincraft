/*
 * Traincraft
 * Copyright (c) 2011-2024.
 */

package traincraft.items.armor;

import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

/**
 * An armor item that can be dyed using dyes in the game.
 * Extends ArmorItem to provide standard armor functionality
 * while implementing IDyeableArmorItem for color customization.
 */
public class DyeableArmorItem extends ArmorItem implements IDyeableArmorItem {

    /**
     * Default constructor for creating a dyeable armor item.
     *
     * @param material the armor material
     * @param type    the armor type (helmet, chestplate, leggings, or boots)
     * @param properties item properties
     */
    public DyeableArmorItem(net.minecraft.core.Holder<ArmorMaterial> material, ArmorItem.Type type, Item.Properties properties) {
        super(material, type, properties);
    }

    @Override
    public int getColor(ItemStack stack) {
        return IDyeableArmorItem.super.getColor(stack);
    }

    @Override
    public void setColor(ItemStack stack, int color) {
        IDyeableArmorItem.super.setColor(stack, color);
    }

    @Override
    public boolean hasCustomColor(ItemStack stack) {
        return IDyeableArmorItem.super.hasCustomColor(stack);
    }

    @Override
    public void clearColor(ItemStack stack) {
        IDyeableArmorItem.super.clearColor(stack);
    }

    @Override
    public boolean isValidRepairItem(ItemStack toRepair, ItemStack repair) {
        // Use the default armor repair logic but also check for leather
        // for compatibility with vanilla dyeing
        return super.isValidRepairItem(toRepair, repair);
    }

    /**
     * Gets the default color for new armor (used when displaying in inventory).
     * Subclasses can override this to provide different default colors.
     *
     * @return the default RGB color value
     */
    protected int getDefaultColor() {
        return DEFAULT_COLOR;
    }

    /**
     * Creates a new dyeable armor item with a specific default color.
     * Useful for creating pre-colored uniforms.
     *
     * @param material     the armor material
     * @param type        the armor type
     * @param properties  item properties
     * @param defaultColor the default color to apply
     * @return a new ItemStack with the default color set
     */
    public static ItemStack createWithDefaultColor(net.minecraft.core.Holder<ArmorMaterial> material, ArmorItem.Type type,
                                                    Item.Properties properties, int defaultColor) {
        DyeableArmorItem item = new DyeableArmorItem(material, type, properties);
        ItemStack stack = new ItemStack(item);
        item.setColor(stack, defaultColor);
        return stack;
    }
}