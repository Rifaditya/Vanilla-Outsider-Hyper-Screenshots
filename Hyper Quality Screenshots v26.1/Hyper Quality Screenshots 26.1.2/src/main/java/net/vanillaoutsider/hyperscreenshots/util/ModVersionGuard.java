package net.vanillaoutsider.hyperscreenshots.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class ModVersionGuard {
    private static final Logger LOGGER = LoggerFactory.getLogger(ModVersionGuard.class);

    private ModVersionGuard() {}

    public static void checkClass(String modName, String targetClassName) {
        try {
            Class.forName(targetClassName, false, Thread.currentThread().getContextClassLoader());
        } catch (ClassNotFoundException e) {
            String errorMsg = String.format(
                "[%s] CRITICAL: Incompatible Minecraft runtime detected! Missing expected class '%s'. "
                + "Please ensure you have installed the correct build matching your exact Minecraft version.",
                modName, targetClassName
            );
            LOGGER.error(errorMsg);
            throw new IllegalStateException(errorMsg, e);
        }
    }
}
