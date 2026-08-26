package net.vanillaoutsider.hyperscreenshots;

import net.fabricmc.api.ClientModInitializer;
import net.vanillaoutsider.hyperscreenshots.util.ModVersionGuard;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HyperScreenshotsClient implements ClientModInitializer {
    public static final String MOD_ID = "hyper-screenshots";
    public static final String MOD_NAME = "Hyper Quality Screenshots";
    public static final Logger LOGGER = LoggerFactory.getLogger(HyperScreenshotsClient.class);

    @Override
    public void onInitializeClient() {
        ModVersionGuard.checkClass(MOD_NAME, "net.minecraft.world.entity.EntityTypes");
        LOGGER.info("[{}] Initialized client-side high-resolution screenshot engine.", MOD_NAME);
    }
}
