/*
 * Traincraft
 * Copyright (c) 2011-2024.
 *
 * This file ("Traincraft.java") is part of the Traincraft mod for Minecraft.
 * It is created by all people that are listed with @author below.
 * It is distributed under LGPL-v3.0.
 * You can find the source code at https://github.com/Traincraft/Traincraft
 */

package traincraft;

import net.neoforged.bus.api.Event;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.common.Mod.EventBusSubscriber;
import net.neoforged.fml.common.Mod.EventBusSubscriber.Bus;
import net.neoforged.fml.common.event.FMLConstructionEvent;
import net.neoforged.fml.common.event.FMLInitializationEvent;
import net.neoforged.fml.common.event.FMLPostInitializationEvent;
import net.neoforged.fml.common.event.FMLPreInitializationEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLLoadCompleteEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import traincraft.api.TraincraftAddonLoader;
import traincraft.blocks.TCBlocks;
import traincraft.entity.TCEntities;
import traincraft.items.TCItems;
import traincraft.liquids.TCLiquids;
import traincraft.network.TCNetwork;
import traincraft.world.OreGen;

import java.io.File;
import java.util.List;

@Mod(Traincraft.MOD_ID)
@EventBusSubscriber(modid = Traincraft.MOD_ID, bus = Bus.FORGE)
public class Traincraft {

    public static final String MOD_ID = "traincraft";
    public static final String MOD_NAME = "Traincraft";
    public static final String MOD_VERSION = "5.0.0-alpha1";
    public static final Logger LOGGER = LogManager.getLogger(MOD_NAME);

    public static Traincraft INSTANCE;
    public static File configDirectory;
    public static File gameDirectory;

    public static final IEventBus EVENT_BUS = null;

    public Traincraft() {
        INSTANCE = this;
    }

    public static void preInit(IEventBus bus) {
        LOGGER.info("Starting Traincraft " + MOD_VERSION + "!");

        configDirectory = new File("config/" + MOD_ID);

        TCEntities.register(bus);
        TCLiquids.register(bus);
        TCBlocks.register(bus);
        TCItems.register(bus);

        OreGen.register(bus);

        LOGGER.info("Pre-initialization complete");
    }

    public static void init(FMLCommonSetupEvent event) {
        LOGGER.info("Initializing Traincraft");

        TCNetwork.register();

        event.enqueueWork(() -> {
            File addonRoot = new File(gameDirectory, "traincraft");
            TraincraftAddonLoader.loadInternals(MOD_ID);
            TraincraftAddonLoader.loadFolders(addonRoot);
        });

        LOGGER.info("Initialization complete");
    }

    public static void postInit(FMLLoadCompleteEvent event) {
        LOGGER.info("Post-initialization complete");
    }

    @net.neoforged.bus.api.Subscribe
    public static void onCommonSetup(FMLCommonSetupEvent event) {
        init(event);
    }

    @net.neoforged.bus.api.Subscribe
    public static void onLoadComplete(FMLLoadCompleteEvent event) {
        postInit(event);
    }
}