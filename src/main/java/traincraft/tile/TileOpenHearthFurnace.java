package traincraft.tile;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class TileOpenHearthFurnace extends BaseTile {
    public TileOpenHearthFurnace(BlockPos pos, BlockState state) { super(null, pos, state); }
    @Nullable @Override protected AbstractContainerMenu createMenu(int windowId, Inventory playerInventory) { return null; }
    @Override public int getContainerSize() { return 0; }
    @Override public boolean isEmpty() { return true; }
    @Override public net.minecraft.world.item.ItemStack getItem(int slot) { return net.minecraft.world.item.ItemStack.EMPTY; }
    @Override public net.minecraft.world.item.ItemStack removeItem(int slot, int amount) { return net.minecraft.world.item.ItemStack.EMPTY; }
    @Override public net.minecraft.world.item.ItemStack removeItemNoUpdate(int slot) { return net.minecraft.world.item.ItemStack.EMPTY; }
    @Override public void setItem(int slot, net.minecraft.world.item.ItemStack stack) {}
    @Override public boolean stillValid(net.minecraft.world.entity.player.Player player) { return false; }
    @Override public void clearContent() {}
}
