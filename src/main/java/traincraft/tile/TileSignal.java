/*
 * Traincraft
 * Copyright (c) 2011-2024.
 */

package traincraft.tile;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.Nullable;
import traincraft.TCSounds;
import traincraft.api.AbstractRollingStock;
import traincraft.blocks.signal.BlockSignal;
import traincraft.tile.TCTiles;

import java.util.List;

public class TileSignal extends BaseTile {
    
    // Radius within which to detect trains
    private static final int DETECTION_RADIUS = 10;
    
    // Track the previous powered state to avoid unnecessary updates
    private boolean previousPoweredState = false;
    
    public TileSignal(BlockPos pos, BlockState state) {
        super(TCTiles.SIGNAL.get(), pos, state);
    }
    
        public static void serverTick(Level level, BlockPos pos, BlockState state, TileSignal tile) {
        if (level.isClientSide) {
            return;
        }
        
        // Detect trains in the area
        boolean trainDetected = tile.detectTrains(level, pos);
        
        // Update the block state if needed
        if (trainDetected != tile.previousPoweredState) {
            tile.previousPoweredState = trainDetected;
            level.setBlockAndUpdate(pos, state.setValue(BlockSignal.POWERED, trainDetected));
            tile.sync(); // Send update to clients
            
            // Play signal activation sound when state changes
            if (trainDetected) {
                level.playSound(null, pos, TCSounds.SIGNAL_ACTIVATE.get(), SoundSource.BLOCKS, 0.5f, 1.0f);
            }
        }
    }
    
    /**
     * Detect trains within the detection radius
     * @param level The world level
     * @param pos The position of this tile entity
     * @return true if a train is detected, false otherwise
     */
    private boolean detectTrains(Level level, BlockPos pos) {
        // Create a bounding box for train detection
        AABB detectionBox = new AABB(
            pos.getX() - DETECTION_RADIUS, 
            pos.getY() - DETECTION_RADIUS, 
            pos.getZ() - DETECTION_RADIUS,
            pos.getX() + DETECTION_RADIUS, 
            pos.getY() + DETECTION_RADIUS, 
            pos.getZ() + DETECTION_RADIUS
        );
        
        // Check for any rolling stock entities in the area
        List<AbstractRollingStock> rollingStock = level.getEntitiesOfClass(AbstractRollingStock.class, detectionBox);
        return !rollingStock.isEmpty();
    }
    
    @Override
    protected void save(net.minecraft.nbt.CompoundTag tag, SyncState state) {
        super.save(tag, state);
        tag.putBoolean("powered", this.previousPoweredState);
    }
    
    @Override
    protected void load(net.minecraft.nbt.CompoundTag tag, SyncState state) {
        super.load(tag, state);
        if (tag.contains("powered")) {
            this.previousPoweredState = tag.getBoolean("powered");
        }
    }

    @Nullable
    @Override
    protected AbstractContainerMenu createMenu(int windowId, Inventory playerInventory) {
        return null;
    }

    @Override
    public int getContainerSize() {
        return 0;
    }

    @Override
    public boolean isEmpty() {
        return true;
    }

    @Override
    public net.minecraft.world.item.ItemStack getItem(int slot) {
        return net.minecraft.world.item.ItemStack.EMPTY;
    }

    @Override
    public net.minecraft.world.item.ItemStack removeItem(int slot, int amount) {
        return net.minecraft.world.item.ItemStack.EMPTY;
    }

    @Override
    public net.minecraft.world.item.ItemStack removeItemNoUpdate(int slot) {
        return net.minecraft.world.item.ItemStack.EMPTY;
    }

    @Override
    public void setItem(int slot, net.minecraft.world.item.ItemStack stack) {
    }

    @Override
    public boolean stillValid(net.minecraft.world.entity.player.Player player) {
        return false;
    }

    @Override
    public void clearContent() {
    }
}
