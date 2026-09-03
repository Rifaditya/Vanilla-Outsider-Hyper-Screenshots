// Copyright (C) 2026 Dasik (Rifaditya) | GNU GPLv3
package net.vanillaoutsider.hyperscreenshots.mixin;

import net.minecraft.client.KeyboardHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.network.chat.Component;
import net.vanillaoutsider.hyperscreenshots.config.HyperScreenshotsConfig;
import net.vanillaoutsider.hyperscreenshots.config.ResolutionPreset;
import net.vanillaoutsider.hyperscreenshots.render.HyperCaptureManager;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(KeyboardHandler.class)
public abstract class KeyboardMixin {
    private static final int KEY_F2 = 291;
    private static final int ACTION_PRESS = 1;
    private static final int MOD_ALT = 4;

    @Shadow
    @Final
    private Minecraft minecraft;

    @Inject(method = "keyPress", at = @At("HEAD"), cancellable = true)
    private void onKeyPress(long handle, int action, KeyEvent event, CallbackInfo ci) {
        if (action == ACTION_PRESS && event.key() == KEY_F2) {
            HyperScreenshotsConfig config = HyperScreenshotsConfig.get();
            boolean isCtrlDown = event.hasControlDownWithQuirk();
            boolean isAltDown = (event.modifiers() & MOD_ALT) != 0;

            if (isAltDown) {
                // Live Toggle: Auto-Hide Hand
                config.autoHideHand = !config.autoHideHand;
                config.save();
                Component feedback = Component.translatable(config.autoHideHand
                    ? "hyperscreenshots.notification.auto_hide_hand.enabled"
                    : "hyperscreenshots.notification.auto_hide_hand.disabled");
                if (this.minecraft.gui != null && this.minecraft.gui.hud != null) {
                    this.minecraft.gui.hud.getChat().addClientSystemMessage(feedback);
                }
                ci.cancel();
            } else if (isCtrlDown && config.instantMaxKeyEnabled) {
                // Instant 16K QUHD Max Screenshot
                if (!HyperCaptureManager.getInstance().isBusy()) {
                    HyperCaptureManager.getInstance().requestCapture(ResolutionPreset.SIXTEEN_K, true);
                }
                ci.cancel();
            } else if (config.resolutionPreset != ResolutionPreset.NORMAL) {
                // Active Supersampled Preset Screenshot
                if (!HyperCaptureManager.getInstance().isBusy()) {
                    HyperCaptureManager.getInstance().requestCapture(config.resolutionPreset, false);
                }
                ci.cancel();
            }
        }
    }
}
