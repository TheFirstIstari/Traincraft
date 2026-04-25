/*
 * Traincraft
 * Copyright (c) 2011-2024.
 */

package traincraft.gui;

import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import traincraft.network.TCMenus;

public class ContainerGuideBook extends AbstractContainerMenu {

    public ContainerGuideBook(int containerId, Inventory playerInventory) {
        super(TCMenus.GUIDE_BOOK.get(), containerId);
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean stillValid(Player player) {
        return true;
    }
}