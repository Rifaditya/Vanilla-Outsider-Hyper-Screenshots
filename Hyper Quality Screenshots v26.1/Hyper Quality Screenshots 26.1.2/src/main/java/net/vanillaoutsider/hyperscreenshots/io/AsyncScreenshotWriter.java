// Copyright (C) 2026 Dasik (Rifaditya) | GNU GPLv3
package net.vanillaoutsider.hyperscreenshots.io;

import com.mojang.blaze3d.platform.NativeImage;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Util;
import net.vanillaoutsider.hyperscreenshots.config.HyperScreenshotsConfig;
import net.vanillaoutsider.hyperscreenshots.config.ResolutionPreset;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

public final class AsyncScreenshotWriter {
    private static final Logger LOGGER = LoggerFactory.getLogger(AsyncScreenshotWriter.class);

    private AsyncScreenshotWriter() {}

    /**
     * Offloads PNG writing to background thread and posts clickable chat notification upon completion.
     */
    public static void dispatchSave(NativeImage image, ResolutionPreset preset, int width, int height) {
        Minecraft minecraft = Minecraft.getInstance();
        File screenshotsDir = new File(minecraft.gameDirectory, "screenshots");

        if (!screenshotsDir.exists()) {
            screenshotsDir.mkdirs();
        }

        File targetFile = generateUniqueFile(screenshotsDir, preset.getSuffix());

        Util.ioPool().execute(() -> {
            try (NativeImage autoCloseImage = image) {
                LOGGER.info("[Hyper Quality Screenshots] Saving {} screenshot ({}x{}) to {}", preset.getDisplayName(), width, height, targetFile.getName());
                autoCloseImage.writeToFile(targetFile);

                minecraft.execute(() -> {
                    HyperScreenshotsConfig config = HyperScreenshotsConfig.get();

                    // Optional audio feedback
                    if (config.playSoundOnSuccess && minecraft.player != null) {
                        minecraft.player.playSound(SoundEvents.EXPERIENCE_ORB_PICKUP, 0.8f, 1.2f);
                    }

                    // Clickable file link
                    MutableComponent fileLink = Component.literal(targetFile.getName())
                            .withStyle(ChatFormatting.UNDERLINE)
                            .withStyle(Style.EMPTY.withClickEvent(new ClickEvent.OpenFile(targetFile.getAbsolutePath())));

                    Component notification = Component.translatable(
                            "hyperscreenshots.notification.saved",
                            fileLink,
                            preset.getDisplayName()
                    );

                    if (minecraft.gui != null && minecraft.gui.getChat() != null) {
                        minecraft.gui.getChat().addClientSystemMessage(notification);
                    }
                });
            } catch (Exception e) {
                LOGGER.error("[Hyper Quality Screenshots] Failed to save screenshot to disk", e);
                minecraft.execute(() -> {
                    Component errorMsg = Component.translatable("hyperscreenshots.notification.error", e.getMessage());
                    if (minecraft.gui != null && minecraft.gui.getChat() != null) {
                        minecraft.gui.getChat().addClientSystemMessage(errorMsg);
                    }
                });
            }
        });
    }

    private static File generateUniqueFile(File dir, String suffix) {
        String baseName = Util.getFilenameFormattedDateTime();
        String filename = baseName + suffix + ".png";
        File file = new File(dir, filename);
        int index = 1;

        while (file.exists()) {
            filename = baseName + suffix + "_" + index + ".png";
            file = new File(dir, filename);
            index++;
        }

        return file;
    }
}
