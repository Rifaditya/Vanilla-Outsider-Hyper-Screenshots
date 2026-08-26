// Copyright (C) 2026 Dasik (Rifaditya) | GNU GPLv3
package net.vanillaoutsider.hyperscreenshots.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public enum ResolutionPreset {
    NORMAL("Normal (Native)", "_native", 1.0f, -1),
    TWO_K("2K QHD (1440p)", "_2k", 1.3333334f, 1440),
    FOUR_K("4K UHD (2160p)", "_4k", 2.0f, 2160),
    EIGHT_K("8K FUHD (4320p)", "_8k", 4.0f, 4320),
    SIXTEEN_K("16K QUHD (8640p)", "_16k", 8.0f, 8640),
    CUSTOM("Custom Multiplier", "_custom", 1.0f, -1);

    private static final Logger LOGGER = LoggerFactory.getLogger(ResolutionPreset.class);

    private final String displayName;
    private final String suffix;
    private final float defaultScale;
    private final int targetHeight;

    ResolutionPreset(String displayName, String suffix, float defaultScale, int targetHeight) {
        this.displayName = displayName;
        this.suffix = suffix;
        this.defaultScale = defaultScale;
        this.targetHeight = targetHeight;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getSuffix() {
        return suffix;
    }

    public float getDefaultScale() {
        return defaultScale;
    }

    public int getTargetHeight() {
        return targetHeight;
    }

    public String getTranslationKey() {
        return "hyperscreenshots.preset." + this.name().toLowerCase();
    }

    /**
     * Calculates target width and height preserving active aspect ratio and rounding to even integers.
     */
    public ResolutionDimensions calculateDimensions(int windowWidth, int windowHeight, float customMultiplier) {
        int safeWindowWidth = Math.max(windowWidth, 128);
        int safeWindowHeight = Math.max(windowHeight, 128);
        double aspectRatio = (double) safeWindowWidth / (double) safeWindowHeight;

        int finalWidth;
        int finalHeight;

        if (this == NORMAL) {
            finalWidth = safeWindowWidth;
            finalHeight = safeWindowHeight;
        } else if (this == CUSTOM) {
            float scale = Math.max(1.0f, Math.min(customMultiplier, 16.0f));
            finalHeight = (int) Math.round(safeWindowHeight * scale);
            finalWidth = (int) Math.round(finalHeight * aspectRatio);
        } else {
            finalHeight = this.targetHeight;
            finalWidth = (int) Math.round(finalHeight * aspectRatio);
        }

        // Align dimensions to even numbers for OpenGL texture alignment and PNG encoders
        if (finalWidth % 2 != 0) {
            finalWidth += 1;
        }
        if (finalHeight % 2 != 0) {
            finalHeight += 1;
        }

        return new ResolutionDimensions(finalWidth, finalHeight);
    }

    /**
     * Returns optimal tile subdivision grid to prevent GPU driver timeouts and VRAM exhaustion.
     */
    public static TileGrid getSuggestedTileGrid(int targetWidth, int targetHeight) {
        int maxDimension = Math.max(targetWidth, targetHeight);

        int cols;
        int rows;

        if (maxDimension <= 4096) {
            cols = 1;
            rows = 1;
        } else if (maxDimension <= 8192) {
            cols = 2;
            rows = 2;
        } else {
            cols = 4;
            rows = 4;
        }

        int tileWidth = (int) Math.ceil((double) targetWidth / cols);
        int tileHeight = (int) Math.ceil((double) targetHeight / rows);

        // Ensure tile dimensions are also even
        if (tileWidth % 2 != 0) {
            tileWidth += 1;
        }
        if (tileHeight % 2 != 0) {
            tileHeight += 1;
        }

        return new TileGrid(cols, rows, tileWidth, tileHeight);
    }

    public record ResolutionDimensions(int width, int height) {
        public ResolutionDimensions {
            if (width <= 0 || height <= 0) {
                throw new IllegalArgumentException("Dimensions must be positive: " + width + "x" + height);
            }
        }

        public long getTotalPixels() {
            return (long) width * (long) height;
        }

        public String getFormattedString() {
            return width + "x" + height;
        }
    }

    public record TileGrid(int columns, int rows, int tileWidth, int tileHeight) {
        public int getTotalTiles() {
            return columns * rows;
        }
    }
}
