/*
 * Traincraft
 * Copyright (c) 2011-2024.
 */

package traincraft.blocks.assemblytable;

import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class ContainerAssemblyTable extends AbstractContainerMenu {

    final TileAssemblyTable tile;

    public ContainerAssemblyTable(int containerId, Inventory playerInventory) {
        this(containerId, playerInventory, null);
    }

    public ContainerAssemblyTable(int containerId, Inventory playerInventory, TileAssemblyTable tile) {
        super(traincraft.network.TCMenus.ASSEMBLY_TABLE.get(), containerId);
        this.tile = tile;

        if (tile != null) {
            var handler = tile.getItemHandler();

            // Crafting slots (0-9)
            addSlot(new AssemblySlot(handler, 0, 25, 27));
            addSlot(new AssemblySlot(handler, 1, 79, 27));
            addSlot(new AssemblySlot(handler, 2, 115, 27));
            addSlot(new AssemblySlot(handler, 3, 145, 27));
            addSlot(new AssemblySlot(handler, 4, 25, 61));
            addSlot(new AssemblySlot(handler, 5, 79, 61));
            addSlot(new AssemblySlot(handler, 6, 115, 61));
            addSlot(new AssemblySlot(handler, 7, 43, 93));
            addSlot(new AssemblySlot(handler, 8, 79, 93));
            addSlot(new AssemblySlot(handler, 9, 145, 93));

            // Storage slots (10-17)
            for (int i = 0; i < 2; i++) {
                for (int j = 0; j < 4; j++) {
                    addSlot(new net.neoforged.neoforge.items.SlotItemHandler(handler, 10 + (j + i * 4), 8 + j * 18, 128 + i * 18));
                }
            }

            // Output slots (18-25)
            for (int i = 0; i < 2; i++) {
                for (int j = 0; j < 4; j++) {
                    addSlot(new AssemblyOutputSlot(tile, 18 + (j + i * 4), 92 + j * 18, 128 + i * 18));
                }
            }
        }

        // Player inventory
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 9; j++) {
                addSlot(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 174 + i * 18));
            }
        }

        // Player hotbar
        for (int i = 0; i < 9; i++) {
            addSlot(new Slot(playerInventory, i, 8 + i * 18, 232));
        }
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        ItemStack result = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot != null && slot.hasItem()) {
            ItemStack stack = slot.getItem();
            result = stack.copy();
            if (index >= 18 && index <= 25) {
                if (!this.moveItemStackTo(stack, 26, this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
                slot.onQuickCraft(stack, result);
            } else {
                if (!this.moveItemStackTo(stack, 26, this.slots.size(), false)) {
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
