// Copyright (C) 2026 Dasik (Rifaditya) | GNU GPLv3
package net.vanillaoutsider.hyperscreenshots.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class HyperScreenshotsConfig {
    private static final Logger LOGGER = LoggerFactory.getLogger(HyperScreenshotsConfig.class);
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    private static HyperScreenshotsConfig instance;

    // Configurable Settings
    public ResolutionPreset resolutionPreset = ResolutionPreset.FOUR_K;
    public float customMultiplier = 2.0f;
    public boolean autoHideHud = false;
    public boolean autoHideHand = false;
    public boolean instantMaxKeyEnabled = true;
    public boolean playSoundOnSuccess = true;
    public boolean hardwareTransparencyAlerts = true;

    public static Path getConfigPath() {
        try {
            if (FabricLoader.getInstance() != null && FabricLoader.getInstance().getConfigDir() != null) {
                return FabricLoader.getInstance().getConfigDir().resolve("hyper_screenshots.json");
            }
        } catch (Throwable ignored) {
            // Fallback for headless unit test environments
        }
        return Path.of("config", "hyper_screenshots.json");
    }

    public static synchronized HyperScreenshotsConfig get() {
        if (instance == null) {
            instance = load();
        }
        return instance;
    }

    public static HyperScreenshotsConfig load() {
        Path configFile = getConfigPath();
        if (Files.exists(configFile)) {
            try (BufferedReader reader = Files.newBufferedReader(configFile)) {
                HyperScreenshotsConfig config = GSON.fromJson(reader, HyperScreenshotsConfig.class);
                if (config != null) {
                    config.validate();
                    LOGGER.info("[Hyper Quality Screenshots] Loaded configuration from {}", configFile.getFileName());
                    return config;
                }
            } catch (Exception e) {
                LOGGER.error("[Hyper Quality Screenshots] Failed to load config from {}. Resetting to defaults.", configFile, e);
            }
        }

        HyperScreenshotsConfig defaultConfig = new HyperScreenshotsConfig();
        defaultConfig.save();
        return defaultConfig;
    }

    public synchronized void save() {
        validate();
        Path configFile = getConfigPath();
        try {
            if (configFile.getParent() != null) {
                Files.createDirectories(configFile.getParent());
            }
            try (BufferedWriter writer = Files.newBufferedWriter(configFile)) {
                GSON.toJson(this, writer);
                LOGGER.debug("[Hyper Quality Screenshots] Saved configuration to {}", configFile.getFileName());
            }
        } catch (IOException e) {
            LOGGER.error("[Hyper Quality Screenshots] Failed to save config to {}", configFile, e);
        }
    }

    public void validate() {
        if (resolutionPreset == null) {
            resolutionPreset = ResolutionPreset.FOUR_K;
        }
        if (customMultiplier < 1.0f || Float.isNaN(customMultiplier)) {
            customMultiplier = 1.0f;
        } else if (customMultiplier > 16.0f) {
            customMultiplier = 16.0f;
        }
    }

    public static synchronized void resetToDefaults() {
        instance = new HyperScreenshotsConfig();
        instance.save();
        LOGGER.info("[Hyper Quality Screenshots] Configuration reset to defaults.");
    }
}
