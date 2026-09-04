package com.serverswitcher.config;

import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public final class ModConfigScreen {

    private ModConfigScreen() {
    }

    public static Screen create(Screen parent) {
        ModSettings s = ModSettings.get();
        ConfigBuilder builder = ConfigBuilder.create()
                .setParentScreen(parent)
                .setTitle(Component.translatable("serverswitcher.config.title"))
                .transparentBackground()
                .setSavingRunnable(() -> {
                    s.save();
                    ModSettings.reload();
                });

        ConfigEntryBuilder eb = builder.entryBuilder();
        ConfigCategory ui = builder.getOrCreateCategory(Component.translatable("serverswitcher.config.category.ui"));

        ui.addEntry(eb.startIntSlider(Component.translatable("serverswitcher.config.panel_width"), s.panelWidth, 200, 400)
                .setDefaultValue(280)
                .setTooltip(Component.translatable("serverswitcher.config.panel_width.tooltip"))
                .setSaveConsumer(v -> s.panelWidth = v)
                .build());

        ui.addEntry(eb.startIntSlider(Component.translatable("serverswitcher.config.panel_height"), s.panelHeight, 140, 320)
                .setDefaultValue(200)
                .setTooltip(Component.translatable("serverswitcher.config.panel_height.tooltip"))
                .setSaveConsumer(v -> s.panelHeight = v)
                .build());

        ui.addEntry(eb.startIntSlider(Component.translatable("serverswitcher.config.row_height"), s.rowHeight, 18, 32)
                .setDefaultValue(22)
                .setTooltip(Component.translatable("serverswitcher.config.row_height.tooltip"))
                .setSaveConsumer(v -> s.rowHeight = v)
                .build());

        ui.addEntry(eb.startBooleanToggle(Component.translatable("serverswitcher.config.show_pause_button"), s.showPauseMenuButton)
                .setDefaultValue(true)
                .setTooltip(Component.translatable("serverswitcher.config.show_pause_button.tooltip"))
                .setSaveConsumer(v -> s.showPauseMenuButton = v)
                .build());

        ConfigCategory keys = builder.getOrCreateCategory(Component.translatable("serverswitcher.config.category.keys"));
        keys.addEntry(eb.startTextDescription(Component.translatable("serverswitcher.config.keys.hint")).build());

        return builder.build();
    }
}
