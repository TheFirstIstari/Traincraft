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

    /**
     * Returns the speed multiplier of the rail block the rolling stock is currently sitting on.
     * Vanilla rails return 1.0; Traincraft rails return their configured multiplier.
     */
    private double getCurrentRailSpeedMultiplier() {
        // BaseRailBlock places its rail state on the same level as the entity's blockPosition.
        net.minecraft.world.level.block.state.BlockState state = this.level().getBlockState(this.blockPosition());
        if (state.getBlock() instanceof traincraft.blocks.rail.BlockTCRail rail) {
            return rail.getSpeedMultiplier();
        }
        // Some rails sit one block below the entity's bounding box origin.
        net.minecraft.world.level.block.state.BlockState below = this.level().getBlockState(this.blockPosition().below());
        if (below.getBlock() instanceof traincraft.blocks.rail.BlockTCRail rail) {
            return rail.getSpeedMultiplier();
        }
        return 1.0;
    }

    /** Desired separation between two coupled rolling stock entities, in blocks. */
    public static final double COUPLING_DISTANCE = 1.6;
    /** Spring stiffness applied per tick to correct coupling drift. Tune for stability. */
    public static final double COUPLING_STIFFNESS = 0.3;

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
            // Push a status line to the driver every half-second so they can see fuel/water levels.
            if (this.tickCount % 10 == 0 && controller instanceof Player p) {
                net.minecraft.network.chat.Component status = statusForDriver();
                if (status != null) {
                    p.displayClientMessage(status, true);
                }
            }
        }

        resolvePendingLinks();
        applyCouplingPhysics();
    }

    /**
     * Restore the next/previous link references from UUIDs persisted in NBT once both endpoints
     * have been loaded by the level. Runs as part of the regular tick so the resolution defers
     * naturally until both entities exist.
     */
    private void resolvePendingLinks() {
        if (pendingNextUuid != null && this.level() instanceof net.minecraft.server.level.ServerLevel sl) {
            net.minecraft.world.entity.Entity e = sl.getEntity(pendingNextUuid);
            if (e instanceof AbstractRollingStock<?> rs) {
                this.next = rs;
                pendingNextUuid = null;
            }
        }
        if (pendingPreviousUuid != null && this.level() instanceof net.minecraft.server.level.ServerLevel sl) {
            net.minecraft.world.entity.Entity e = sl.getEntity(pendingPreviousUuid);
            if (e instanceof AbstractRollingStock<?> rs) {
                this.previous = rs;
                pendingPreviousUuid = null;
            }
        }
    }

    /**
     * Propagate motion between coupled rolling stock entities by applying a spring force toward
     * the desired follow distance behind the leader. Each cart in a chain only handles its
     * upstream neighbour (the one stored in {@link #previous}); the downstream neighbour will run
     * the same logic in its own tick.
     */
    private void applyCouplingPhysics() {
        AbstractRollingStock<?> leader = this.previous;
        if (leader == null || leader.isRemoved() || leader.level() != this.level()) return;

        // Don't couple if the leader is more than a few blocks away — the link is broken in practice.
        Vec3 toLeader = leader.position().subtract(this.position());
        double dist = toLeader.length();
        if (dist < 1e-3 || dist > 6.0) return;

        Vec3 dir = toLeader.normalize();
        double error = dist - COUPLING_DISTANCE;

        // Spring correction: positive error pulls follower toward leader; negative pushes apart.
        // Apply mostly in the horizontal plane to preserve gravity behaviour.
        Vec3 correction = new Vec3(dir.x, 0, dir.z).scale(error * COUPLING_STIFFNESS);
        Vec3 motion = getDeltaMovement();
        setDeltaMovement(motion.x + correction.x, motion.y, motion.z + correction.z);
    }

    private void applyCustomPhysics(LivingEntity controller) {
        // Stock speed is in blocks/sec; convert to per-tick. Apply a rail-based bonus when the
        // train is currently riding on one of our custom rail blocks.
        double railBonus = getCurrentRailSpeedMultiplier();
        double maxSpeed = getMaxSpeed() / 20.0 * railBonus;
        double maxReverse = getMaxReverseSpeed() / 20.0 * railBonus;
        double accel = getAcceleration() / 400.0 * railBonus;   // gentler than per-tick raw acceleration
        double brake = getBreakPower() / 20.0;

        Vec3 motion = getDeltaMovement();
        // Throttle input: positive forward (W), negative reverse (S).
        float throttle = controller.zza;
        // Reject acceleration if the rolling stock has its own fuel/power model and is currently dry.
        // Braking and friction still apply so a runaway cart can be stopped without fuel.
        if (throttle != 0f && !canApplyThrottle()) {
            throttle = 0f;
        }
        // Strafing (A/D) is reserved for switching/horn etc.; ignore for now.

        // Determine the train's forward direction in world-space using the controller yaw,
        // so the player steers along their look direction. The minecart-style track logic
        // already constrains motion to rails when on rails, so this is a no-op when locked.
        float yawRad = (float) Math.toRadians(controller.getYRot());
        Vec3 forward = new Vec3(-Math.sin(yawRad), 0, Math.cos(yawRad));

        // Forward-direction scalar speed (positive = same dir as facing).
        double forwardSpeed = motion.x * forward.x + motion.z * forward.z;

        if (throttle > 0.01f) {
            // Accelerate forward.
            forwardSpeed = Math.min(forwardSpeed + accel * throttle, maxSpeed);
        } else if (throttle < -0.01f) {
            // If still rolling forward, brake first; once stopped, accelerate in reverse.
            if (forwardSpeed > 0) {
                forwardSpeed = Math.max(forwardSpeed - brake / 20.0, 0);
            } else {
                forwardSpeed = Math.max(forwardSpeed - accel * -throttle, -maxReverse);
            }
        } else {
            // Coasting friction.
            double friction = 0.99;
            forwardSpeed *= friction;
            if (Math.abs(forwardSpeed) < 0.001) forwardSpeed = 0;
        }

        // Compose the new horizontal motion from the chosen forward speed.
        double vx = forward.x * forwardSpeed;
        double vz = forward.z * forwardSpeed;
        setDeltaMovement(vx, motion.y, vz);
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

    @Nullable
    private UUID pendingNextUuid;
    @Nullable
    private UUID pendingPreviousUuid;

    protected void readFromNBT(CompoundTag nbt, SyncState state) {
        if (nbt.hasUUID("owner")) {
            this.owner = nbt.getUUID("owner");
        }
        if (nbt.hasUUID("link_next")) this.pendingNextUuid = nbt.getUUID("link_next");
        if (nbt.hasUUID("link_previous")) this.pendingPreviousUuid = nbt.getUUID("link_previous");
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
        if (this.next != null) nbt.putUUID("link_next", this.next.getUUID());
        if (this.previous != null) nbt.putUUID("link_previous", this.previous.getUUID());
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
                    if (player.startRiding(this)) {
                        seat.setCurrentUser(player);
                        return InteractionResult.SUCCESS;
                    }
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

    /**
     * Whether this rolling stock currently has the fuel/power required to accelerate.
     * Default rolling stock without an internal fuel model (carts, bogies) always returns true,
     * so they coast freely when pushed by a powered locomotive.
     */
    public boolean canApplyThrottle() {
        return true;
    }

    /**
     * Status line shown above the driver's hotbar (e.g. fuel/water levels). Returning null
     * suppresses the message for stock that has nothing meaningful to report.
     */
    @Nullable
    public net.minecraft.network.chat.Component statusForDriver() {
        return null;
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
        // Common interaction: a connector item links two rolling stock entities together.
        if (stack.getItem() instanceof traincraft.items.ItemConnector) {
            traincraft.items.ItemConnector.handleEntityClick(this, player, hand, stack);
            return true;
        }
        return false;
    }

    public void registerSkins(Map<String, net.minecraft.resources.ResourceLocation> skinMap) {
        this.skins.putAll(skinMap);
    }

    public void registerSeats(List<PassengerSeat> seatList) {
        this.seats.addAll(seatList);
    }

    public void addAxes(List<Vec3> axisList) {
        this.axes.addAll(axisList);
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

    public void setNextRollingStock(@Nullable AbstractRollingStock<?> next) {
        this.next = next;
    }

    public void setPreviousRollingStock(@Nullable AbstractRollingStock<?> previous) {
        this.previous = previous;
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
