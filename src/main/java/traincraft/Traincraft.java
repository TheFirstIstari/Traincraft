/*
 * Traincraft
 * Copyright (c) 2011-2024.
 */

package traincraft;

import net.neoforged.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(Traincraft.MOD_ID)
public class Traincraft {

    public static final String MOD_ID = "traincraft";
    public static final String MOD_NAME = "Traincraft";
    public static final String MOD_VERSION = "5.0.0-alpha1";
    public static final Logger LOGGER = LogManager.getLogger(MOD_NAME);

    public Traincraft() {
        LOGGER.info("Traincraft " + MOD_VERSION + " loaded!");
    }
}