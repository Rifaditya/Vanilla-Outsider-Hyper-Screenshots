// Copyright (C) 2026 Dasik (Rifaditya) | GNU GPLv3
package net.vanillaoutsider.hyperscreenshots.mixin;

import net.minecraft.client.KeyboardHandler;
import net.minecraft.client.input.KeyEvent;
import net.vanillaoutsider.hyperscreenshots.config.HyperScreenshotsConfig;
import net.vanillaoutsider.hyperscreenshots.config.ResolutionPreset;
import net.vanillaoutsider.hyperscreenshots.render.HyperCaptureManager;
import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(KeyboardHandler.class)
public abstract class KeyboardMixin {

    @Inject(method = "keyPress", at = @At("HEAD"), cancellable = true)
    private void onKeyPress(long handle, int action, KeyEvent event, CallbackInfo ci) {
        if (action == GLFW.GLFW_PRESS && event.key() == GLFW.GLFW_KEY_F2) {
            HyperScreenshotsConfig config = HyperScreenshotsConfig.get();
            boolean isCtrlDown = event.hasControlDownWithQuirk();

            if (isCtrlDown && config.instantMaxKeyEnabled) {
                // Instant 16K QUHD Max Screenshot
                HyperCaptureManager.getInstance().requestCapture(ResolutionPreset.SIXTEEN_K, true);
                ci.cancel();
            } else if (config.resolutionPreset != ResolutionPreset.NORMAL) {
                // Active Supersampled Preset Screenshot
                HyperCaptureManager.getInstance().requestCapture(config.resolutionPreset, false);
                ci.cancel();
            }
        }
    }
}
