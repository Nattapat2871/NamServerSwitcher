# Server Switcher

A client-side Fabric mod for switching between saved servers from the pause menu.
Maintained by **nattapat2871 (https://nattapat2871.me)**.

[Download the latest release](https://github.com/Nattapat2871/server-switcher/releases/latest)
| [Modrinth: NamServerSwitcher](https://modrinth.com/mod/namserverswitcher)

Fabric builds for Minecraft **1.21.1 through 26.2**, with one jar per game version.

<img width="579" height="410" alt="image" src="https://github.com/user-attachments/assets/b38bed4e-1bd3-4536-8805-d8ddd2c9d056" />
<img width="428" height="105" alt="image" src="https://github.com/user-attachments/assets/94b13841-4557-4650-b16f-7dc55274268f" />


## Install

Requires Fabric Loader **0.19.3 or newer** and matching Minecraft builds of
**Fabric API** and **Cloth Config**. Use Java **21** for 1.21.x or **25** for 26.x.
Mod Menu is optional. Meteor Client is not required.

Supported targets: 1.21.1, 1.21.2, 1.21.3, 1.21.4, 1.21.5, 1.21.6, 1.21.7,
1.21.8, 1.21.9, 1.21.10, 1.21.11, 26.1, 26.1.1, 26.1.2, and 26.2.

Place `serverswitcher-<minecraft>-1.1.0.jar` in your instance's `mods` directory.
Choose the exact game version; a single jar does not support the entire range.
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
.\gradlew.bat collectRelease '-Pmc=26.2' --console=plain --stacktrace
# All targets (PowerShell 7):
.\scripts\build-all.ps1
.\scripts\verify-release.ps1
```

On Linux/macOS:

```sh
./gradlew collectRelease -Pmc=1.21.1 --console=plain --stacktrace
```

The mod and sources jars are written to `build/<minecraft>/libs/` and collected
in `dist/`. Dependencies and compatibility adapters are selected by `versions.json`.
1.21.x jars use intermediary mappings; 26.x jars use official names.
Unit tests cover wheel
scrolling, track clicks, thumb dragging, limits, resize behavior, and empty lists.
`./gradlew runClient -Pmc=26.2` starts an isolated development client for manual testing.
Build and unit-test success is not proof of in-game compatibility; multiplayer
switching and mouse interaction still need manual validation on each target.

## Credits and License

Free and open source under the [MIT License](LICENSE).
This standalone fork is based on
[Progem4041/ServersSwitcher](https://github.com/Progem4041/ServersSwitcher),
upstream commit `7666735cbaf12e2c6bcd4865c7bc459bfc5ba7e9`, with the local 26.2 port
and subsequent fixes by nattapat2871. Original attribution and license are retained.

This repository contains the mod source only, not Minecraft game files, user server
lists, worlds, or launcher account data.

Compatibility code and documentation were developed with AI assistance. The
optional bundled icon was AI-generated; see [asset provenance](docs/assets/README.md).
The two screenshots above were supplied by the maintainer. No AI service runs in
the mod. See [Modrinth submission notes](docs/MODRINTH.md) for publishing details.
