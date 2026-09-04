// SPDX-License-Identifier: MIT
// Author: nattapat2871 (https://nattapat2871.me)
package com.serverswitcher.compat;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.Font;
import net.minecraft.network.chat.Component;

public abstract class RenderingScreen extends InputScreen {
    protected RenderingScreen(Component title) { super(title); }
    protected abstract void drawContent(UiGraphics graphics, int mouseX, int mouseY, float delta);

    @Override
    public void extractRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float delta) {
        drawContent(new Drawing(graphics), mouseX, mouseY, delta);
        super.extractRenderState(graphics, mouseX, mouseY, delta);
    }

    private record Drawing(GuiGraphicsExtractor graphics) implements UiGraphics {
        public void fill(int l, int t, int r, int b, int color) { graphics.fill(l, t, r, b, color); }
        public void outline(int l, int t, int w, int h, int color) { graphics.outline(l, t, w, h, color); }
        public void centeredText(Font font, Component text, int x, int y, int color) { graphics.centeredText(font, text, x, y, color); }
        public void text(Font font, String text, int x, int y, int color, boolean shadow) { graphics.text(font, text, x, y, color, shadow); }
        public void enableScissor(int l, int t, int r, int b) { graphics.enableScissor(l, t, r, b); }
        public void disableScissor() { graphics.disableScissor(); }
    }
}
