// SPDX-License-Identifier: MIT
// Author: nattapat2871 (https://nattapat2871.me)
package com.serverswitcher.screen;

import com.mojang.blaze3d.platform.InputConstants;
import com.serverswitcher.config.ModConfigScreen;
import com.serverswitcher.config.ModSettings;
import com.serverswitcher.network.ServerConnectionHelper;
import com.serverswitcher.compat.ClientCompat;
import com.serverswitcher.compat.RenderingScreen;
import com.serverswitcher.compat.UiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.multiplayer.ServerList;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class ServerSwitchScreen extends RenderingScreen {

    private static final int SCROLLBAR_WIDTH = 10;

    private final Screen parent;
    private final List<ServerData> servers = new ArrayList<>();

    private Button connectButton;
    private Button reconnectButton;

    private int panelLeft;
    private int panelTop;
    private int panelRight;
    private int panelBottom;
    private int listLeft;
    private int listRight;
    private int listTop;
    private int listBottom;
    private int rowHeight;

    private int selectedIndex = -1;
    private final ServerListScroll scroll = new ServerListScroll();
    private long lastClickTime;
    private int lastClickedIndex = -1;

    public ServerSwitchScreen(@Nullable Screen parent) {
        super(Component.translatable("serverswitcher.screen.title"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        ModSettings s = ModSettings.get();
        int pw = Math.min(s.panelWidth, this.width - 16);
        int ph = Math.min(s.panelHeight, this.height - 16);
        panelLeft = (this.width - pw) / 2;
        panelTop = (this.height - ph) / 2;
        panelRight = panelLeft + pw;
        panelBottom = panelTop + ph;

        listTop = panelTop + 28;
        listBottom = panelBottom - 52;
        listLeft = panelLeft + 8;
        listRight = panelRight - 8;
        rowHeight = s.rowHeight;

        reloadServersFromFile();
        scroll.configure(listBottom - listTop, servers.size() * rowHeight,
                listTop + 2, listBottom - listTop - 4);

        int bw = (pw - 24) / 3;
        int bx = panelLeft + 8;
        int by = panelBottom - 44;

        reconnectButton = Button.builder(Component.translatable("serverswitcher.button.reconnect"), b -> {
            if (!ServerConnectionHelper.reconnectToCurrent(this.minecraft, parent != null ? parent : this)
                    && this.minecraft.player != null) {
                ClientCompat.showMessage(this.minecraft, Component.translatable("serverswitcher.message.no_current_server"));
            }
        }).bounds(bx, by, bw, 20).build();

        connectButton = Button.builder(Component.translatable("serverswitcher.button.connect"), b -> {
            ServerData selected = getSelectedServer();
            if (selected != null) {
                Screen back = parent != null ? parent : this;
                ServerConnectionHelper.switchToServer(this.minecraft, back, selected);
            }
        }).bounds(bx + bw + 4, by, bw, 20).build();

        Button settingsButton = Button.builder(Component.translatable("serverswitcher.button.settings"), b ->
                ClientCompat.setScreen(this.minecraft, ModConfigScreen.create(this))
        ).bounds(bx + (bw + 4) * 2, by, bw, 20).build();

        this.addRenderableWidget(reconnectButton);
        this.addRenderableWidget(connectButton);
        this.addRenderableWidget(settingsButton);

        boolean multiplayer = this.minecraft.getCurrentServer() != null;
        reconnectButton.active = multiplayer;
        connectButton.active = getSelectedServer() != null;

        this.addRenderableWidget(Button.builder(Component.translatable("gui.back"), b -> this.onClose())
                .bounds(panelLeft + pw / 2 - 50, panelBottom - 22, 100, 20).build());
    }

    private void reloadServersFromFile() {
        servers.clear();
        ServerList vanilla = new ServerList(this.minecraft);
        vanilla.load();
        for (int i = 0; i < vanilla.size(); i++) {
            ServerData fromFile = vanilla.get(i);
            ServerData copy = new ServerData(fromFile.name, fromFile.ip, fromFile.type());
            copy.copyFrom(fromFile);
            servers.add(copy);
        }
        if (selectedIndex >= servers.size()) {
            selectedIndex = servers.isEmpty() ? -1 : servers.size() - 1;
        }
    }

    @Nullable
    private ServerData getSelectedServer() {
        if (selectedIndex < 0 || selectedIndex >= servers.size()) {
            return null;
        }
        return servers.get(selectedIndex);
    }

    private int rowIndexAt(double mouseY) {
        if (mouseY < listTop || mouseY >= listBottom) {
            return -1;
        }
        int rel = (int) (mouseY - listTop + (int) scroll.amount());
        return rel / rowHeight;
    }

    private boolean isOverList(double mouseX, double mouseY) {
        return mouseX >= listLeft && mouseX < listRight && mouseY >= listTop && mouseY < listBottom;
    }

    private int scrollbarLeft() {
        return listRight - SCROLLBAR_WIDTH - 2;
    }

    private int rowRight() {
        return scroll.maximum() > 0 ? scrollbarLeft() - 2 : listRight - 1;
    }

    private boolean isOverScrollbar(double mouseX, double mouseY) {
        return scroll.maximum() > 0 && mouseX >= scrollbarLeft() - 3 && mouseX < listRight
                && mouseY >= listTop + 1 && mouseY < listBottom - 1;
    }

    private void resetDoubleClick() {
        lastClickTime = 0;
        lastClickedIndex = -1;
    }

    @Override
    public void removed() {
        scroll.endDrag();
        resetDoubleClick();
        super.removed();
    }

    private void connectToSelectedServer(ServerData info) {
        Screen back = parent != null ? parent : this;
        ServerConnectionHelper.switchToServer(this.minecraft, back, info);
    }

    @Override
    public void onClose() {
        ClientCompat.setScreen(this.minecraft, parent);
    }

    @Override
    protected void drawContent(UiGraphics graphics, int mouseX, int mouseY, float delta) {
        graphics.fill(panelLeft, panelTop, panelRight, panelBottom, 0xC0101010);
        graphics.outline(panelLeft, panelTop, panelRight - panelLeft, panelBottom - panelTop, 0xFF404040);
        graphics.centeredText(this.font, this.title, this.width / 2, panelTop + 10, 0xFFFFFFFF);

        graphics.fill(listLeft, listTop, listRight, listBottom, 0x60000000);
        graphics.outline(listLeft, listTop, listRight - listLeft, listBottom - listTop, 0xFF606060);

        if (!servers.isEmpty()) {
            graphics.enableScissor(listLeft + 1, listTop + 1, rowRight(), listBottom - 1);
            for (int i = 0; i < servers.size(); i++) {
                int rowY = listTop + i * rowHeight - (int) scroll.amount();
                if (rowY + rowHeight <= listTop || rowY >= listBottom) {
                    continue;
                }
                ServerData info = servers.get(i);
                boolean hovered = isOverList(mouseX, mouseY)
                        && mouseY >= rowY && mouseY < rowY + rowHeight
                        && mouseX >= listLeft && mouseX < rowRight();
                boolean selected = i == selectedIndex;

                if (selected) {
                    graphics.fill(listLeft + 1, rowY, rowRight(), rowY + rowHeight, 0x55FFFFFF);
                } else if (hovered) {
                    graphics.fill(listLeft + 1, rowY, rowRight(), rowY + rowHeight, 0x33FFFFFF);
                }

                int color = (hovered || selected) ? 0xFFFFFFA0 : 0xFFE0E0E0;
                int ty = rowY + Math.max(1, (rowHeight - (rowHeight >= 20 ? 19 : 9)) / 2);
                int textWidth = Math.max(0, rowRight() - listLeft - 8);
                graphics.text(this.font, this.font.plainSubstrByWidth(info.name, textWidth), listLeft + 4, ty, color, true);
                if (rowHeight >= 20) {
                    graphics.text(this.font, this.font.plainSubstrByWidth(info.ip, textWidth), listLeft + 4, ty + 10, 0xFFAAAAAA, true);
                }
            }
            graphics.disableScissor();

            if (scroll.maximum() > 0) {
                int trackX = scrollbarLeft();
                graphics.fill(trackX, listTop + 2, trackX + SCROLLBAR_WIDTH, listBottom - 2, 0xFF303030);
                int color = scroll.dragging() || isOverScrollbar(mouseX, mouseY) ? 0xFFCCCCCC : 0xFF909090;
                graphics.fill(trackX, scroll.thumbTop(), trackX + SCROLLBAR_WIDTH,
                        scroll.thumbTop() + scroll.thumbHeight(), color);
            }
        }

        if (servers.isEmpty()) {
            graphics.centeredText(
                    this.font,
                    Component.translatable("serverswitcher.empty"),
                    this.width / 2,
                    listTop + (listBottom - listTop) / 2 - 4,
                    0xFFA0A0A0
            );
        }
    }

    @Override
    protected boolean onMouseDown(double mouseX, double mouseY, int button) {
        if (button == InputConstants.MOUSE_BUTTON_LEFT && isOverScrollbar(mouseX, mouseY)) {
            resetDoubleClick();
            return scroll.beginDrag(mouseY);
        }
        if (button == InputConstants.MOUSE_BUTTON_LEFT && isOverList(mouseX, mouseY)) {
            int idx = rowIndexAt(mouseY);
            if (idx >= 0 && idx < servers.size()) {
                long now = System.currentTimeMillis();
                if (idx == lastClickedIndex && now - lastClickTime < 350) {
                    connectToSelectedServer(servers.get(idx));
                    lastClickTime = 0;
                    lastClickedIndex = -1;
                } else {
                    selectedIndex = idx;
                    lastClickTime = now;
                    lastClickedIndex = idx;
                    connectButton.active = true;
                }
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
        if (mouseX >= panelLeft && mouseX < panelRight && mouseY >= panelTop && mouseY < panelBottom
                && !servers.isEmpty()) {
            scroll.scroll(-verticalAmount * rowHeight);
            resetDoubleClick();
            return true;
        }
        return super.mouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount);
    }

    @Override
    protected boolean onMouseDrag(double mouseX, double mouseY, int button) {
        return button == InputConstants.MOUSE_BUTTON_LEFT && scroll.drag(mouseY);
    }

    @Override
    protected boolean onMouseUp(double mouseX, double mouseY, int button) {
        if (button == InputConstants.MOUSE_BUTTON_LEFT && scroll.dragging()) {
            scroll.endDrag();
            return true;
        }
        return false;
    }
}
