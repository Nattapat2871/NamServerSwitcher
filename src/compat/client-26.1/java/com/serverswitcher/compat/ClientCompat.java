// SPDX-License-Identifier: MIT
// Author: nattapat2871 (https://nattapat2871.me)
package com.serverswitcher.compat;

import com.mojang.blaze3d.platform.InputConstants;
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.resources.Identifier;

public final class ClientCompat {
    private ClientCompat() {}

    public static void showMessage(Minecraft client, Component message) {
        if (client.player != null) client.player.sendSystemMessage(message);
    }

    public static KeyMapping registerOpenKey() {
        return KeyMappingHelper.registerKeyMapping(new KeyMapping(
                "key.serverswitcher.open", InputConstants.Type.KEYSYM, InputConstants.UNKNOWN.getValue(),
                KeyMapping.Category.register(Identifier.fromNamespaceAndPath("serverswitcher", "category"))));
    }

    public static Screen screen(Minecraft client) {
        return client.screen;
    }

    public static void setScreen(Minecraft client, Screen screen) {
        client.setScreen(screen);
    }
}
