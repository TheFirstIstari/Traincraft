/*
 * Traincraft
 * Copyright (c) 2011-2024.
 */

package traincraft.entity;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import traincraft.api.LocomotiveSteam;

public class LocomotiveSteamSmall extends LocomotiveSteam<LocomotiveSteamSmall> {

    public LocomotiveSteamSmall(EntityType<?> type, Level level) {
        super(type, level);
    }

    @Override
    public Item getDropItem() {
        return Items.MINECART;
    }

    @Override
    public double getAcceleration() {
        return 0.3;
    }

    @Override
    public double getBreakPower() {
        return 0.5;
    }

    @Override
    public double getMaxSpeed() {
        return 25.0;
    }

    @Override
    public double getMaxReverseSpeed() {
        return 8.0;
    }

    @Override
    public double getMass() {
        return 12000.0;
    }

    @Override
    public int getWaterTankCapacity() {
        return 24000;
    }
}
