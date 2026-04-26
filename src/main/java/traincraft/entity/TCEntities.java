/*
 * Traincraft
 * Copyright (c) 2011-2024.
 */

package traincraft.entity;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.minecraft.core.registries.Registries;
import traincraft.Traincraft;
import traincraft.api.AbstractRollingStock;
import traincraft.entity.rollingstock.EntityFreightCart;
import traincraft.entity.rollingstock.EntityPassengerCart;
import traincraft.entity.bogie.EntityBogie;
import traincraft.entity.LocomotiveSteamSmall;
import traincraft.entity.LocomotiveDieselSmall;

public class TCEntities {

    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(Registries.ENTITY_TYPE, Traincraft.MOD_ID);

    public static final DeferredHolder<EntityType<?>, EntityType<LocomotiveSteamSmall>> LOCOMOTIVE_STEAM_SMALL = ENTITIES.register("locomotive_steam_small",
        () -> EntityType.Builder.of(LocomotiveSteamSmall::new, MobCategory.MISC)
            .sized(0.98f, 0.98f)
            .clientTrackingRange(10)
            .build("locomotive_steam_small"));

    public static final DeferredHolder<EntityType<?>, EntityType<EntityFreightCart>> FREIGHT_CART = ENTITIES.register("freight_cart",
        () -> EntityType.Builder.of(EntityFreightCart::new, MobCategory.MISC)
            .sized(0.98f, 0.98f)
            .clientTrackingRange(10)
            .build("freight_cart"));

    public static final DeferredHolder<EntityType<?>, EntityType<EntityBogie>> BOGIE = ENTITIES.register("bogie",
        () -> EntityType.Builder.of(EntityBogie::new, MobCategory.MISC)
            .sized(0.98f, 0.98f)
            .clientTrackingRange(10)
            .build("bogie"));

    public static final DeferredHolder<EntityType<?>, EntityType<EntityPassengerCart>> PASSENGER_CART = ENTITIES.register("passenger_cart",
        () -> EntityType.Builder.of(EntityPassengerCart::new, MobCategory.MISC)
            .sized(0.98f, 0.98f)
            .clientTrackingRange(10)
            .build("passenger_cart"));

    public static final DeferredHolder<EntityType<?>, EntityType<LocomotiveDieselSmall>> LOCOMOTIVE_DIESEL_SMALL = ENTITIES.register("locomotive_diesel_small",
        () -> EntityType.Builder.of(LocomotiveDieselSmall::new, MobCategory.MISC)
            .sized(0.98f, 0.98f)
            .clientTrackingRange(10)
            .build("locomotive_diesel_small"));
}
