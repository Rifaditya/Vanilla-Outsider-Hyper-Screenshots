// Copyright (C) 2026 Dasik (Rifaditya) | GNU GPLv3
package net.vanillaoutsider.hyperscreenshots.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.network.chat.Component;
import net.vanillaoutsider.hyperscreenshots.config.HyperScreenshotsConfig;
import net.vanillaoutsider.hyperscreenshots.config.ResolutionPreset;
import net.vanillaoutsider.hyperscreenshots.render.HyperCaptureManager;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public final class HyperScreenshotsCommand {
    private static final List<String> PRESET_NAMES = Arrays.asList(
            "normal", "2k", "two_k", "4k", "four_k", "8k", "eight_k", "16k", "sixteen_k", "custom"
    );

    private static final List<String> TOGGLE_SETTINGS = Arrays.asList(
            "hud", "hand", "instantmax", "sound", "alerts"
    );

    private HyperScreenshotsCommand() {}

    public static void register() {
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> {
            registerCommands(dispatcher);
        });
    }

    public static void registerCommands(CommandDispatcher<FabricClientCommandSource> dispatcher) {
        dispatcher.register(buildTree("hyperscreenshots"));
        dispatcher.register(buildTree("hypershot"));
        dispatcher.register(buildTree("hqss"));
    }

    public static LiteralArgumentBuilder<FabricClientCommandSource> buildTree(String rootLiteral) {
        return ClientCommandManager.literal(rootLiteral)
                .executes(context -> {
                    showHelp(context.getSource());
                    return 1;
                })
                .then(ClientCommandManager.literal("help")
                        .executes(context -> {
                            showHelp(context.getSource());
                            return 1;
                        }))
                .then(ClientCommandManager.literal("status")
                        .executes(context -> {
                            showStatus(context.getSource());
                            return 1;
                        }))
                .then(ClientCommandManager.literal("preset")
                        .then(ClientCommandManager.argument("name", StringArgumentType.word())
                                .suggests((context, builder) -> {
                                    String remaining = builder.getRemaining().toLowerCase(Locale.ROOT);
                                    for (String p : PRESET_NAMES) {
                                        if (p.startsWith(remaining)) {
                                            builder.suggest(p);
                                        }
                                    }
                                    return builder.buildFuture();
                                })
                                .executes(context -> {
                                    String name = StringArgumentType.getString(context, "name");
                                    return setPreset(context.getSource(), name);
                                })))
                .then(ClientCommandManager.literal("capture")
                        .executes(context -> {
                            HyperScreenshotsConfig config = HyperScreenshotsConfig.get();
                            return triggerCapture(context.getSource(), config.resolutionPreset);
                        })
                        .then(ClientCommandManager.argument("name", StringArgumentType.word())
                                .suggests((context, builder) -> {
                                    String remaining = builder.getRemaining().toLowerCase(Locale.ROOT);
                                    for (String p : PRESET_NAMES) {
                                        if (p.startsWith(remaining)) {
                                            builder.suggest(p);
                                        }
                                    }
                                    return builder.buildFuture();
                                })
                                .executes(context -> {
                                    String name = StringArgumentType.getString(context, "name");
                                    ResolutionPreset preset = parsePreset(name);
                                    if (preset == null) {
                                        context.getSource().sendFeedback(Component.translatable("hyperscreenshots.command.invalid_preset", name));
                                        return 0;
                                    }
                                    return triggerCapture(context.getSource(), preset);
                                })))
                .then(ClientCommandManager.literal("toggle")
                        .then(ClientCommandManager.argument("setting", StringArgumentType.word())
                                .suggests((context, builder) -> {
                                    String remaining = builder.getRemaining().toLowerCase(Locale.ROOT);
                                    for (String s : TOGGLE_SETTINGS) {
                                        if (s.startsWith(remaining)) {
                                            builder.suggest(s);
                                        }
                                    }
                                    return builder.buildFuture();
                                })
                                .executes(context -> {
                                    String setting = StringArgumentType.getString(context, "setting");
                                    return toggleSetting(context.getSource(), setting);
                                })))
                .then(ClientCommandManager.literal("reload")
                        .executes(context -> {
                            HyperScreenshotsConfig.load();
                            context.getSource().sendFeedback(Component.translatable("hyperscreenshots.command.reloaded"));
                            return 1;
                        }));
    }

    public static ResolutionPreset parsePreset(String name) {
        if (name == null) return null;
        String lower = name.toLowerCase(Locale.ROOT);
        switch (lower) {
            case "normal":
                return ResolutionPreset.NORMAL;
            case "2k":
            case "two_k":
                return ResolutionPreset.TWO_K;
            case "4k":
            case "four_k":
                return ResolutionPreset.FOUR_K;
            case "8k":
            case "eight_k":
                return ResolutionPreset.EIGHT_K;
            case "16k":
            case "sixteen_k":
                return ResolutionPreset.SIXTEEN_K;
            case "custom":
                return ResolutionPreset.CUSTOM;
            default:
                return null;
        }
    }

    private static int setPreset(FabricClientCommandSource source, String name) {
        ResolutionPreset preset = parsePreset(name);
        if (preset == null) {
            source.sendFeedback(Component.translatable("hyperscreenshots.command.invalid_preset", name));
            return 0;
        }
        HyperScreenshotsConfig config = HyperScreenshotsConfig.get();
        config.resolutionPreset = preset;
        config.save();
        source.sendFeedback(Component.translatable("hyperscreenshots.command.preset_changed", preset.getDisplayName()));
        return 1;
    }

    private static int triggerCapture(FabricClientCommandSource source, ResolutionPreset preset) {
        if (HyperCaptureManager.getInstance().isBusy()) {
            source.sendFeedback(Component.translatable("hyperscreenshots.notification.busy"));
            return 0;
        }
        HyperCaptureManager.getInstance().requestCapture(preset, false);
        source.sendFeedback(Component.translatable("hyperscreenshots.command.capture_triggered", preset.getDisplayName()));
        return 1;
    }

    public static boolean applyToggle(HyperScreenshotsConfig config, String setting) {
        if (config == null || setting == null) return false;
        String lower = setting.toLowerCase(Locale.ROOT);
        switch (lower) {
            case "hud":
            case "autohidehud":
                config.autoHideHud = !config.autoHideHud;
                return true;
            case "hand":
            case "autohidehand":
                config.autoHideHand = !config.autoHideHand;
                return true;
            case "instantmax":
            case "instantmaxkeyenabled":
                config.instantMaxKeyEnabled = !config.instantMaxKeyEnabled;
                return true;
            case "sound":
            case "chime":
            case "playsoundonsuccess":
                config.playSoundOnSuccess = !config.playSoundOnSuccess;
                return true;
            case "alerts":
            case "hardwaretransparencyalerts":
                config.hardwareTransparencyAlerts = !config.hardwareTransparencyAlerts;
                return true;
            default:
                return false;
        }
    }

    public static int toggleSetting(FabricClientCommandSource source, String setting) {
        HyperScreenshotsConfig config = HyperScreenshotsConfig.get();
        boolean toggled = applyToggle(config, setting);
        if (!toggled) {
            source.sendFeedback(Component.translatable("hyperscreenshots.command.invalid_toggle", setting));
            return 0;
        }

        String lower = setting.toLowerCase(Locale.ROOT);
        String label = lower.contains("hud") ? "Auto-Hide HUD"
                : lower.contains("hand") ? "Auto-Hide Hand"
                : lower.contains("instant") ? "Instant Max 16K (Ctrl+F2)"
                : lower.contains("sound") || lower.contains("chime") ? "Success Sound Chime"
                : "Hardware Transparency Alerts";

        boolean state = lower.contains("hud") ? config.autoHideHud
                : lower.contains("hand") ? config.autoHideHand
                : lower.contains("instant") ? config.instantMaxKeyEnabled
                : lower.contains("sound") || lower.contains("chime") ? config.playSoundOnSuccess
                : config.hardwareTransparencyAlerts;

        config.save();
        source.sendFeedback(Component.translatable("hyperscreenshots.command.toggle_changed", label, (state ? "§aEnabled§r" : "§cDisabled§r")));
        return 1;
    }

    private static void showHelp(FabricClientCommandSource source) {
        source.sendFeedback(Component.literal(
                "§6=== Hyper Quality Screenshots ===§r\n" +
                "§e/hyperscreenshots status§r - Show current resolution & settings\n" +
                "§e/hyperscreenshots preset <name>§r - Switch active preset (normal, 2k, 4k, 8k, 16k, custom)\n" +
                "§e/hyperscreenshots capture [preset]§r - Trigger capture immediately\n" +
                "§e/hyperscreenshots toggle <setting>§r - Toggle HUD, hand, chime, or alert settings\n" +
                "§e/hyperscreenshots reload§r - Reload config from disk\n" +
                "§7Hotkeys: §fF2§7 (Capture) | §fCtrl+F2§7 (Instant 16K) | §fAlt+F2§7 (Toggle Hand)"
        ));
    }

    private static void showStatus(FabricClientCommandSource source) {
        HyperScreenshotsConfig config = HyperScreenshotsConfig.get();
        source.sendFeedback(Component.literal(
                "§6=== Hyper Quality Screenshots: Status ===§r\n" +
                "§7Active Preset: §f" + config.resolutionPreset.getDisplayName() + "\n" +
                "§7Custom Multiplier: §f" + config.customMultiplier + "x\n" +
                "§7Auto-Hide HUD: §f" + (config.autoHideHud ? "§aEnabled" : "§cDisabled") + "\n" +
                "§7Auto-Hide Hand: §f" + (config.autoHideHand ? "§aEnabled" : "§cDisabled") + "\n" +
                "§7Instant 16K (Ctrl+F2): §f" + (config.instantMaxKeyEnabled ? "§aEnabled" : "§cDisabled") + "\n" +
                "§7Success Chime: §f" + (config.playSoundOnSuccess ? "§aEnabled" : "§cDisabled") + "\n" +
                "§7Hardware Transparency Alerts: §f" + (config.hardwareTransparencyAlerts ? "§aEnabled" : "§cDisabled")
        ));
    }
}
