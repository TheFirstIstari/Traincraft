/*
 * Traincraft
 * Copyright (c) 2011-2024.
 */

package traincraft.entity.rollingstock;

import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.wrapper.InvWrapper;
import org.jetbrains.annotations.Nullable;
import traincraft.api.AbstractRollingStock;
import traincraft.api.PassengerSeat;
import traincraft.api.SyncState;

import java.util.List;

public class EntityFreightCart extends AbstractRollingStock<EntityFreightCart> {

    public static final int CARGO_SIZE = 27;

    private final SimpleContainer cargo = new SimpleContainer(CARGO_SIZE) {
        @Override
        public void setChanged() {
            super.setChanged();
            // SimpleContainer is otherwise self-contained, but persistence happens through the entity's NBT.
        }
    };

    private final IItemHandler cargoHandler = new InvWrapper(cargo);

    public EntityFreightCart(EntityType<?> type, Level level) {
        super(type, level);
        registerSeats(List.of(new PassengerSeat(-0.3, 0.5, -0.3, 0.6, 1.0, 0.6, false)));
    }

    @Override
    public IItemHandler getInventory(@Nullable Direction side) {
        return cargoHandler;
    }

    @Override
    public InteractionResult interact(Player player, InteractionHand hand) {
        // Sneak + empty-hand right-click opens the cargo inventory; otherwise fall through to seat/link logic.
        if (player.isShiftKeyDown() && player.getItemInHand(hand).isEmpty()) {
            if (!this.level().isClientSide) {
                player.openMenu(new SimpleMenuProvider(
                    (containerId, playerInventory, p) -> ChestMenu.threeRows(containerId, playerInventory, this.cargo),
                    Component.translatable("entity.traincraft.freight_cart")
                ));
            }
            return InteractionResult.sidedSuccess(this.level().isClientSide);
        }
        return super.interact(player, hand);
    }

    @Override
    public void remove(RemovalReason reason) {
        // Drop cargo when the entity is destroyed (matches vanilla minecart-with-chest behaviour).
        if (!this.level().isClientSide && reason == RemovalReason.KILLED) {
            for (int i = 0; i < cargo.getContainerSize(); i++) {
                ItemStack stack = cargo.getItem(i);
                if (!stack.isEmpty()) {
                    this.spawnAtLocation(stack);
                }
            }
            cargo.clearContent();
        }
        super.remove(reason);
    }

    @Override
    protected void readFromNBT(CompoundTag nbt, SyncState state) {
        super.readFromNBT(nbt, state);
        if (nbt.contains("cargo_wrap", 10)) {
            NonNullList<ItemStack> items = NonNullList.withSize(CARGO_SIZE, ItemStack.EMPTY);
            ContainerHelper.loadAllItems(nbt.getCompound("cargo_wrap"), items, this.registryAccess());
            for (int i = 0; i < items.size(); i++) cargo.setItem(i, items.get(i));
        }
    }

    @Override
    protected void writeToNBT(CompoundTag nbt, SyncState state) {
        super.writeToNBT(nbt, state);
        NonNullList<ItemStack> items = NonNullList.withSize(CARGO_SIZE, ItemStack.EMPTY);
        for (int i = 0; i < CARGO_SIZE; i++) items.set(i, cargo.getItem(i));
        CompoundTag wrap = new CompoundTag();
        ContainerHelper.saveAllItems(wrap, items, this.registryAccess());
        nbt.put("cargo_wrap", wrap);
    }

    @Override
    public double getAcceleration() {
        return 0.1;
    }

    @Override
    public double getBreakPower() {
        return 0.3;
    }

    @Override
    public double getMaxSpeed() {
        return 15.0;
    }

    @Override
    public double getMaxReverseSpeed() {
        return 5.0;
    }

    @Override
    public double getMass() {
        return 5000.0;
    }

    @Override
    public boolean canLinkToAnotherRollingStock(AbstractRollingStock<?> other, net.minecraft.world.entity.player.Player linker) {
        return true;
    }

    @Override
    public void linkToAnotherRollingStock(AbstractRollingStock<?> other, net.minecraft.world.entity.player.Player linker) {
        if (this.next == null && other != this && other.getPrevious() == null) {
            this.setNextRollingStock(other);
            other.setPreviousRollingStock(this);
        }
    }

    @Override
    public Item getDropItem() {
        return Items.MINECART;
    }
}
