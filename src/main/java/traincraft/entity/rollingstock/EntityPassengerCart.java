/*
 * Traincraft
 * Copyright (c) 2011-2024.
 */

package traincraft.entity.rollingstock;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import traincraft.api.AbstractRollingStock;
import traincraft.api.PassengerSeat;

import java.util.List;

public class EntityPassengerCart extends AbstractRollingStock<EntityPassengerCart> {

    public EntityPassengerCart(EntityType<?> type, Level level) {
        super(type, level);
        registerSeats(List.of(
            new PassengerSeat(-0.3, 0.5, -0.5, 0.6, 1.0, 0.5, false),
            new PassengerSeat(-0.3, 0.5, 0.0, 0.6, 1.0, 0.5, false),
            new PassengerSeat(-0.3, 0.5, 0.5, 0.6, 1.0, 0.5, false)
        ));
    }

    @Override
    public double getAcceleration() {
        return 0.1;
    }

    @Override
    public double getBreakPower() {
        return 0.4;
    }

    @Override
    public double getMaxSpeed() {
        return 20.0;
    }

    @Override
    public double getMaxReverseSpeed() {
        return 7.0;
    }

    @Override
    public double getMass() {
        return 8000.0;
    }

    @Override
    public boolean canLinkToAnotherRollingStock(AbstractRollingStock<?> other, net.minecraft.world.entity.player.Player linker) {
        return true;
    }

    @Override
    public void linkToAnotherRollingStock(AbstractRollingStock<?> other, net.minecraft.world.entity.player.Player linker) {
        if (this.next == null && other != this) {
            this.next = other;
            if (other.getPrevious() == null) {
                other.linkToAnotherRollingStock(this, linker);
            }
        }
    }

    @Override
    public Item getDropItem() {
        return Items.MINECART;
    }
}
