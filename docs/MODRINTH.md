# Modrinth Submission

Author: nattapat2871 (https://nattapat2871.me)

Project: https://modrinth.com/mod/namserverswitcher
Project settings: https://modrinth.com/mod/namserverswitcher/settings
Publication requires Modrinth moderation; a prepared draft is not an approval.

## Project Details

- Type: Mod
- Name: NamServerSwitcher
- Slug: namserverswitcher
- Summary: Switch saved servers from the pause menu with mouse-wheel scrolling, a draggable scrollbar, and immediate disconnect before joining.
- Categories: Utility, Management
- Loader: Fabric
- Client side: Required
- Server side: Unsupported
- License: MIT
- Source: https://github.com/Nattapat2871/NamServerSwitcher
- Issues: https://github.com/Nattapat2871/NamServerSwitcher/issues
- Gallery: `assets/quick-join.png` and `assets/pause-menu.png` (maintainer screenshots)
- Icon: leave unset until a non-AI image is supplied. Do not upload the generated icon.
- Description: `MODRINTH-DESCRIPTION.md`

## Uploading Versions

Use the normal `namserverswitcher-<minecraft>-1.1.1.jar`, not a sources or dev jar.
Create one version entry per Minecraft target and select **only that exact game
version**. Each jar is compiled and remapped for its own target; do not mark one
jar as supporting the whole range.

For each version, add **Fabric API** and **Cloth Config** as required dependencies,
and **Mod Menu** as optional. All three must match the selected Minecraft version.
Meteor Client is not required. Java 21 is required for 1.21.x; Java 25 for 26.x.
The build dependency versions are pinned in `versions.json`.

Use the generated `modrinth/versions.json` in the release bundle to match files,
supported game versions, dependencies, and hashes during upload.

## Content Disclosures

Configure these in Modrinth's content-disclosure settings, not only in the description:

- Derivative content: this MIT-licensed fork derives from
  [Progem4041/ServersSwitcher](https://github.com/Progem4041/ServersSwitcher),
  also published at https://modrinth.com/project/servers-switcher.
- AI-generated assets: the new server-tower icon was generated with OpenAI image generation.
- AI-generated code: the multiversion compatibility implementation and fixes were AI-assisted.
- AI-generated text: this submission text, README, and release descriptions were AI-assisted.
- No runtime AI service is required by the mod. The icon does not add any network requests.

The August 13, 2026 content rules prohibit AI-generated images on project pages.
The generated icon was rejected by the website and must not be reuploaded there.
Use only the maintainer's genuine gameplay screenshots for the gallery. AI usage
must be disclosed; primarily AI-generated projects are not eligible for publication.

Keep the upstream MIT copyright notice and the original-project attribution.
Check the current [content disclosure requirements](https://support.modrinth.com/en/articles/16567675-content-disclosures)
and [content rules](https://modrinth.com/legal/rules) when submitting. Review approval
is decided by Modrinth; the prepared files are not evidence of approval.

## Validation Before Submission

The release report lists per-target compilation and unit-test results. Compilation
does not replace in-game validation. Test Quick Join, the scrollbar, reconnect,
switching between two servers, and cancelling a failed connection in each target
you plan to publish. Use an actual gameplay screenshot for the gallery; the icon
is branding artwork, not a screenshot of the mod interface.
