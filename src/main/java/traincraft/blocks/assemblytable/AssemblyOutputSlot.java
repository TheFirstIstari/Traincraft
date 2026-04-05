/*
 * Traincraft
 * Copyright (c) 2011-2024.
 */

package traincraft.blocks.assemblytable;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.SlotItemHandler;
import org.jetbrains.annotations.NotNull;

public class AssemblyOutputSlot extends SlotItemHandler {

    private final TileAssemblyTable tile;

    public AssemblyOutputSlot(TileAssemblyTable tile, int index, int x, int y) {
        super(tile.getItemHandler(), index, x, y);
        this.tile = tile;
    }

    @Override
    public boolean mayPlace(@NotNull ItemStack stack) {
        return false;
    }

    @Override
    public void onTake(Player player, ItemStack stack) {
        tile.onItemCrafted();
        super.onTake(player, stack);
    }

    @Override
    public int getMaxStackSize() {
        return 64;
    }
}
