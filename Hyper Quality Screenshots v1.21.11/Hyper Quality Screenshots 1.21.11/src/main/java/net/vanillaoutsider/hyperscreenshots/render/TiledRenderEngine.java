// Copyright (C) 2026 Dasik (Rifaditya) | GNU GPLv3
package net.vanillaoutsider.hyperscreenshots.render;

import com.mojang.blaze3d.platform.NativeImage;
import org.joml.Matrix4f;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class TiledRenderEngine {
    private static final Logger LOGGER = LoggerFactory.getLogger(TiledRenderEngine.class);

    private TiledRenderEngine() {}

    /**
     * Modifies the camera projection matrix for the sub-tile at grid coordinate (col, row).
     * Slices the frustum horizontally and vertically so that the sub-tile renders its exact quadrant.
     */
    public static Matrix4f createSubFrustumMatrix(Matrix4f baseProjection, int col, int row, int cols, int rows) {
        Matrix4f subFrustum = new Matrix4f(baseProjection);

        if (cols <= 1 && rows <= 1) {
            return subFrustum;
        }

        // NDC coordinates range from -1.0 to 1.0
        float scaleX = (float) cols;
        float scaleY = (float) rows;

        // Translation offsets in Normalized Device Coordinates
        float transX = (float) (cols - 1 - 2 * col);
        float transY = (float) (2 * row + 1 - rows);

        // Apply scale and translation to NDC viewport projection
        Matrix4f tileTransform = new Matrix4f();
        tileTransform.scaling(scaleX, scaleY, 1.0f);
        tileTransform.translate(transX / scaleX, transY / scaleY, 0.0f);

        return tileTransform.mul(subFrustum);
    }

    /**
     * Stitches a rendered sub-tile into the master NativeImage destination buffer.
     */
    public static void stitchTile(NativeImage destination, NativeImage subTile, int col, int row, int tileWidth, int tileHeight, int totalWidth, int totalHeight) {
        int startX = col * tileWidth;
        int startY = row * tileHeight;

        int copyWidth = Math.min(tileWidth, totalWidth - startX);
        int copyHeight = Math.min(tileHeight, totalHeight - startY);

        for (int y = 0; y < copyHeight; y++) {
            for (int x = 0; x < copyWidth; x++) {
                int destX = startX + x;
                int destY = startY + y;

                if (destX < totalWidth && destY < totalHeight) {
                    int pixelColor = subTile.getPixel(x, y);
                    destination.setPixel(destX, destY, pixelColor);
                }
            }
        }
    }
}
