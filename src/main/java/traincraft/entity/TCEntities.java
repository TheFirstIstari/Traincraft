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

public class TCEntities {

    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(Registries.ENTITY_TYPE, Traincraft.MOD_ID);

    public static final DeferredHolder<EntityType<?>, EntityType<LocomotiveSteamSmall>> LOCOMOTIVE_STEAM_SMALL = ENTITIES.register("locomotive_steam_small",
        () -> EntityType.Builder.of(LocomotiveSteamSmall::new, MobCategory.MISC)
            .sized(0.98f, 0.98f)
            .clientTrackingRange(10)
            .build("locomotive_steam_small"));
}
