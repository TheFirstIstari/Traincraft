/*
 * Traincraft
 * Copyright (c) 2011-2024.
 */

package traincraft.tile;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import traincraft.blocks.switchstand.BlockSwitchStand;

public class TileSwitchStand extends BaseTile {
    
    // Track the current switch state (true = switched/activated, false = normal)
    private boolean switchState = false;
    
    public TileSwitchStand(BlockPos pos, BlockState state) {
        super(TCTiles.SWITCH_STAND.get(), pos, state);
    }
    
    /**
     * Set the switch state and update the block state accordingly
     * @param switchState the new switch state
     */
    public void setSwitchState(boolean switchState) {
        this.switchState = switchState;
        
        // Update the block state
        if (this.level != null && !this.level.isClientSide) {
            this.level.setBlockAndUpdate(this.worldPosition, 
                this.getBlockState().setValue(BlockSwitchStand.POWERED, switchState));
            this.sync(); // Send update to clients
        }
    }
    
    /**
     * Get the current switch state
     * @return the current switch state
     */
    public boolean getSwitchState() {
        return this.switchState;
    }
    
    /**
     * Toggle the switch state
     * @return the new switch state
     */
    public boolean toggleSwitchState() {
        this.setSwitchState(!this.switchState);
        return this.switchState;
    }
    
    @Override
    protected void save(net.minecraft.nbt.CompoundTag tag, SyncState state) {
        super.save(tag, state);
        tag.putBoolean("switchState", this.switchState);
    }
    
    @Override
    protected void load(net.minecraft.nbt.CompoundTag tag, SyncState state) {
        super.load(tag, state);
        if (tag.contains("switchState")) {
            this.switchState = tag.getBoolean("switchState");
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
