package com.serverswitcher;

import com.mojang.blaze3d.platform.InputConstants;
import com.serverswitcher.config.ModSettings;
import com.serverswitcher.screen.ServerSwitchScreen;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.PauseScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.resources.Identifier;

public final class ServerSwitcherClient implements ClientModInitializer {

    public static final String MOD_ID = "serverswitcher";

    private static KeyMapping openSwitcherKey;

    @Override
    public void onInitializeClient() {
        ModSettings.reload();
        openSwitcherKey = KeyMappingHelper.registerKeyMapping(new KeyMapping(
                "key.serverswitcher.open",
                InputConstants.Type.KEYSYM,
                InputConstants.UNKNOWN.getValue(),
                KeyMapping.Category.register(Identifier.fromNamespaceAndPath(MOD_ID, "category"))
        ));

        ClientTickEvents.END_CLIENT_TICK.register(ServerSwitcherClient::onEndClientTick);
    }

    private static void onEndClientTick(Minecraft client) {
        while (openSwitcherKey.consumeClick()) {
            if (client.level == null || client.player == null) {
                continue;
            }
            Screen current = client.gui.screen();
            if (current instanceof PauseScreen || current == null) {
                client.gui.setScreen(new ServerSwitchScreen(current));
            }
        }
    }
}
