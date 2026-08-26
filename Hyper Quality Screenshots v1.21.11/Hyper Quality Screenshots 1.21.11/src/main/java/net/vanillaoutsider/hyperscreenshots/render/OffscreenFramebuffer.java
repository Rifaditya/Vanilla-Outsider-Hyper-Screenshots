// Copyright (C) 2026 Dasik (Rifaditya) | GNU GPLv3
package net.vanillaoutsider.hyperscreenshots.render;

import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.pipeline.TextureTarget;
import com.mojang.blaze3d.systems.RenderSystem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OffscreenFramebuffer {
    private static final Logger LOGGER = LoggerFactory.getLogger(OffscreenFramebuffer.class);

    private RenderTarget target;
    private int currentWidth;
    private int currentHeight;

    public RenderTarget prepare(int width, int height) {
        RenderSystem.assertOnRenderThread();

        if (target == null || currentWidth != width || currentHeight != height) {
            dispose();
            LOGGER.debug("[Hyper Quality Screenshots] Allocating offscreen RenderTarget ({}x{})", width, height);
            target = new TextureTarget("HyperScreenshots-FBO", width, height, true);
            currentWidth = width;
            currentHeight = height;
        }

        return target;
    }

    public RenderTarget getTarget() {
        return target;
    }

    public int getWidth() {
        return currentWidth;
    }

    public int getHeight() {
        return currentHeight;
    }

    public void dispose() {
        RenderSystem.assertOnRenderThread();
        if (target != null) {
            LOGGER.debug("[Hyper Quality Screenshots] Destroying offscreen RenderTarget ({}x{})", currentWidth, currentHeight);
            target.destroyBuffers();
            target = null;
            currentWidth = 0;
            currentHeight = 0;
        }
    }
}
