# Changelog

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
