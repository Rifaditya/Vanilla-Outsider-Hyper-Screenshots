// Copyright (C) 2026 Dasik (Rifaditya) | GNU GPLv3
package net.vanillaoutsider.hyperscreenshots.render;

import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.platform.NativeImage;
import com.mojang.blaze3d.platform.Window;
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

    private final OffscreenFramebuffer offscreenFramebuffer = new OffscreenFramebuffer();
    private boolean captureRequested = false;
    private ResolutionPreset activePreset = ResolutionPreset.FOUR_K;
    private boolean isInstantMax = false;
    private boolean previousHudHidden = false;

    private HyperCaptureManager() {}

    public static HyperCaptureManager getInstance() {
        return INSTANCE;
    }

    public void requestCapture(ResolutionPreset preset, boolean instantMax) {
        this.activePreset = (preset != null) ? preset : HyperScreenshotsConfig.get().resolutionPreset;
        this.isInstantMax = instantMax;
        this.captureRequested = true;
        LOGGER.debug("[Hyper Quality Screenshots] Capture requested for preset: {} (Instant Max: {})", activePreset, isInstantMax);
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

        HyperScreenshotsConfig config = HyperScreenshotsConfig.get();
        ResolutionPreset preset = isInstantMax ? ResolutionPreset.SIXTEEN_K : activePreset;

        Window window = minecraft.getWindow();
        int originalWidth = window.getWidth();
        int originalHeight = window.getHeight();
        int originalGuiScale = window.getGuiScale();
        ResolutionPreset.ResolutionDimensions dimensions = preset.calculateDimensions(originalWidth, originalHeight, config.customMultiplier);

        LOGGER.info("[Hyper Quality Screenshots] Executing {} capture ({}x{}) with proportional UI scale", preset.getDisplayName(), dimensions.width(), dimensions.height());

        // Handle auto-hide HUD if configured
        if (config.autoHideHud) {
            this.previousHudHidden = minecraft.options.hideGui;
            minecraft.options.hideGui = true;
        }

        RenderTarget target = minecraft.getMainRenderTarget();
        boolean targetResized = false;

        try {
            if (preset == ResolutionPreset.NORMAL) {
                // Capture directly from main render target
                Screenshot.takeScreenshot(target, image -> {
                    restoreHudState(minecraft, config);
                    AsyncScreenshotWriter.dispatchSave(image, preset, dimensions.width(), dimensions.height());
                });
            } else {
                // Supersampled resolution render pass (World + UI / Screens)
                ResolutionPreset.TileGrid grid = ResolutionPreset.getSuggestedTileGrid(dimensions.width(), dimensions.height());
                
                if (grid.getTotalTiles() > 1 && config.hardwareTransparencyAlerts) {
                    Component tilingMsg = Component.translatable("hyperscreenshots.notification.tiling_active", grid.columns(), grid.rows());
                    if (minecraft.gui != null && minecraft.gui.getChat() != null) {
                        minecraft.gui.getChat().addClientSystemMessage(tilingMsg);
                    }
                }

                float scaleFactor = (float) dimensions.width() / (float) Math.max(1, originalWidth);
                int targetGuiScale = Math.max(1, Math.round(originalGuiScale * scaleFactor));

                window.setWidth(dimensions.width());
                window.setHeight(dimensions.height());
                window.setGuiScale(targetGuiScale);
                target.resize(dimensions.width(), dimensions.height());
                targetResized = true;

                minecraft.gameRenderer.render(deltaTracker, true);

                Screenshot.takeScreenshot(target, image -> {
                    window.setWidth(originalWidth);
                    window.setHeight(originalHeight);
                    window.setGuiScale(originalGuiScale);
                    target.resize(originalWidth, originalHeight);
                    restoreHudState(minecraft, config);

                    AsyncScreenshotWriter.dispatchSave(image, preset, dimensions.width(), dimensions.height());
                });
            }
        } catch (Exception e) {
            LOGGER.error("[Hyper Quality Screenshots] Exception during screenshot capture pass", e);
            if (targetResized) {
                window.setWidth(originalWidth);
                window.setHeight(originalHeight);
                window.setGuiScale(originalGuiScale);
                target.resize(originalWidth, originalHeight);
            }
            restoreHudState(minecraft, config);
            Component errorMsg = Component.translatable("hyperscreenshots.notification.error", e.getMessage());
            if (minecraft.gui != null && minecraft.gui.getChat() != null) {
                minecraft.gui.getChat().addClientSystemMessage(errorMsg);
            }
        }
    }

    private void restoreHudState(Minecraft minecraft, HyperScreenshotsConfig config) {
        if (config.autoHideHud) {
            minecraft.options.hideGui = previousHudHidden;
        }
    }

    public OffscreenFramebuffer getOffscreenFramebuffer() {
        return offscreenFramebuffer;
    }
}
