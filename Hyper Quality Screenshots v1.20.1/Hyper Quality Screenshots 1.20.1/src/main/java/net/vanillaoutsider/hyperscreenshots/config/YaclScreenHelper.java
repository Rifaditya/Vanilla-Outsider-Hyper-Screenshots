// Copyright (C) 2026 Dasik (Rifaditya) | GNU GPLv3
package net.vanillaoutsider.hyperscreenshots.config;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import dev.isxander.yacl3.api.ConfigCategory;
import dev.isxander.yacl3.api.Option;
import dev.isxander.yacl3.api.OptionDescription;
import dev.isxander.yacl3.api.OptionGroup;
import dev.isxander.yacl3.api.YetAnotherConfigLib;
import dev.isxander.yacl3.api.controller.BooleanControllerBuilder;
import dev.isxander.yacl3.api.controller.EnumControllerBuilder;
import dev.isxander.yacl3.api.controller.FloatFieldControllerBuilder;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

/**
 * Isolated Screen Helper for YetAnotherConfigLib v3.
 * Referenced strictly via reflection from ModMenuIntegration to guarantee zero server classloading issues.
 */
public final class YaclScreenHelper {

    private YaclScreenHelper() {}

    public static ConfigScreenFactory<?> createScreen() {
        return parent -> buildScreen(parent);
    }

    private static Screen buildScreen(Screen parent) {
        HyperScreenshotsConfig config = HyperScreenshotsConfig.get();

        return YetAnotherConfigLib.createBuilder()
                .title(Component.translatable("config.hyperscreenshots.title"))
                .save(config::save)
                // --- CATEGORY 1: RESOLUTION & CAPTURE ---
                .category(ConfigCategory.createBuilder()
                        .name(Component.translatable("config.hyperscreenshots.category.capture"))
                        .group(OptionGroup.createBuilder()
                                .name(Component.translatable("config.hyperscreenshots.group.presets"))
                                .option(Option.<ResolutionPreset>createBuilder()
                                        .name(Component.translatable("config.hyperscreenshots.resolutionPreset"))
                                        .description(OptionDescription.of(Component.translatable("config.hyperscreenshots.resolutionPreset.desc")))
                                        .binding(
                                                ResolutionPreset.FOUR_K,
                                                () -> config.resolutionPreset,
                                                val -> config.resolutionPreset = val
                                        )
                                        .controller(opt -> EnumControllerBuilder.create(opt).enumClass(ResolutionPreset.class))
                                        .build())
                                .option(Option.<Float>createBuilder()
                                        .name(Component.translatable("config.hyperscreenshots.customMultiplier"))
                                        .description(OptionDescription.of(Component.translatable("config.hyperscreenshots.customMultiplier.desc")))
                                        .binding(
                                                2.0f,
                                                () -> config.customMultiplier,
                                                val -> config.customMultiplier = val
                                        )
                                        .controller(opt -> FloatFieldControllerBuilder.create(opt).min(1.0f).max(16.0f))
                                        .build())
                                .build())
                        .group(OptionGroup.createBuilder()
                                .name(Component.translatable("config.hyperscreenshots.group.behavior"))
                                .option(Option.<Boolean>createBuilder()
                                        .name(Component.translatable("config.hyperscreenshots.autoHideHud"))
                                        .description(OptionDescription.of(Component.translatable("config.hyperscreenshots.autoHideHud.desc")))
                                        .binding(
                                                false,
                                                () -> config.autoHideHud,
                                                val -> config.autoHideHud = val
                                        )
                                        .controller(BooleanControllerBuilder::create)
                                        .build())
                                .option(Option.<Boolean>createBuilder()
                                        .name(Component.translatable("config.hyperscreenshots.autoHideHand"))
                                        .description(OptionDescription.of(Component.translatable("config.hyperscreenshots.autoHideHand.desc")))
                                        .binding(
                                                false,
                                                () -> config.autoHideHand,
                                                val -> config.autoHideHand = val
                                        )
                                        .controller(BooleanControllerBuilder::create)
                                        .build())
                                .option(Option.<Boolean>createBuilder()
                                        .name(Component.translatable("config.hyperscreenshots.instantMaxKeyEnabled"))
                                        .description(OptionDescription.of(Component.translatable("config.hyperscreenshots.instantMaxKeyEnabled.desc")))
                                        .binding(
                                                true,
                                                () -> config.instantMaxKeyEnabled,
                                                val -> config.instantMaxKeyEnabled = val
                                        )
                                        .controller(BooleanControllerBuilder::create)
                                        .build())
                                .build())
                        .build())
                // --- CATEGORY 2: NOTIFICATIONS & FEEDBACK ---
                .category(ConfigCategory.createBuilder()
                        .name(Component.translatable("config.hyperscreenshots.category.feedback"))
                        .group(OptionGroup.createBuilder()
                                .name(Component.translatable("config.hyperscreenshots.group.alerts"))
                                .option(Option.<Boolean>createBuilder()
                                        .name(Component.translatable("config.hyperscreenshots.playSoundOnSuccess"))
                                        .description(OptionDescription.of(Component.translatable("config.hyperscreenshots.playSoundOnSuccess.desc")))
                                        .binding(
                                                true,
                                                () -> config.playSoundOnSuccess,
                                                val -> config.playSoundOnSuccess = val
                                        )
                                        .controller(BooleanControllerBuilder::create)
                                        .build())
                                .option(Option.<Boolean>createBuilder()
                                        .name(Component.translatable("config.hyperscreenshots.hardwareTransparencyAlerts"))
                                        .description(OptionDescription.of(Component.translatable("config.hyperscreenshots.hardwareTransparencyAlerts.desc")))
                                        .binding(
                                                true,
                                                () -> config.hardwareTransparencyAlerts,
                                                val -> config.hardwareTransparencyAlerts = val
                                        )
                                        .controller(BooleanControllerBuilder::create)
                                        .build())
                                .build())
                        .build())
                .build()
                .generateScreen(parent);
    }
}
