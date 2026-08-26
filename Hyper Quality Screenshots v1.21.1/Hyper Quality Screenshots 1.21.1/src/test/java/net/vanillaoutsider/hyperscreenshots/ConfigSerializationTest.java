// Copyright (C) 2026 Dasik (Rifaditya) | GNU GPLv3
package net.vanillaoutsider.hyperscreenshots;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.vanillaoutsider.hyperscreenshots.config.HyperScreenshotsConfig;
import net.vanillaoutsider.hyperscreenshots.config.ResolutionPreset;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ConfigSerializationTest {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    @Test
    @DisplayName("Assert default configuration has safe non-null values")
    void testDefaultConfig() {
        HyperScreenshotsConfig config = new HyperScreenshotsConfig();
        config.validate();

        assertEquals(ResolutionPreset.FOUR_K, config.resolutionPreset);
        assertEquals(2.0f, config.customMultiplier);
        assertFalse(config.autoHideHud);
        assertFalse(config.autoHideHand);
        assertTrue(config.instantMaxKeyEnabled);
        assertTrue(config.playSoundOnSuccess);
        assertTrue(config.hardwareTransparencyAlerts);
    }

    @Test
    @DisplayName("Assert validation corrects corrupted or out-of-bound values")
    void testValidationBounds() {
        HyperScreenshotsConfig config = new HyperScreenshotsConfig();
        config.resolutionPreset = null;
        config.customMultiplier = -5.0f;
        config.validate();

        assertEquals(ResolutionPreset.FOUR_K, config.resolutionPreset);
        assertEquals(1.0f, config.customMultiplier);

        config.customMultiplier = Float.NaN;
        config.validate();
        assertEquals(1.0f, config.customMultiplier);

        config.customMultiplier = 99.0f;
        config.validate();
        assertEquals(16.0f, config.customMultiplier);
    }

    @Test
    @DisplayName("Assert JSON roundtrip serialization matches POJO values")
    void testJsonSerializationRoundtrip() {
        HyperScreenshotsConfig original = new HyperScreenshotsConfig();
        original.resolutionPreset = ResolutionPreset.EIGHT_K;
        original.customMultiplier = 4.5f;
        original.autoHideHud = true;
        original.autoHideHand = true;

        String json = GSON.toJson(original);
        assertNotNull(json);

        HyperScreenshotsConfig deserialized = GSON.fromJson(json, HyperScreenshotsConfig.class);
        assertNotNull(deserialized);
        deserialized.validate();

        assertEquals(ResolutionPreset.EIGHT_K, deserialized.resolutionPreset);
        assertEquals(4.5f, deserialized.customMultiplier);
        assertTrue(deserialized.autoHideHud);
        assertTrue(deserialized.autoHideHand);
    }
}
