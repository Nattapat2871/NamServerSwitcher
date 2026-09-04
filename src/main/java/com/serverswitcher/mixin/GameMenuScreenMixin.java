package com.serverswitcher.mixin;

import com.serverswitcher.config.ModSettings;
import com.serverswitcher.compat.ClientCompat;
import com.serverswitcher.screen.ServerSwitchScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.PauseScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.contents.TranslatableContents;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = PauseScreen.class, priority = 1100)
public abstract class GameMenuScreenMixin {

    @Inject(method = "init", at = @At("TAIL"))
    private void serverswitcher$addSwitchServerButton(CallbackInfo ci) {
        if (!ModSettings.get().showPauseMenuButton) {
            return;
        }
        PauseScreen self = (PauseScreen) (Object) this;
        Minecraft client = Minecraft.getInstance();

        for (GuiEventListener child : self.children()) {
            if (!(child instanceof Button button) || !isDisconnectButton(button)) {
                continue;
            }

            int bx = button.getX();
            int by = button.getY();
            int half = (button.getWidth() - 2) / 2;
            button.setWidth(half);

            self.addRenderableWidget(Button.builder(
                    Component.translatable("serverswitcher.button.switch_server"),
                    b -> {
                        if (client.level != null) {
                            ClientCompat.setScreen(client, new ServerSwitchScreen(self));
                        }
                    }
            ).bounds(bx + half + 2, by, half, 20).build());
            return;
        }
    }

    private static boolean isDisconnectButton(Button button) {
        if (button.getMessage().getContents() instanceof TranslatableContents translatable) {
            return "menu.disconnect".equals(translatable.getKey());
        }
        return false;
    }
}
