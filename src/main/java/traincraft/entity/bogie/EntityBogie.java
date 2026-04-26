/*
 * Traincraft
 * Copyright (c) 2011-2024.
 */

package traincraft.entity.bogie;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import traincraft.api.AbstractRollingStock;
import traincraft.api.SyncState;

public class EntityBogie extends AbstractRollingStock<EntityBogie> {
    
    // Reference to the main train entity this bogie is attached to
    private AbstractRollingStock<?> mainTrain;
    
    // Position offset from the main train
    private Vec3 positionOffset = Vec3.ZERO;
    
    // Distance behind the main train this bogie should follow at
    private double followDistance = 1.5;
    
    public EntityBogie(EntityType<?> type, Level level) {
        super(type, level);
        // Bogie-specific initialization
    }
    
    @Override
    public void tick() {
        super.tick();
        
        // Only run on server side
        if (this.level().isClientSide) {
            return;
        }
        
        // Follow the main train if linked
        if (this.mainTrain != null && this.mainTrain.isAlive()) {
            this.followMainTrain();
        }
    }
    
    /**
     * Make this bogie follow the main train entity
     */
    private void followMainTrain() {
        // Calculate the target position based on the main train's position and our offset
        Vec3 targetPos = this.mainTrain.position().add(this.positionOffset);
        
        // Calculate direction to target position
        Vec3 direction = targetPos.subtract(this.position());
        
        // If we're too far from our target position, move toward it
        double distance = direction.length();
        if (distance > 0.1) {
            // Normalize direction and apply movement
            Vec3 movement = direction.normalize().scale(Math.min(distance * 0.1, 0.5));
            this.setDeltaMovement(movement);
        }
        
        // Orient the bogie to face the direction of movement
        if (direction.horizontalDistance() > 0.1) {
            this.setYRot((float) Math.toDegrees(Math.atan2(-direction.x, direction.z)));
        }
    }
    
    /**
     * Link this bogie to a main train entity
     */
    public void linkToMainTrain(AbstractRollingStock<?> mainTrain, Vec3 offset) {
        this.mainTrain = mainTrain;
        this.positionOffset = offset;
    }
    
    /**
     * Get the main train this bogie is linked to
     */
    public AbstractRollingStock<?> getMainTrain() {
        return this.mainTrain;
    }
    
    @Override
    protected void readFromNBT(CompoundTag nbt, SyncState state) {
        super.readFromNBT(nbt, state);
        
        // Read position offset
        if (nbt.contains("positionOffsetX") && nbt.contains("positionOffsetY") && nbt.contains("positionOffsetZ")) {
            this.positionOffset = new Vec3(
                nbt.getDouble("positionOffsetX"),
                nbt.getDouble("positionOffsetY"),
                nbt.getDouble("positionOffsetZ")
            );
        }
        
        this.followDistance = nbt.getDouble("followDistance");
    }
    
    @Override
    protected void writeToNBT(CompoundTag nbt, SyncState state) {
        super.writeToNBT(nbt, state);
        
        // Write position offset
        if (this.positionOffset != null) {
            nbt.putDouble("positionOffsetX", this.positionOffset.x);
            nbt.putDouble("positionOffsetY", this.positionOffset.y);
            nbt.putDouble("positionOffsetZ", this.positionOffset.z);
        }
        
        nbt.putDouble("followDistance", this.followDistance);
    }
    
    // Bogies don't have their own acceleration, they follow the main train
    @Override
    public double getAcceleration() {
        return 0.0;
    }
    
    @Override
    public double getBreakPower() {
        return 0.0;
    }
    
    @Override
    public double getMaxSpeed() {
        return 0.0; // Bogies don't have independent max speed
    }
    
    @Override
    public double getMaxReverseSpeed() {
        return 0.0;
    }
    
    // Bogies are quite light
    @Override
    public double getMass() {
        return 500.0;
    }
    
    // Bogies can't be linked to other rolling stock
    @Override
    public boolean canLinkToAnotherRollingStock(AbstractRollingStock<?> other, net.minecraft.world.entity.player.Player linker) {
        return false;
    }
    
    @Override
    public void linkToAnotherRollingStock(AbstractRollingStock<?> other, net.minecraft.world.entity.player.Player linker) {
        // Bogies can't be linked to other rolling stock
    }

    @Override
    public Item getDropItem() {
        return Items.MINECART;
    }
}