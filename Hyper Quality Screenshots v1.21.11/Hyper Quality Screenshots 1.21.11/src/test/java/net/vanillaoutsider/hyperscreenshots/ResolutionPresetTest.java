// Copyright (C) 2026 Dasik (Rifaditya) | GNU GPLv3
package net.vanillaoutsider.hyperscreenshots;

import net.vanillaoutsider.hyperscreenshots.config.ResolutionPreset;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ResolutionPresetTest {

    @Test
    @DisplayName("Assert NORMAL preset returns window native dimensions")
    void testNormalPreset() {
        ResolutionPreset.ResolutionDimensions dims = ResolutionPreset.NORMAL.calculateDimensions(1920, 1080, 1.0f);
        assertEquals(1920, dims.width());
        assertEquals(1080, dims.height());
    }

    @Test
    @DisplayName("Assert standard 16:9 supersampling heights")
    void testStandardSupersampling() {
        // 2K (1440p)
        ResolutionPreset.ResolutionDimensions twoK = ResolutionPreset.TWO_K.calculateDimensions(1920, 1080, 1.0f);
        assertEquals(1440, twoK.height());
        assertEquals(2560, twoK.width());

        // 4K (2160p)
        ResolutionPreset.ResolutionDimensions fourK = ResolutionPreset.FOUR_K.calculateDimensions(1920, 1080, 1.0f);
        assertEquals(2160, fourK.height());
        assertEquals(3840, fourK.width());

        // 8K (4320p)
        ResolutionPreset.ResolutionDimensions eightK = ResolutionPreset.EIGHT_K.calculateDimensions(1920, 1080, 1.0f);
        assertEquals(4320, eightK.height());
        assertEquals(7680, eightK.width());

        // 16K (8640p)
        ResolutionPreset.ResolutionDimensions sixteenK = ResolutionPreset.SIXTEEN_K.calculateDimensions(1920, 1080, 1.0f);
        assertEquals(8640, sixteenK.height());
        assertEquals(15360, sixteenK.width());
    }

    @Test
    @DisplayName("Assert ultrawide 21:9 aspect ratio preservation and even rounding")
    void testUltrawideAspectRatio() {
        // 3440 x 1440 ultrawide baseline
        ResolutionPreset.ResolutionDimensions fourK = ResolutionPreset.FOUR_K.calculateDimensions(3440, 1440, 1.0f);
        assertEquals(2160, fourK.height());
        // 2160 * (3440/1440) = 5160
        assertEquals(5160, fourK.width());
        assertEquals(0, fourK.width() % 2, "Width must be even");
        assertEquals(0, fourK.height() % 2, "Height must be even");
    }

    @Test
    @DisplayName("Assert custom multiplier calculation and bounds clamping")
    void testCustomMultiplier() {
        // 2.5x on 1000x500
        ResolutionPreset.ResolutionDimensions dims = ResolutionPreset.CUSTOM.calculateDimensions(1000, 500, 2.5f);
        assertEquals(1250, dims.height());
        assertEquals(2500, dims.width());

        // Clamping upper bound at 16x
        ResolutionPreset.ResolutionDimensions clampedMax = ResolutionPreset.CUSTOM.calculateDimensions(1920, 1080, 25.0f);
        assertEquals(17280, clampedMax.height()); // 1080 * 16
        assertEquals(30720, clampedMax.width());  // 1920 * 16

        // Clamping lower bound at 1x
        ResolutionPreset.ResolutionDimensions clampedMin = ResolutionPreset.CUSTOM.calculateDimensions(1920, 1080, 0.2f);
        assertEquals(1080, clampedMin.height());
        assertEquals(1920, clampedMin.width());
    }

    @Test
    @DisplayName("Assert tile grid subdivision for crash prevention")
    void testTileGridSubdivision() {
        // <= 4096: 1x1
        ResolutionPreset.TileGrid grid4K = ResolutionPreset.getSuggestedTileGrid(3840, 2160);
        assertEquals(1, grid4K.columns());
        assertEquals(1, grid4K.rows());
        assertEquals(1, grid4K.getTotalTiles());

        // 8K: 2x2
        ResolutionPreset.TileGrid grid8K = ResolutionPreset.getSuggestedTileGrid(7680, 4320);
        assertEquals(2, grid8K.columns());
        assertEquals(2, grid8K.rows());
        assertEquals(4, grid8K.getTotalTiles());

        // 16K: 4x4
        ResolutionPreset.TileGrid grid16K = ResolutionPreset.getSuggestedTileGrid(15360, 8640);
        assertEquals(4, grid16K.columns());
        assertEquals(4, grid16K.rows());
        assertEquals(16, grid16K.getTotalTiles());
    }
}
