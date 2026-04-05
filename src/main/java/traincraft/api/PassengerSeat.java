/*
 * Traincraft
 * Copyright (c) 2011-2024.
 */

package traincraft.api;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public class PassengerSeat {

    private final AABB boundingBox;
    private final boolean controllingSeat;
    @Nullable
    private Entity currentUser;

    public PassengerSeat(double offsetX, double offsetY, double offsetZ, double width, double height, double depth, boolean controllingSeat) {
        this.boundingBox = new AABB(offsetX, offsetY, offsetZ, offsetX + width, offsetY + height, offsetZ + depth);
        this.controllingSeat = controllingSeat;
    }

    public AABB getBoundingBox() {
        return this.boundingBox;
    }

    public Vec3 getCenter() {
        return this.boundingBox.getCenter();
    }

    public double getHeight() {
        return this.boundingBox.getYsize();
    }

    public boolean isControllingSeat() {
        return this.controllingSeat;
    }

    public boolean isFree() {
        return this.currentUser == null;
    }

    public boolean isUsedBy(Entity entity) {
        return this.currentUser == entity;
    }

    @Nullable
    public Entity getCurrentUser() {
        return this.currentUser;
    }

    public void setCurrentUser(@Nullable Entity currentUser) {
        this.currentUser = currentUser;
    }
}
