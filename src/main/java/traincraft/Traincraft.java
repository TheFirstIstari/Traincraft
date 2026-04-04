/*
 * Traincraft
 * Copyright (c) 2011-2024.
 */

package traincraft;

import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.minecraft.world.item.Item;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(Traincraft.MOD_ID)
public class Traincraft {

    public static final String MOD_ID = "traincraft";
    public static final String MOD_NAME = "Traincraft";
    public static final String MOD_VERSION = "5.0.0-alpha1";
    public static final Logger LOGGER = LogManager.getLogger(MOD_NAME);

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(
            net.minecraft.core.registries.Registries.ITEM, MOD_ID
    );

    public static Traincraft INSTANCE;

    public Traincraft() {
        INSTANCE = this;
        
        ITEMS.register("copper_ingot", () -> new Item(new Item.Properties()));
        ITEMS.register("steel_ingot", () -> new Item(new Item.Properties()));
        
        LOGGER.info("Traincraft " + MOD_VERSION + " loaded!");
    }
}