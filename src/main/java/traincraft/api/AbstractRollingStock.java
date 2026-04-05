/*
 * Traincraft
 * Copyright (c) 2011-2024.
 */

package traincraft.api;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.common.util.INBTSerializable;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.items.IItemHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public abstract class AbstractRollingStock<A extends AbstractRollingStock<A>> extends net.minecraft.world.entity.vehicle.AbstractMinecart implements IRollingStock {

    private static final EntityDataAccessor<Integer> DATA_SKIN_ID = SynchedEntityData.defineId(AbstractRollingStock.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> DATA_RESTRICTION = SynchedEntityData.defineId(AbstractRollingStock.class, EntityDataSerializers.INT);

    @Nullable
    private UUID owner;
    @Nullable
    private String name;
    private EnumRestriction restriction = EnumRestriction.PUBLIC;
    private int activeSkin = 0;
    private double travelDistance = 0D;
    private final Map<String, net.minecraft.resources.ResourceLocation> skins = new HashMap<>();
    @Nullable
    protected AbstractRollingStock<?> next;
    @Nullable
    protected AbstractRollingStock<?> previous;
    private final List<Vec3> axes = new ArrayList<>();
    private final List<PassengerSeat> seats = new ArrayList<>();

    protected AbstractRollingStock(EntityType<?> type, Level level) {
        super(type, level);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(DATA_SKIN_ID, 0);
        builder.define(DATA_RESTRICTION, 0);
    }

    @Override
    public void tick() {
        super.tick();

        if (this.level().isClientSide) {
            return;
        }

        this.travelDistance += this.getDeltaMovement().length();

        LivingEntity controller = getControllingPassenger();
        if (controller != null) {
            applyCustomPhysics(controller);
        }
    }

    private void applyCustomPhysics(LivingEntity controller) {
        double maxSpeed = getMaxSpeed() / 20.0;
        double accel = getAcceleration() / 20.0;
        double brake = getBreakPower() / 20.0;

        Vec3 motion = getDeltaMovement();
        double currentSpeed = motion.horizontalDistance();

        if (maxSpeed > 0 && currentSpeed > maxSpeed) {
            Vec3 dir = motion.normalize();
            setDeltaMovement(new Vec3(dir.x * maxSpeed, motion.y, dir.z * maxSpeed));
        }

        if (currentSpeed > 0.01) {
            double friction = 1.0 - brake;
            Vec3 newMotion = new Vec3(motion.x * friction, motion.y, motion.z * friction);
            if (newMotion.horizontalDistance() < 0.001) {
                newMotion = new Vec3(0, motion.y, 0);
            }
            setDeltaMovement(new Vec3(newMotion.x, motion.y, newMotion.z));
        }
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        readFromNBT(tag, SyncState.SAVE);
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        writeToNBT(tag, SyncState.SAVE);
    }

    protected void readFromNBT(CompoundTag nbt, SyncState state) {
        if (nbt.hasUUID("owner")) {
            this.owner = nbt.getUUID("owner");
        }
        if (nbt.contains("name", 8)) {
            this.name = nbt.getString("name");
        }
        if (nbt.contains("restriction", 99)) {
            int ord = nbt.getInt("restriction");
            if (ord >= 0 && ord < EnumRestriction.values().length) {
                this.restriction = EnumRestriction.values()[ord];
            }
        }
        this.activeSkin = nbt.getInt("active_skin");
        this.travelDistance = nbt.getDouble("travel_distance");

        IItemHandler inventory = getInventory(null);
        if (inventory instanceof INBTSerializable<?> serializable) {
            CompoundTag invTag = nbt.getCompound("inventory");
            ((INBTSerializable<CompoundTag>) serializable).deserializeNBT(this.registryAccess(), invTag);
        }

        IFluidHandler fluidHandler = getFluidTank(null);
        if (fluidHandler instanceof INBTSerializable<?> serializable) {
            CompoundTag fluidTag = nbt.getCompound("fluid_tank");
            ((INBTSerializable<CompoundTag>) serializable).deserializeNBT(this.registryAccess(), fluidTag);
        }
    }

    protected void writeToNBT(CompoundTag nbt, SyncState state) {
        if (this.owner != null) {
            nbt.putUUID("owner", this.owner);
        }
        if (this.name != null) {
            nbt.putString("name", this.name);
        }
        nbt.putInt("restriction", this.restriction.ordinal());
        nbt.putInt("active_skin", this.activeSkin);
        nbt.putDouble("travel_distance", this.travelDistance);

        IItemHandler inventory = getInventory(null);
        if (inventory instanceof INBTSerializable<?> serializable) {
            CompoundTag invTag = ((INBTSerializable<CompoundTag>) serializable).serializeNBT(this.registryAccess());
            nbt.put("inventory", invTag);
        }

        IFluidHandler fluidHandler = getFluidTank(null);
        if (fluidHandler instanceof INBTSerializable<?> serializable) {
            CompoundTag fluidTag = ((INBTSerializable<CompoundTag>) serializable).serializeNBT(this.registryAccess());
            nbt.put("fluid_tank", fluidTag);
        }
    }

    @Override
    public @NotNull InteractionResult interact(Player player, InteractionHand hand) {
        ItemStack holdingItem = player.getItemInHand(hand);
        if (!holdingItem.isEmpty() && handlePlayerClickWithItem(player, hand, holdingItem, player.position())) {
            return InteractionResult.SUCCESS;
        }
        if (!player.isShiftKeyDown()) {
            for (PassengerSeat seat : this.seats) {
                if (seat.isFree()) {
                    player.startRiding(this);
                    return InteractionResult.SUCCESS;
                }
            }
        }
        if (canPlayerOpenGuiOrContainer(player)) {
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.PASS;
    }

    @Override
    public @NotNull Type getMinecartType() {
        return Type.RIDEABLE;
    }

    @Override
    protected boolean canAddPassenger(@NotNull Entity passenger) {
        return this.seats.stream().anyMatch(PassengerSeat::isFree);
    }

    public void ejectPassengers() {
        super.ejectPassengers();
        this.seats.forEach(seat -> seat.setCurrentUser(null));
    }

    @Nullable
    @Override
    public LivingEntity getControllingPassenger() {
        return this.seats.stream()
            .filter(PassengerSeat::isControllingSeat)
            .map(PassengerSeat::getCurrentUser)
            .filter(e -> e instanceof LivingEntity)
            .map(e -> (LivingEntity) e)
            .findFirst()
            .orElse(null);
    }

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
        return 0.0;
    }

    @Override
    public double getMaxReverseSpeed() {
        return 0.0;
    }

    @Override
    public double getMass() {
        return 1000.0;
    }

    @Override
    public boolean canLinkToAnotherRollingStock(AbstractRollingStock<?> other, @Nullable Player linker) {
        return false;
    }

    @Override
    public void linkToAnotherRollingStock(AbstractRollingStock<?> other, @Nullable Player linker) {
    }

    @Override
    public boolean canPlayerOpenGuiOrContainer(Player player) {
        if (this.restriction == EnumRestriction.PRIVATE || this.restriction == EnumRestriction.SEATS_ONLY) {
            if (this.owner == null || !player.getUUID().equals(this.owner)) {
                return false;
            }
        }
        return this.distanceToSqr(player) <= 64.0;
    }

    @Override
    public boolean handlePlayerClickWithItem(Player player, InteractionHand hand, ItemStack stack, Vec3 hitVector) {
        return false;
    }

    public void registerSkins(Map<String, net.minecraft.resources.ResourceLocation> skinMap) {
    }

    public void registerSeats(List<PassengerSeat> seatList) {
    }

    public void addAxes(List<Vec3> axisList) {
    }

    @Nullable
    public UUID getOwner() {
        return this.owner;
    }

    public A setOwner(UUID owner) {
        this.owner = owner;
        return self();
    }

    @Nullable
    public String getRollingStockName() {
        return this.name;
    }

    public A setRollingStockName(String name) {
        this.name = name;
        return self();
    }

    public EnumRestriction getRestriction() {
        return this.restriction;
    }

    public A setRestriction(EnumRestriction restriction) {
        this.restriction = restriction;
        return self();
    }

    public int getActiveSkinId() {
        return this.entityData.get(DATA_SKIN_ID);
    }

    public void setActiveSkin(int skinId) {
        this.entityData.set(DATA_SKIN_ID, skinId);
    }

    public int getNextSkinId() {
        int nextId = getActiveSkinId() + 1;
        if (this.skins.size() > nextId) {
            return nextId;
        }
        return 0;
    }

    public double getTravelDistance() {
        return this.travelDistance;
    }

    @Nullable
    public Map.Entry<String, net.minecraft.resources.ResourceLocation> getActiveSkin() {
        int id = getActiveSkinId();
        if (this.skins.size() > id) {
            return (Map.Entry<String, net.minecraft.resources.ResourceLocation>) this.skins.entrySet().toArray()[id];
        } else if (!this.skins.isEmpty()) {
            return (Map.Entry<String, net.minecraft.resources.ResourceLocation>) this.skins.entrySet().toArray()[0];
        }
        return null;
    }

    @Nullable
    public AbstractRollingStock<?> getNext() {
        return this.next;
    }

    @Nullable
    public AbstractRollingStock<?> getPrevious() {
        return this.previous;
    }

    public List<Vec3> getAxes() {
        return this.axes;
    }

    public List<PassengerSeat> getSeats() {
        return this.seats;
    }

    @SuppressWarnings("unchecked")
    private A self() {
        return (A) this;
    }

    public enum EnumRestriction {
        PUBLIC,
        SEATS_ONLY,
        PRIVATE
    }
}
