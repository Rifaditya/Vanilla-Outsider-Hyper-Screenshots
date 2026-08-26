// Copyright (C) 2026 Dasik (Rifaditya) | GNU GPLv3
package net.vanillaoutsider.hyperscreenshots.render;

import com.mojang.blaze3d.platform.NativeImage;
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

        HyperScreenshotsConfig config = HyperScreenshotsConfig.get();
        ResolutionPreset preset = isInstantMax ? ResolutionPreset.SIXTEEN_K : activePreset;

        int windowWidth = minecraft.getWindow().getWidth();
        int windowHeight = minecraft.getWindow().getHeight();
        ResolutionPreset.ResolutionDimensions dimensions = preset.calculateDimensions(windowWidth, windowHeight, config.customMultiplier);

        LOGGER.info("[Hyper Quality Screenshots] Executing {} capture ({}x{})", preset.getDisplayName(), dimensions.width(), dimensions.height());

        // Handle auto-hide HUD if configured
        if (config.autoHideHud) {
            this.previousHudHidden = minecraft.options.hideGui;
            minecraft.options.hideGui = true;
        }

        try {
            if (preset == ResolutionPreset.NORMAL) {
                // Capture directly from main render target
                Screenshot.takeScreenshot(minecraft.getMainRenderTarget(), image -> {
                    restoreHudState(minecraft, config);
                    AsyncScreenshotWriter.dispatchSave(image, preset, dimensions.width(), dimensions.height());
                });
            } else {
                // Offscreen supersampling capture
                ResolutionPreset.TileGrid grid = ResolutionPreset.getSuggestedTileGrid(dimensions.width(), dimensions.height());
                
                if (grid.getTotalTiles() > 1 && config.hardwareTransparencyAlerts) {
                    Component tilingMsg = Component.translatable("hyperscreenshots.notification.tiling_active", grid.columns(), grid.rows());
                    if (minecraft.gui != null && minecraft.gui.getChat() != null) {
                        minecraft.gui.getChat().addClientSystemMessage(tilingMsg);
                    }
                }

                offscreenFramebuffer.prepare(dimensions.width(), dimensions.height());

                // Capture offscreen buffer
                Screenshot.takeScreenshot(offscreenFramebuffer.getTarget(), image -> {
                    restoreHudState(minecraft, config);
                    AsyncScreenshotWriter.dispatchSave(image, preset, dimensions.width(), dimensions.height());
                });
            }
        } catch (Exception e) {
            LOGGER.error("[Hyper Quality Screenshots] Exception during screenshot capture pass", e);
            restoreHudState(minecraft, config);
            Component errorMsg = Component.translatable("hyperscreenshots.notification.error", e.getMessage());
            if (minecraft.gui != null && minecraft.gui.getChat() != null) {
                minecraft.gui.getChat().addClientSystemMessage(errorMsg);
            }
        } finally {
            this.captureRequested = false;
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
