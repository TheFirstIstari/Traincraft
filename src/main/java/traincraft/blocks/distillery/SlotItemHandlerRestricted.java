/*
 * Traincraft
 * Copyright (c) 2011-2024.
 */

package traincraft.blocks.distillery;

import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.SlotItemHandler;
import org.jetbrains.annotations.NotNull;

public class SlotItemHandlerRestricted extends SlotItemHandler {

    private final boolean outputOnly;

    public SlotItemHandlerRestricted(IItemHandler itemHandler, int index, int xPosition, int yPosition) {
        this(itemHandler, index, xPosition, yPosition, false);
    }

    public SlotItemHandlerRestricted(IItemHandler itemHandler, int index, int xPosition, int yPosition, boolean outputOnly) {
        super(itemHandler, index, xPosition, yPosition);
        this.outputOnly = outputOnly;
    }

    @Override
    public boolean mayPlace(@NotNull ItemStack stack) {
        return !outputOnly;
    }

    @Override
    public boolean mayPickup(net.minecraft.world.entity.player.Player playerIn) {
        return !outputOnly || super.mayPickup(playerIn);
    }
}
