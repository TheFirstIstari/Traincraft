/*
 * Traincraft
 * Copyright (c) 2011-2024.
 */

package traincraft.blocks.distillery;

import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import traincraft.Traincraft;

public class ContainerDistillery extends AbstractContainerMenu {

    final TileDistillery tile;

    public ContainerDistillery(int containerId, Inventory playerInventory) {
        this(containerId, playerInventory, null);
    }

    public ContainerDistillery(int containerId, Inventory playerInventory, TileDistillery tile) {
        super(traincraft.network.TCMenus.DISTILLERY.get(), containerId);
        this.tile = tile;

        addSlot(new SlotItemHandlerRestricted(tile.getItemHandler(), TileDistillery.INPUT_SLOT, 56, 17));
        addSlot(new SlotItemHandlerRestricted(tile.getItemHandler(), TileDistillery.BURN_SLOT, 56, 53));
        addSlot(new SlotItemHandlerRestricted(tile.getItemHandler(), TileDistillery.OUTPUT_SLOT, 116, 60, true));
        addSlot(new SlotItemHandlerRestricted(tile.getItemHandler(), TileDistillery.CONTAINER_INPUT_SLOT, 123, 8));
        addSlot(new SlotItemHandlerRestricted(tile.getItemHandler(), TileDistillery.CONTAINER_OUTPUT_SLOT, 123, 33, true));

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
        if (slot.hasItem()) {
            ItemStack stack = slot.getItem();
            result = stack.copy();
            if (index < 5) {
                if (!this.moveItemStackTo(stack, 5, this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else {
                if (!this.moveItemStackTo(stack, 0, 5, false)) {
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
}
