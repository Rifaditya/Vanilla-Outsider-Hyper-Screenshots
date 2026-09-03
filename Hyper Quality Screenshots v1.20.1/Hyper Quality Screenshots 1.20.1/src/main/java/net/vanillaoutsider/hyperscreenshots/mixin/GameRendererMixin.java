// Copyright (C) 2026 Dasik (Rifaditya) | GNU GPLv3
package net.vanillaoutsider.hyperscreenshots.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.vanillaoutsider.hyperscreenshots.config.HyperScreenshotsConfig;
import net.vanillaoutsider.hyperscreenshots.render.HyperCaptureManager;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameRenderer.class)
public abstract class GameRendererMixin {

    @Shadow @Final private Minecraft minecraft;

    @Inject(method = "renderItemInHand", at = @At("HEAD"), cancellable = true)
    private void onRenderItemInHand(CallbackInfo ci) {
        if (HyperCaptureManager.getInstance().isCapturing() && HyperScreenshotsConfig.get().autoHideHand) {
            ci.cancel();
        }
    }

    @Inject(
        method = "render",
        at = @At("TAIL")
    )
    private void onRenderTail(float tickDelta, long startTime, boolean tick, CallbackInfo ci) {
        HyperCaptureManager captureManager = HyperCaptureManager.getInstance();
        if (captureManager.isCapturePending()) {
            captureManager.executePendingCapture(this.minecraft, tickDelta);
        }
    }
}
