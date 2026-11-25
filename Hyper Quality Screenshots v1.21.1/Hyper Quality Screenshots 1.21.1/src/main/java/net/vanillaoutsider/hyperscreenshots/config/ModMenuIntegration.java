// Copyright (C) 2026 Dasik (Rifaditya) | GNU GPLv3
package net.vanillaoutsider.hyperscreenshots.config;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.api.FabricLoader;

@Environment(EnvType.CLIENT)
public class ModMenuIntegration implements ModMenuApi {

    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        try {
            if (FabricLoader.getInstance().isModLoaded("yet-another-config-lib")
                    || FabricLoader.getInstance().isModLoaded("yet_another_config_lib_v3")) {
                return YaclScreenHelper.createFactory();
            }
        } catch (Throwable ignored) {
            // Fallback gracefully without crash (absent YACL or headless test runtime)
        }
        return parent -> null;
    }
}
