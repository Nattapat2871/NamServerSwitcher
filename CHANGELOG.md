# Changelog

## 1.1.3 - Minecraft 26.3 quick-join fixes

- Fixed Quick Join on Minecraft 26.3 by following the vanilla ConnectScreen.startConnecting flow instead of disconnecting the current connection twice.
- Synced custom list/scrollbar mouse handling with Minecraft 26.3 Screen dragging state.
- Increased the scrollbar hit area and kept mouse-wheel scrolling across the Quick Join panel.
- Retained saved-server loading, row selection, double-click join, Connect, Reconnect, and Settings behavior.

Validation: Minecraft 26.3 Gradle build and the ServerListScroll unit-test suite completed successfully.

## 1.1.2 - Minecraft 26.3

- Added an exact Minecraft 26.3 Fabric build target.
- Added a dedicated 26.3 client compatibility adapter for the renamed keyboard input API.
- Updated the 26.3 dependency set to Fabric Loader 0.19.5, Fabric API 0.161.0+26.3, Cloth Config 26.3.159, and Mod Menu 21.0.0.
- Kept existing 1.21.x through 26.2 build targets unchanged.

Validation: Gradle build for 26.3 completed successfully. In-game loading requires restarting the target instance after replacing the JAR.

## 1.1.1 - NamServerSwitcher branding

- Renamed the project, distributable jars, and in-game display name to NamServerSwitcher.
- Updated repository, issue tracker, release, Modrinth, and documentation links.
- Kept the `serverswitcher` mod ID, configuration path, translations, and package
  namespace compatible with existing installations.

Validation: all 15 exact-version builds and scroll-model unit tests. Live multiplayer
and GUI behavior have not been manually tested for every target.

## 1.1.0 - Minecraft 1.21.1 through 26.2

- Added 15 exact-version Fabric build targets, each with its own distributable jar.
- Isolated input, rendering, key mapping, and screen APIs behind compatibility adapters.
- Used intermediary access wideners for 1.21.x and official names for 26.x.
- Retained immediate old-server disconnect, wheel scrolling, and draggable scrollbars.
- Added a generated in-mod icon, maintainer screenshots, and publishing documentation.
- Added a full CI matrix and release artifact verification.

Validation: per-target Gradle builds and scroll-model unit tests. Live multiplayer
and GUI behavior have not been manually tested for every target.

## 1.0.2 - Minecraft 26.2

- Added draggable scrollbar thumbs and click-to-position scrollbar tracks.
- Allowed mouse-wheel scrolling across the entire Quick Join panel.
- Clipped partially visible rows and long server names/addresses within the list.
- Preserved scroll position when returning from settings and resizing the screen.
- Clamped configured panel dimensions to the current GUI size.
- Closed the old network connection before cleanup and joining another server.
- Used a multiplayer return screen after a failed or cancelled connection.
- Extracted a standalone build with unit tests, CI, and retained MIT attribution.

Validation: Gradle build and automated tests. Manual multiplayer switching and
in-game mouse interaction still need confirmation in a running Minecraft client.
