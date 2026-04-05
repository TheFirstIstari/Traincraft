/*
 * Traincraft
 * Copyright (c) 2011-2024.
 */

package traincraft.blocks.assemblytable;

import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.SlotItemHandler;
import org.jetbrains.annotations.NotNull;

public class AssemblySlot extends net.neoforged.neoforge.items.SlotItemHandler {
    public AssemblySlot(IItemHandler itemHandler, int index, int x, int y) {
        super(itemHandler, index, x, y);
    }

    @Override
    public int getMaxStackSize() {
        return 64;
    }

    @Override
    public int getMaxStackSize(@NotNull ItemStack stack) {
        return 64;
    }
}
