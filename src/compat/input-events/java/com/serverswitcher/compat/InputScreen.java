// SPDX-License-Identifier: MIT
// Author: nattapat2871 (https://nattapat2871.me)
package com.serverswitcher.compat;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.client.input.MouseButtonEvent;

public abstract class InputScreen extends Screen {
    protected InputScreen(Component title) { super(title); }
    protected abstract boolean onMouseDown(double x, double y, int button);
    protected abstract boolean onMouseUp(double x, double y, int button);
    protected abstract boolean onMouseDrag(double x, double y, int button);

    @Override
    public boolean mouseClicked(MouseButtonEvent event, boolean doubleClick) {
        if (onMouseDown(event.x(), event.y(), event.button())) {
            if (event.button() == InputConstants.MOUSE_BUTTON_LEFT) {
                setDragging(true);
            }
            return true;
        }
        return super.mouseClicked(event, doubleClick);
    }

    @Override
    public boolean mouseReleased(MouseButtonEvent event) {
        boolean handled = onMouseUp(event.x(), event.y(), event.button());
        if (event.button() == InputConstants.MOUSE_BUTTON_LEFT) {
            setDragging(false);
        }
        return handled || super.mouseReleased(event);
    }

    @Override
    public boolean mouseDragged(MouseButtonEvent event, double dx, double dy) {
        if (onMouseDrag(event.x(), event.y(), event.button())) {
            return true;
        }
        return super.mouseDragged(event, dx, dy);
    }
}
