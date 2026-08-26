// Copyright (C) 2026 Dasik (Rifaditya) | GNU GPLv3
package net.vanillaoutsider.hyperscreenshots.config;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import dev.isxander.yacl3.api.ConfigCategory;
import dev.isxander.yacl3.api.Option;
import dev.isxander.yacl3.api.OptionDescription;
import dev.isxander.yacl3.api.YetAnotherConfigLib;
import dev.isxander.yacl3.api.controller.EnumDropdownControllerBuilder;
import dev.isxander.yacl3.api.controller.FloatSliderControllerBuilder;
import dev.isxander.yacl3.api.controller.TickBoxControllerBuilder;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public final class YaclScreenHelper {

    private YaclScreenHelper() {}

    public static ConfigScreenFactory<?> createFactory() {
        return YaclScreenHelper::createScreen;
    }

    public static Screen createScreen(Screen parent) {
        HyperScreenshotsConfig config = HyperScreenshotsConfig.get();

        return YetAnotherConfigLib.createBuilder()
            .title(Component.translatable("config.hyperscreenshots.title"))
            .save(config::save)

            // === 1. RESOLUTION & SCALING ===
            .category(ConfigCategory.createBuilder()
                .name(Component.translatable("config.hyperscreenshots.category.resolution"))
                .tooltip(Component.translatable("config.hyperscreenshots.category.resolution.tooltip"))

                // Preset Dropdown
                .option(Option.<ResolutionPreset>createBuilder()
                    .name(Component.translatable("config.hyperscreenshots.preset"))
                    .description(OptionDescription.of(Component.translatable("config.hyperscreenshots.preset.desc")))
                    .binding(ResolutionPreset.FOUR_K, () -> config.resolutionPreset, val -> config.resolutionPreset = val)
                    .controller(EnumDropdownControllerBuilder::create)
                    .build())

                // Custom Multiplier Slider
                .option(Option.<Float>createBuilder()
                    .name(Component.translatable("config.hyperscreenshots.customMultiplier"))
                    .description(OptionDescription.of(Component.translatable("config.hyperscreenshots.customMultiplier.desc")))
                    .binding(2.0f, () -> config.customMultiplier, val -> config.customMultiplier = val)
                    .controller(opt -> FloatSliderControllerBuilder.create(opt).range(1.0f, 16.0f).step(0.5f))
                    .build())
                .build())

            // === 2. CAPTURE & INTERFACE ===
            .category(ConfigCategory.createBuilder()
                .name(Component.translatable("config.hyperscreenshots.category.capture"))
                .tooltip(Component.translatable("config.hyperscreenshots.category.capture.tooltip"))

                // Auto-Hide HUD
                .option(Option.<Boolean>createBuilder()
                    .name(Component.translatable("config.hyperscreenshots.autoHideHud"))
                    .description(OptionDescription.of(Component.translatable("config.hyperscreenshots.autoHideHud.desc")))
                    .binding(false, () -> config.autoHideHud, val -> config.autoHideHud = val)
                    .controller(TickBoxControllerBuilder::create)
                    .build())

                // Auto-Hide Hand
                .option(Option.<Boolean>createBuilder()
                    .name(Component.translatable("config.hyperscreenshots.autoHideHand"))
                    .description(OptionDescription.of(Component.translatable("config.hyperscreenshots.autoHideHand.desc")))
                    .binding(false, () -> config.autoHideHand, val -> config.autoHideHand = val)
                    .controller(TickBoxControllerBuilder::create)
                    .build())

                // Instant Max Keybind (Ctrl + F2)
                .option(Option.<Boolean>createBuilder()
                    .name(Component.translatable("config.hyperscreenshots.instantMaxKeyEnabled"))
                    .description(OptionDescription.of(Component.translatable("config.hyperscreenshots.instantMaxKeyEnabled.desc")))
                    .binding(true, () -> config.instantMaxKeyEnabled, val -> config.instantMaxKeyEnabled = val)
                    .controller(TickBoxControllerBuilder::create)
                    .build())
                .build())

            // === 3. FEEDBACK & DIAGNOSTICS ===
            .category(ConfigCategory.createBuilder()
                .name(Component.translatable("config.hyperscreenshots.category.feedback"))
                .tooltip(Component.translatable("config.hyperscreenshots.category.feedback.tooltip"))

                // Audio Chime
                .option(Option.<Boolean>createBuilder()
                    .name(Component.translatable("config.hyperscreenshots.playSoundOnSuccess"))
                    .description(OptionDescription.of(Component.translatable("config.hyperscreenshots.playSoundOnSuccess.desc")))
                    .binding(true, () -> config.playSoundOnSuccess, val -> config.playSoundOnSuccess = val)
                    .controller(TickBoxControllerBuilder::create)
                    .build())

                // Hardware Transparency Alerts
                .option(Option.<Boolean>createBuilder()
                    .name(Component.translatable("config.hyperscreenshots.hardwareTransparencyAlerts"))
                    .description(OptionDescription.of(Component.translatable("config.hyperscreenshots.hardwareTransparencyAlerts.desc")))
                    .binding(true, () -> config.hardwareTransparencyAlerts, val -> config.hardwareTransparencyAlerts = val)
                    .controller(TickBoxControllerBuilder::create)
                    .build())
                .build())

            .build()
            .generateScreen(parent);
    }
}
