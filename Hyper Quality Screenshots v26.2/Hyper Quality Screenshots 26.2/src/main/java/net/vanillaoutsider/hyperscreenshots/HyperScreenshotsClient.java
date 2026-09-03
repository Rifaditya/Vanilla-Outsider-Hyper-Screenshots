// Copyright (C) 2026 Dasik (Rifaditya) | GNU GPLv3
package net.vanillaoutsider.hyperscreenshots;

import net.fabricmc.api.ClientModInitializer;
import net.vanillaoutsider.hyperscreenshots.config.HyperScreenshotsConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HyperScreenshotsClient implements ClientModInitializer {
    public static final String MOD_ID = "hyper-screenshots";
    public static final String MOD_NAME = "Hyper Quality Screenshots";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_NAME);

    @Override
    public void onInitializeClient() {
        LOGGER.info("[{}] Initializing client-side hyper quality screenshot engine...", MOD_NAME);

        // Preload client configuration
        HyperScreenshotsConfig config = HyperScreenshotsConfig.get();
        // Register client commands
        net.vanillaoutsider.hyperscreenshots.command.HyperScreenshotsCommand.register();
    }
}
