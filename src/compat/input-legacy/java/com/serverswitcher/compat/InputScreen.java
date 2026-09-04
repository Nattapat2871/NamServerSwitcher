// SPDX-License-Identifier: MIT
// Author: nattapat2871 (https://nattapat2871.me)
package com.serverswitcher.compat;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;


public abstract class InputScreen extends Screen {
    protected InputScreen(Component title) { super(title); }
    protected abstract boolean onMouseDown(double x, double y, int button);
    protected abstract boolean onMouseUp(double x, double y, int button);
    protected abstract boolean onMouseDrag(double x, double y, int button);

    @Override
    public boolean mouseClicked(double x, double y, int button) {
        return onMouseDown(x, y, button) || super.mouseClicked(x, y, button);
    }

    @Override
    public boolean mouseReleased(double x, double y, int button) {
        return onMouseUp(x, y, button) || super.mouseReleased(x, y, button);
    }

    @Override
    public boolean mouseDragged(double x, double y, int button, double dx, double dy) {
        return onMouseDrag(x, y, button) || super.mouseDragged(x, y, button, dx, dy);
    }
}
