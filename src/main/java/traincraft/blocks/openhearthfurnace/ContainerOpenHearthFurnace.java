/*
 * Traincraft
 * Copyright (c) 2011-2024.
 */

package traincraft.blocks.openhearthfurnace;

import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.SlotItemHandler;

public class ContainerOpenHearthFurnace extends AbstractContainerMenu {

    final TileOpenHearthFurnace tile;

    public ContainerOpenHearthFurnace(int containerId, Inventory playerInventory) {
        this(containerId, playerInventory, null);
    }

    public ContainerOpenHearthFurnace(int containerId, Inventory playerInventory, TileOpenHearthFurnace tile) {
        super(traincraft.network.TCMenus.OPEN_HEARTH_FURNACE.get(), containerId);
        this.tile = tile;

        if (tile != null) {
            var handler = tile.getItemHandler();
            addSlot(new SlotItemHandler(handler, TileOpenHearthFurnace.INPUT_SLOT, 56, 17));
            addSlot(new SlotItemHandler(handler, TileOpenHearthFurnace.FUEL_SLOT, 56, 53));
            addSlot(new OutputSlot(handler, TileOpenHearthFurnace.OUTPUT_SLOT, 116, 35));
        }

        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 9; col++) {
                addSlot(new Slot(playerInventory, col + row * 9 + 9, 8 + col * 18, 84 + row * 18));
            }
        }
        for (int col = 0; col < 9; col++) {
            addSlot(new Slot(playerInventory, col, 8 + col * 18, 142));
        }
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        ItemStack result = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot != null && slot.hasItem()) {
            ItemStack stack = slot.getItem();
            result = stack.copy();
            if (index < 3) {
                if (!this.moveItemStackTo(stack, 3, this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else {
                if (stack.getBurnTime(null) > 0) {
                    if (!this.moveItemStackTo(stack, 1, 2, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (!this.moveItemStackTo(stack, 0, 1, false)) {
                    return ItemStack.EMPTY;
                }
            }
            if (stack.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }
        }
        return result;
    }

    @Override
    public boolean stillValid(Player player) {
        return tile != null && tile.getLevel() != null && tile.getLevel().getBlockEntity(tile.getBlockPos()) == tile
            && player.distanceToSqr(tile.getBlockPos().getX() + 0.5, tile.getBlockPos().getY() + 0.5, tile.getBlockPos().getZ() + 0.5) <= 64.0;
    }

    private static class OutputSlot extends SlotItemHandler {
        public OutputSlot(net.neoforged.neoforge.items.IItemHandler handler, int index, int x, int y) {
            super(handler, index, x, y);
        }

        @Override
        public void onTake(Player player, ItemStack stack) {
            super.onTake(player, stack);
        }
    }
}
