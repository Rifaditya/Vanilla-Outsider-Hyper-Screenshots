// Copyright (C) 2026 Dasik (Rifaditya) | GNU GPLv3
package net.vanillaoutsider.hyperscreenshots.render;

import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.platform.NativeImage;
import com.mojang.blaze3d.platform.Window;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Screenshot;
import net.minecraft.network.chat.Component;
import net.vanillaoutsider.hyperscreenshots.config.HyperScreenshotsConfig;
import net.vanillaoutsider.hyperscreenshots.config.ResolutionPreset;
import net.vanillaoutsider.hyperscreenshots.io.AsyncScreenshotWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class HyperCaptureManager {
    private static final Logger LOGGER = LoggerFactory.getLogger(HyperCaptureManager.class);
    private static final HyperCaptureManager INSTANCE = new HyperCaptureManager();

    private static final long CAPTURE_COOLDOWN_MS = 300L;
    private long lastCaptureTimeMs = 0L;
    private final OffscreenFramebuffer offscreenFramebuffer = new OffscreenFramebuffer();
    private boolean captureRequested = false;
    private boolean capturing = false;
    private ResolutionPreset activePreset = ResolutionPreset.FOUR_K;
    private boolean isInstantMax = false;
    private boolean previousHudHidden = false;

    private HyperCaptureManager() {}

    public static HyperCaptureManager getInstance() {
        return INSTANCE;
    }

    public boolean isCapturing() {
        return capturing;
    }

    public boolean isBusy() {
        return capturing || captureRequested || (System.currentTimeMillis() - lastCaptureTimeMs < CAPTURE_COOLDOWN_MS);
    }

    public boolean requestCapture(ResolutionPreset preset, boolean instantMax) {
        if (isBusy()) {
            LOGGER.debug("[Hyper Quality Screenshots] Capture request ignored: capture busy or debounced");
            return false;
        }
        this.lastCaptureTimeMs = System.currentTimeMillis();
        this.activePreset = (preset != null) ? preset : HyperScreenshotsConfig.get().resolutionPreset;
        this.isInstantMax = instantMax;
        this.captureRequested = true;
        LOGGER.debug("[Hyper Quality Screenshots] Capture requested for preset: {} (Instant Max: {})", activePreset, isInstantMax);
        return true;
    }

    public void resetForTesting() {
        this.capturing = false;
        this.captureRequested = false;
        this.lastCaptureTimeMs = 0L;
    }

    public boolean isCapturePending() {
        return captureRequested;
    }

    public void executePendingCapture(Minecraft minecraft, DeltaTracker deltaTracker) {
        if (!captureRequested || minecraft.level == null) {
            captureRequested = false;
            return;
        }

        this.captureRequested = false;
        this.capturing = true;

        HyperScreenshotsConfig config = HyperScreenshotsConfig.get();
        ResolutionPreset preset = isInstantMax ? ResolutionPreset.SIXTEEN_K : activePreset;

        Window window = minecraft.getWindow();
        int originalWidth = window.getWidth();
        int originalHeight = window.getHeight();
        double originalGuiScale = window.getGuiScale();
        ResolutionPreset.ResolutionDimensions dimensions = preset.calculateDimensions(originalWidth, originalHeight, config.customMultiplier);

        // Hardware Bounds Query & Aspect-Preserving Clamp
        int maxTextureSize = getMaxSupportedTextureSize();
        ResolutionPreset.ResolutionDimensions effectiveDimensions = clampToHardwareBounds(dimensions, maxTextureSize, config, minecraft);

        LOGGER.info("[Hyper Quality Screenshots] Executing {} capture ({}x{}) with proportional UI scale", preset.getDisplayName(), effectiveDimensions.width(), effectiveDimensions.height());

        // Handle auto-hide HUD if configured
        if (config.autoHideHud) {
            this.previousHudHidden = minecraft.options.hideGui;
            minecraft.options.hideGui = true;
        }

        RenderTarget target = minecraft.getMainRenderTarget();
        boolean targetResized = false;

        try {
            if (preset == ResolutionPreset.NORMAL) {
                if (config.autoHideHud || config.autoHideHand) {
                    minecraft.gameRenderer.render(deltaTracker, true);
                }
                // Capture directly from main render target
                NativeImage image = Screenshot.takeScreenshot(target);
                restoreCaptureState(minecraft, config);
                AsyncScreenshotWriter.dispatchSave(image, preset, dimensions.width(), dimensions.height());
            } else {
                // Supersampled resolution render pass (World + UI / Screens)
                ResolutionPreset.TileGrid grid = ResolutionPreset.getSuggestedTileGrid(effectiveDimensions.width(), effectiveDimensions.height());
                
                if (grid.getTotalTiles() > 1 && config.hardwareTransparencyAlerts) {
                    Component tilingMsg = Component.translatable("hyperscreenshots.notification.tiling_active", grid.columns(), grid.rows());
                    sendSystemChatMessage(minecraft, tilingMsg);
                }

                float scaleFactor = (float) effectiveDimensions.width() / (float) Math.max(1, originalWidth);
                int targetGuiScale = (int) Math.max(1, Math.round(originalGuiScale * scaleFactor));

                window.setWidth(effectiveDimensions.width());
                window.setHeight(effectiveDimensions.height());
                window.setGuiScale(targetGuiScale);
                target.resize(effectiveDimensions.width(), effectiveDimensions.height(), Minecraft.ON_OSX);
                targetResized = true;

                minecraft.gameRenderer.render(deltaTracker, true);

                NativeImage image = Screenshot.takeScreenshot(target);

                window.setWidth(originalWidth);
                window.setHeight(originalHeight);
                window.setGuiScale(originalGuiScale);
                target.resize(originalWidth, originalHeight, Minecraft.ON_OSX);
                targetResized = false;
                restoreCaptureState(minecraft, config);

                AsyncScreenshotWriter.dispatchSave(image, preset, effectiveDimensions.width(), effectiveDimensions.height());
            }
        } catch (Exception e) {
            LOGGER.error("[Hyper Quality Screenshots] Exception during screenshot capture pass", e);
            if (targetResized) {
                window.setWidth(originalWidth);
                window.setHeight(originalHeight);
                window.setGuiScale(originalGuiScale);
                target.resize(originalWidth, originalHeight, Minecraft.ON_OSX);
            }
            restoreCaptureState(minecraft, config);
            Component errorMsg = Component.translatable("hyperscreenshots.notification.error", e.getMessage());
            sendSystemChatMessage(minecraft, errorMsg);
        }
    }

    public static int getMaxSupportedTextureSize() {
        try {
            int max = RenderSystem.maxSupportedTextureSize();
            return max > 0 ? max : 8192;
        } catch (Throwable t) {
            return 8192;
        }
    }

    public static ResolutionPreset.ResolutionDimensions clampToHardwareBounds(
            ResolutionPreset.ResolutionDimensions dimensions,
            int maxTextureSize,
            HyperScreenshotsConfig config,
            Minecraft minecraft
    ) {
        if (dimensions.width() <= maxTextureSize && dimensions.height() <= maxTextureSize) {
            return dimensions;
        }

        double aspectRatio = (double) dimensions.width() / (double) dimensions.height();
        int clampedW = dimensions.width();
        int clampedH = dimensions.height();

        if (clampedW > maxTextureSize) {
            clampedW = maxTextureSize;
            clampedH = (int) Math.round(clampedW / aspectRatio);
        }
        if (clampedH > maxTextureSize) {
            clampedH = maxTextureSize;
            clampedW = (int) Math.round(clampedH * aspectRatio);
        }

        if (clampedW % 2 != 0) clampedW -= 1;
        if (clampedH % 2 != 0) clampedH -= 1;

        ResolutionPreset.ResolutionDimensions clamped = new ResolutionPreset.ResolutionDimensions(
                Math.max(128, clampedW),
                Math.max(128, clampedH)
        );

        LOGGER.warn("[Hyper Quality Screenshots] Requested resolution {} exceeds GPU limit ({}). Clamping to {} to prevent OpenGL driver crash.",
                dimensions.getFormattedString(), maxTextureSize, clamped.getFormattedString());

        if (config != null && config.hardwareTransparencyAlerts && minecraft != null) {
            Component clampAlert = Component.translatable(
                    "hyperscreenshots.notification.hardware_clamped",
                    String.valueOf(maxTextureSize),
                    clamped.getFormattedString()
            );
            sendSystemChatMessage(minecraft, clampAlert);
        }

        return clamped;
    }

    private static void sendSystemChatMessage(Minecraft minecraft, Component message) {
        if (minecraft != null && minecraft.gui != null && minecraft.gui.getChat() != null) {
            minecraft.gui.getChat().addMessage(message);
        }
    }

    private void restoreCaptureState(Minecraft minecraft, HyperScreenshotsConfig config) {
        this.capturing = false;
        if (config.autoHideHud) {
            minecraft.options.hideGui = previousHudHidden;
        }
    }

    public OffscreenFramebuffer getOffscreenFramebuffer() {
        return offscreenFramebuffer;
    }
}
