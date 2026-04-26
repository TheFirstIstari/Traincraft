/*
 * Traincraft
 * Copyright (c) 2011-2024.
 */

package traincraft.entity;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import traincraft.api.LocomotiveDiesel;

public class LocomotiveDieselSmall extends LocomotiveDiesel<LocomotiveDieselSmall> {

    public LocomotiveDieselSmall(EntityType<?> type, Level level) {
        super(type, level);
    }

    @Override
    public Item getDropItem() {
        return Items.MINECART;
    }

    @Override
    public double getAcceleration() {
        return 0.4;
    }

    @Override
    public double getBreakPower() {
        return 0.5;
    }

    @Override
    public double getMaxSpeed() {
        return 30.0;
    }

    @Override
    public double getMaxReverseSpeed() {
        return 12.0;
    }

    @Override
    public double getMass() {
        return 14000.0;
    }

    @Override
    public int getFuelTankCapacity() {
        return 32000;
    }
}
