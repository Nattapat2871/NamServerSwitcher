package com.serverswitcher;

import com.serverswitcher.compat.ClientCompat;
import com.serverswitcher.config.ModSettings;
import com.serverswitcher.screen.ServerSwitchScreen;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.PauseScreen;
import net.minecraft.client.gui.screens.Screen;

public final class ServerSwitcherClient implements ClientModInitializer {

    public static final String MOD_ID = "serverswitcher";

    private static KeyMapping openSwitcherKey;

    @Override
    public void onInitializeClient() {
        ModSettings.reload();
        openSwitcherKey = ClientCompat.registerOpenKey();

        ClientTickEvents.END_CLIENT_TICK.register(ServerSwitcherClient::onEndClientTick);
    }

    private static void onEndClientTick(Minecraft client) {
        while (openSwitcherKey.consumeClick()) {
            if (client.level == null || client.player == null) {
                continue;
            }
            Screen current = ClientCompat.screen(client);
            if (current instanceof PauseScreen || current == null) {
                ClientCompat.setScreen(client, new ServerSwitchScreen(current));
            }
        }
    }
}
