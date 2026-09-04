// SPDX-License-Identifier: MIT
// Author: nattapat2871 (https://nattapat2871.me)
package com.serverswitcher.compat;

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
        return onMouseDown(event.x(), event.y(), event.button()) || super.mouseClicked(event, doubleClick);
    }

    @Override
    public boolean mouseReleased(MouseButtonEvent event) {
        return onMouseUp(event.x(), event.y(), event.button()) || super.mouseReleased(event);
    }

    @Override
    public boolean mouseDragged(MouseButtonEvent event, double dx, double dy) {
        return onMouseDrag(event.x(), event.y(), event.button()) || super.mouseDragged(event, dx, dy);
    }
}
