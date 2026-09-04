// SPDX-License-Identifier: MIT
// Author: nattapat2871 (https://nattapat2871.me)
package com.serverswitcher.network;

import com.serverswitcher.screen.ServerSwitchScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.ConnectScreen;
import net.minecraft.client.gui.screens.PauseScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.client.gui.screens.multiplayer.JoinMultiplayerScreen;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.multiplayer.resolver.ServerAddress;
import net.minecraft.network.chat.Component;

public final class ServerConnectionHelper {

    private ServerConnectionHelper() {
    }

    private static Screen sanitizeReturnScreen(Screen requested) {
        if (requested instanceof PauseScreen || requested instanceof ServerSwitchScreen) {
            return new JoinMultiplayerScreen(new TitleScreen());
        }
        return requested != null ? requested : new TitleScreen();
    }

    public static void switchToServer(Minecraft client, Screen returnScreen, ServerData target) {
        ServerData next = new ServerData(target.name, target.ip, target.type());
        next.copyFrom(target);
        ServerAddress address = ServerAddress.parseString(next.ip);
        Screen safeReturn = sanitizeReturnScreen(returnScreen);

        var previous = client.getConnection();
        if (previous != null) {
            // Minecraft.disconnect tears down client state, but does not close the socket.
            // Close it first so the old server sees us leave without waiting for a timeout.
            previous.getConnection().disconnect(Component.translatable("disconnect.quitting"));
        }
        if (client.level != null || previous != null) {
            client.disconnect(safeReturn, false);
        }
        ConnectScreen.startConnecting(safeReturn, client, address, next, false, null);
    }

    public static boolean reconnectToCurrent(Minecraft client, Screen returnScreen) {
        ServerData current = client.getCurrentServer();
        if (current == null) {
            return false;
        }
        switchToServer(client, returnScreen, current);
        return true;
    }
}
