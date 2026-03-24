package com.meteorui;

import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MeteorGui implements ModInitializer {
    public static final String MOD_ID = "meteor-gui";
    public static final Logger LOGGER = LoggerFactory.getLogger("MeteorGUI");

    @Override
    public void onInitialize() {
        LOGGER.info("[MeteorGUI] Initializing...");
    }
}
