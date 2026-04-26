/*
 * Traincraft
 * Copyright (c) 2011-2024.
 */

package traincraft.blocks.trainworkbench;

import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.neoforged.neoforge.items.SlotItemHandler;

public class ContainerTrainWorkbench extends AbstractContainerMenu {

    private final TileTrainWorkbench tile;
    private final IItemHandler handler;

    public ContainerTrainWorkbench(int containerId, Inventory playerInventory) {
        this(containerId, playerInventory, null);
    }

    public ContainerTrainWorkbench(int containerId, Inventory playerInventory, TileTrainWorkbench tile) {
        super(traincraft.network.TCMenus.TRAIN_WORKBENCH.get(), containerId);
        this.tile = tile;
        this.handler = tile != null ? tile.getItemHandler() : new ItemStackHandler(TileTrainWorkbench.INVENTORY_SIZE);

        // 3x3 input grid (slots 0..8). Layout matches the gui_builder.png background.
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                addSlot(new SlotItemHandler(handler, col + row * 3, 30 + col * 18, 17 + row * 18));
            }
        }

        // Output slot (index 9). Disallow insertion; consume ingredients on take.
        addSlot(new SlotItemHandler(handler, TileTrainWorkbench.OUTPUT_SLOT, 124, 35) {
            @Override
            public boolean mayPlace(ItemStack stack) {
                return false;
            }

            @Override
            public void onTake(Player player, ItemStack taken) {
                if (tile != null) {
                    tile.consumeIngredients();
                }
                super.onTake(player, taken);
            }
        });

        // Player inventory
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 9; col++) {
                addSlot(new Slot(playerInventory, col + row * 9 + 9, 8 + col * 18, 84 + row * 18));
            }
        }
        // Player hotbar
        for (int col = 0; col < 9; col++) {
            addSlot(new Slot(playerInventory, col, 8 + col * 18, 142));
        }
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        ItemStack result = ItemStack.EMPTY;
        if (index < 0 || index >= this.slots.size()) {
            return result;
        }
        Slot slot = this.slots.get(index);
        if (slot != null && slot.hasItem()) {
            ItemStack stack = slot.getItem();
            result = stack.copy();
            int gridStart = 0;
            int gridEnd = TileTrainWorkbench.GRID_SIZE;          // exclusive
            int outputIdx = TileTrainWorkbench.OUTPUT_SLOT;
            int playerStart = TileTrainWorkbench.INVENTORY_SIZE; // 10
            int playerEnd = this.slots.size();                   // exclusive

            if (index == outputIdx) {
                // Crafted result → push to player inventory; if pushed, consume ingredients.
                if (!this.moveItemStackTo(stack, playerStart, playerEnd, true)) {
                    return ItemStack.EMPTY;
                }
                if (tile != null) {
                    tile.consumeIngredients();
                }
                slot.onQuickCraft(stack, result);
            } else if (index < TileTrainWorkbench.INVENTORY_SIZE) {
                // From input grid → player inventory.
                if (!this.moveItemStackTo(stack, playerStart, playerEnd, true)) {
                    return ItemStack.EMPTY;
                }
            } else {
                // From player inventory → input grid.
                if (!this.moveItemStackTo(stack, gridStart, gridEnd, false)) {
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
