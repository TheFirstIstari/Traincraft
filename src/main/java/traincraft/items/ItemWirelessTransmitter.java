/*
 * Traincraft
 * Copyright (c) 2011-2024.
 */

package traincraft.items;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

import java.util.List;

public class ItemWirelessTransmitter extends Item {

    public ItemWirelessTransmitter() {
        super(new Properties().stacksTo(1));
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(Component.literal("§7Wireless Transmitter"));
        tooltip.add(Component.literal("§7Send signals to trains remotely."));
    }
}
