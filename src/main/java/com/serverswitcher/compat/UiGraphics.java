// SPDX-License-Identifier: MIT
// Author: nattapat2871 (https://nattapat2871.me)
package com.serverswitcher.compat;

import net.minecraft.client.gui.Font;
import net.minecraft.network.chat.Component;

/** Drawing operations shared by pre-26.1 and extraction-based Minecraft screens. */
public interface UiGraphics {
    void fill(int left, int top, int right, int bottom, int color);
    void outline(int left, int top, int width, int height, int color);
    void centeredText(Font font, Component text, int x, int y, int color);
    void text(Font font, String text, int x, int y, int color, boolean shadow);
    void enableScissor(int left, int top, int right, int bottom);
    void disableScissor();
}
