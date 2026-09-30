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

        // Minecraft 26.3 ConnectScreen.startConnecting() performs the world disconnect,
        // multiplayer preparation, report-environment update and screen transition itself.
        // Disconnecting manually first races that vanilla flow and can leave the quick-join
        // screen unable to start the new connection.
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
