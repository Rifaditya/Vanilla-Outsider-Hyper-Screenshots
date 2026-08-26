// Copyright (C) 2026 Dasik (Rifaditya) | GNU GPLv3
package net.vanillaoutsider.hyperscreenshots.render;

import com.mojang.blaze3d.platform.NativeImage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class TiledRenderEngine {
    private static final Logger LOGGER = LoggerFactory.getLogger(TiledRenderEngine.class);

    private TiledRenderEngine() {}

    /**
     * Calculates the sub-frustum viewport bounding box for a given tile in normalized device coordinates [-1, 1].
     */
    public static FrustumBounds calculateTileFrustum(int col, int row, int totalCols, int totalRows) {
        float left = -1.0f + (2.0f * col) / totalCols;
        float right = -1.0f + (2.0f * (col + 1)) / totalCols;
        float bottom = -1.0f + (2.0f * row) / totalRows;
        float top = -1.0f + (2.0f * (row + 1)) / totalRows;

        LOGGER.debug("[Hyper Quality Screenshots] Tile [{}, {}] Frustum bounds: L={}, R={}, B={}, T={}", col, row, left, right, bottom, top);
        return new FrustumBounds(left, right, bottom, top);
    }

    /**
     * Stitches an individual tile NativeImage into the composite destination NativeImage.
     */
    public static void stitchTile(
            NativeImage destination,
            NativeImage subTile,
            int col,
            int row,
            int tileWidth,
            int tileHeight,
            int totalRows
    ) {
        int startX = col * tileWidth;
        // Invert row index because OpenGL framebuffers are bottom-to-top whereas image formats are top-to-bottom
        int invertedRow = (totalRows - 1) - row;
        int startY = invertedRow * tileHeight;

        for (int y = 0; y < tileHeight; y++) {
            for (int x = 0; x < tileWidth; x++) {
                int destX = startX + x;
                int destY = startY + y;

                if (destX < destination.getWidth() && destY < destination.getHeight() && x < subTile.getWidth() && y < subTile.getHeight()) {
                    int pixelColor = subTile.getPixelRGBA(x, y);
                    destination.setPixelRGBA(destX, destY, pixelColor);
                }
            }
        }
    }

    public record FrustumBounds(float left, float right, float bottom, float top) {}
}
