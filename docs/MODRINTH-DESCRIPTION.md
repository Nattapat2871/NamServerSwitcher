# NamServerSwitcher

Switch between your saved Minecraft servers directly from the pause menu.
Quick Join keeps your existing multiplayer server list available without returning
to the main menu.

## Features

- Mouse-wheel scrolling across the Quick Join panel.
- Drag the scrollbar thumb or click its track to jump through a long server list.
- Connect to a selected server, double-click a row, or reconnect to the current server.
- Closes the previous network connection before starting the new connection.
- Adjustable panel size and row height, plus an optional keyboard shortcut.
- Client-side Fabric mod. No Meteor Client or server-side installation is needed.

## Installation

Choose the file for your exact Minecraft version, install Fabric Loader, Fabric API,
and Cloth Config for that version, then place the NamServerSwitcher jar in `mods`.
Mod Menu is optional. Remove older Server Switcher or NamServerSwitcher jars before updating.

Minecraft 1.21.1 through 1.21.11 use Java 21. Minecraft 26.1, 26.1.1, 26.1.2,
and 26.2 use Java 25. The release provides a separate build for each target.

Open the pause menu while connected to a multiplayer server and select **Switch server**.
An optional shortcut can be assigned in Minecraft's Controls settings.

The next server still controls login, authentication, resource packs, and reconnect
cooldowns. Closing the previous connection cannot bypass those requirements.

## Credits

Maintained by **nattapat2871 (https://nattapat2871.me)**.
Based on [Progem4041's Server Switcher](https://github.com/Progem4041/ServersSwitcher),
with multiversion builds and scrolling/connection fixes. Free and open source under
the MIT License; upstream copyright and attribution are retained.

[Source code](https://github.com/Nattapat2871/NamServerSwitcher) |
[Report an issue](https://github.com/Nattapat2871/NamServerSwitcher/issues)
