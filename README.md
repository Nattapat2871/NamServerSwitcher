# Server Switcher for Minecraft 26.2

A client-side Fabric mod for switching between saved servers from the pause menu.
Maintained by **nattapat2871 (https://nattapat2871.me)**.

[Download the latest release](https://github.com/Nattapat2871/server-switcher/releases/latest)

## Install

Requires Minecraft **26.2**, Java **25**, Fabric Loader **0.19.3 or newer**,
and the Minecraft 26.2 builds of **Fabric API** and **Cloth Config**.
Mod Menu is optional. Meteor Client is not required.

Place `serverswitcher-26.2-1.0.2.jar` in your instance's `mods` directory.
Remove older Server Switcher jars from that directory first. Restart Minecraft.

## Use

- Open the pause menu on a multiplayer server and select **Switch Server**.
- Quick Join reads the servers saved in Minecraft's multiplayer list.
- Scroll the mouse wheel anywhere over the Quick Join panel.
- Hold the left mouse button on the scrollbar thumb and drag it up or down.
  Clicking elsewhere on the track jumps to that position.
- Select a server, then choose **Connect**, or double-click its row.
- **Reconnect** reconnects to the current server.
- An optional key binding is available in Minecraft's Controls menu.

The old network connection is closed before client-world cleanup and the new
connection attempt. This avoids leaving the previous connection open until the
old server times it out. Joining still depends on the target server's response,
authentication, resource packs, and any server-side reconnect cooldown.

Panel dimensions and row height can be changed under **Settings**.
Configuration is stored in `config/serverswitcher.json`.

## Build

Install JDK 25 and run:

```powershell
.\gradlew.bat build --console=plain --stacktrace
```

On Linux/macOS:

```sh
./gradlew build --console=plain --stacktrace
```

The mod and sources jars are written to `build/libs/`. Unit tests cover wheel
scrolling, track clicks, thumb dragging, limits, resize behavior, and empty lists.
`./gradlew runClient` starts an isolated development client for manual testing.

## Credits and License

Free and open source under the [MIT License](LICENSE).
This standalone Minecraft 26.2 fork is based on
[Progem4041/ServersSwitcher](https://github.com/Progem4041/ServersSwitcher),
upstream commit `7666735cbaf12e2c6bcd4865c7bc459bfc5ba7e9`, with the local 26.2 port
and subsequent fixes by nattapat2871. Original attribution and license are retained.

This repository contains the mod source only, not Minecraft game files, user server
lists, worlds, or launcher account data.
