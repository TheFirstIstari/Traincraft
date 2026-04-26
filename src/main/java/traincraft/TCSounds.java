/*
 * Traincraft
 * Copyright (c) 2011-2024.
 */

package traincraft;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredHolder;

public class TCSounds {

    public static final DeferredRegister<SoundEvent> SOUND_EVENTS = DeferredRegister.create(Registries.SOUND_EVENT, Traincraft.MOD_ID);

    // Locomotive sounds
    public static final DeferredHolder<SoundEvent, SoundEvent> LOCOMOTIVE_STEAM_WHISTLE = 
        SOUND_EVENTS.register("locomotive.steam.whistle", () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(Traincraft.MOD_ID, "locomotive.steam.whistle")));
    
    public static final DeferredHolder<SoundEvent, SoundEvent> LOCOMOTIVE_STEAM_ENGINE = 
        SOUND_EVENTS.register("locomotive.steam.engine", () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(Traincraft.MOD_ID, "locomotive.steam.engine")));
    
    // Train movement sounds
    public static final DeferredHolder<SoundEvent, SoundEvent> TRAIN_WHEELS = 
        SOUND_EVENTS.register("train.wheels", () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(Traincraft.MOD_ID, "train.wheels")));
    
    // Assembly table sounds
    public static final DeferredHolder<SoundEvent, SoundEvent> ASSEMBLY_TABLE_CRAFT = 
        SOUND_EVENTS.register("assembly_table.craft", () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(Traincraft.MOD_ID, "assembly_table.craft")));
    
    // Distillery sounds
    public static final DeferredHolder<SoundEvent, SoundEvent> DISTILLERY_PROCESS = 
        SOUND_EVENTS.register("distillery.process", () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(Traincraft.MOD_ID, "distillery.process")));
    
    // Signal sounds
    public static final DeferredHolder<SoundEvent, SoundEvent> SIGNAL_ACTIVATE = 
        SOUND_EVENTS.register("signal.activate", () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(Traincraft.MOD_ID, "signal.activate")));

    // Diesel locomotive engine sound (loops while moving).
    public static final DeferredHolder<SoundEvent, SoundEvent> LOCOMOTIVE_DIESEL_ENGINE =
        SOUND_EVENTS.register("locomotive.diesel.engine", () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(Traincraft.MOD_ID, "locomotive.diesel.engine")));
}
