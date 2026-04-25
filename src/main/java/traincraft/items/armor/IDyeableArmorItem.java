/*
 * Traincraft
 * Copyright (c) 2011-2024.
 */

package traincraft.items.armor;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;

/**
 * Interface for armor items that can be dyed.
 * Provides methods to get and set the armor's dye color.
 */
public interface IDyeableArmorItem {

    /**
     * The NBT key used to store the armor color.
     */
    String COLOR_TAG = "display";

    /**
     * The NBT key for the color value within the display tag.
     */
    String COLOR_KEY = "color";

    /**
     * Default color (white) when no dye has been applied.
     */
    int DEFAULT_COLOR = 0xFFFFFF;

    /**
     * Gets the RGB color of this armor item.
     *
     * @param stack the armor item stack
     * @return the RGB color value, or DEFAULT_COLOR if not set
     */
    default int getColor(ItemStack stack) {
        CompoundTag nbt = stack.getTag();
        if (nbt != null && nbt.contains(COLOR_TAG, 10)) {
            CompoundTag display = nbt.getCompound(COLOR_TAG);
            if (display.contains(COLOR_KEY, 3)) {
                return display.getInt(COLOR_KEY);
            }
        }
        return DEFAULT_COLOR;
    }

    /**
     * Sets the RGB color of this armor item.
     *
     * @param stack the armor item stack
     * @param color the RGB color value to set
     */
    default void setColor(ItemStack stack, int color) {
        CompoundTag nbt = stack.getOrCreateTag();
        CompoundTag display = nbt.contains(COLOR_TAG, 10) ? nbt.getCompound(COLOR_TAG) : new CompoundTag();
        display.putInt(COLOR_KEY, color);
        nbt.put(COLOR_TAG, display);
        stack.setTag(nbt);
    }

    /**
     * Checks if this armor item has a custom color applied.
     *
     * @param stack the armor item stack
     * @return true if a custom color is applied (not default white)
     */
    default boolean hasCustomColor(ItemStack stack) {
        return getColor(stack) != DEFAULT_COLOR;
    }

    /**
     * Removes the custom color from this armor item,
     * resetting it to the default color.
     *
     * @param stack the armor item stack
     */
    default void clearColor(ItemStack stack) {
        setColor(stack, DEFAULT_COLOR);
    }
}