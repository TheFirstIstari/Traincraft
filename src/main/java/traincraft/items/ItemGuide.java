/*
 * Traincraft
 * Copyright (c) 2011-2024.
 */

package traincraft.items;

import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.Inventory;
import traincraft.gui.ContainerGuideBook;
import traincraft.network.TCMenus;

import java.util.List;

public class ItemGuide extends Item {

    public ItemGuide() {
        super(new Properties().stacksTo(1));
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        if (context.getLevel().isClientSide) {
            return InteractionResult.SUCCESS;
        }
        // Open the guide book menu on the server side
        context.getPlayer().openMenu(new SimpleMenuProvider(
            (containerId, playerInventory, player) -> new ContainerGuideBook(containerId, playerInventory),
            Component.translatable("screen.traincraft.guide_book")
        ));
        return InteractionResult.SUCCESS;
    }
    
    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        if (level.isClientSide) {
            return InteractionResult.SUCCESS;
        }
        // Open the guide book menu on the server side
        player.openMenu(new SimpleMenuProvider(
            (containerId, playerInventory, p) -> new ContainerGuideBook(containerId, playerInventory),
            Component.translatable("screen.traincraft.guide_book")
        ));
        return InteractionResult.SUCCESS;
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(Component.translatable("item.traincraft.guide.tooltip"));
    }
}