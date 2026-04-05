/*
 * Traincraft
 * Copyright (c) 2011-2024.
 */

package traincraft.api;

import net.minecraft.core.Direction;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.items.IItemHandler;
import org.jetbrains.annotations.Nullable;

public interface IRollingStock {

    double getAcceleration();

    double getBreakPower();

    double getMaxSpeed();

    double getMaxReverseSpeed();

    double getMass();

    default IItemHandler getInventory(@Nullable Direction side) {
        return null;
    }

    default IFluidHandler getFluidTank(@Nullable Direction side) {
        return null;
    }

    boolean canLinkToAnotherRollingStock(AbstractRollingStock<?> other, @Nullable Player linker);

    void linkToAnotherRollingStock(AbstractRollingStock<?> other, @Nullable Player linker);

    boolean canPlayerOpenGuiOrContainer(Player player);

    boolean handlePlayerClickWithItem(Player player, net.minecraft.world.InteractionHand hand, ItemStack stack, Vec3 hitVector);
}
