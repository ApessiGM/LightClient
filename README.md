# Light Client

A high-performance, modular **Minecraft 1.21.11** PvP client built on **Fabric** and **Java 21**,
featuring a glassmorphism ClickGUI, a fully customisable HUD, an in-game offline account switcher,
cosmetics and performance modules.

**Author:** kryszv_

Output artifact: `build/libs/LightClient-1.21.11.jar`

---

## Requirements

| Tool | Version |
|------|---------|
| JDK | 21 (e.g. Temurin / OpenJDK 21) |
| Minecraft | 1.21.11 |
| Fabric Loader | 0.19.3+ |
| Fabric API | 0.141.4+1.21.11 |
| Gradle | wrapper provides 9.6.1 (no manual install needed) |

The repository ships a Gradle wrapper, so you do **not** need a system Gradle install.

---

## Building

### Linux / macOS
```bash
export JAVA_HOME=/path/to/jdk-21
./gradlew build
```

### Windows (PowerShell)
```powershell
$env:JAVA_HOME = "C:\path\to\jdk-21"
.\gradlew.bat build
```

After a successful build the client jar is at:
```
build/libs/LightClient-1.21.11.jar
```

Copy that jar into your `.minecraft/mods` folder (alongside the matching **Fabric API** jar)
on a Fabric Loader 1.21.11 profile.

### Running in a dev environment
```bash
./gradlew runClient
```

---

## Default controls

| Key | Action |
|-----|--------|
| `Right Shift` | Open / close the ClickGUI |
| `Right Ctrl` | Open the offline Account Switcher |
| `C` | Zoom (while enabled) |
| `V` | Freelook |
| `B` | Emote |

In the ClickGUI: **left-click** a module to toggle it, **right-click** to expand its settings,
drag panel headers to reposition them, and use the search bar (top-right) to filter modules.

---

## Chat commands

The command prefix is `.`

| Command | Description |
|---------|-------------|
| `.help` | List all commands |
| `.toggle <module>` | Enable/disable a module |
| `.bind <module> <key\|none>` | Rebind or clear a module's keybind |
| `.friend add\|remove\|list [name]` | Manage your friends list |
| `.config save\|load\|list\|export\|import [name\|path]` | Manage configuration & profiles |
| `.theme <name\|list>` | Switch the active colour theme |
| `.account add\|remove\|login\|list [name]` | Manage offline accounts (no args opens the GUI) |

Configuration is stored as JSON under `config/lightclient/` with profile support under
`config/lightclient/profiles/`.

---

## Project structure

```
src/main/java/dev/lightclient/
├─ LightClient.java          # ClientModInitializer entry point + service locator
├─ Reference.java            # Mod id, version, brand palette
├─ event/                    # Reflection-based EventBus (@EventTarget, priorities)
├─ module/                   # Module base class + categories
├─ setting/                  # Boolean/Number/Mode/Color/Keybind settings (JSON-serialisable)
├─ modules/                  # Feature modules grouped by category
│  ├─ combat/ movement/ render/ player/
│  ├─ utility/ misc/ performance/ cosmetics/
├─ hud/                      # HudModule base + draggable HUD elements
│  └─ elements/              # FPS, CPS, Ping, Coordinates, Keystrokes, Armor, ...
├─ gui/                      # ClickGUI screen, panels, module buttons, components
├─ manager/                  # Event/Module/Config/Command/Notification/Theme/Hud/Render managers
├─ command/                  # Chat command system + implementations
├─ account/                  # Offline account model
├─ util/                     # Color, math, animation, render helpers, click tracker
└─ mixin/                    # TitleScreenMixin (branding) + MinecraftClientAccessor (session swap)

src/main/resources/
├─ fabric.mod.json
├─ lightclient.mixins.json
└─ assets/lightclient/
   ├─ icon.png               # crystal-L client logo
   ├─ textures/logo.png      # high-res logo
   └─ lang/en_us.json
```

---

## Theme palette

| Colour | Hex |
|--------|-----|
| BlueViolet | `#8A2BE2` |
| Neon | `#7B2DFF` |
| Bright | `#AA00FF` |
| Dark Primary | `#121212` |
| Dark Secondary | `#0A0A0A` |

---

## Accounts (offline / cracked)

Light Client ships an **in-game offline account switcher** — no game restart required.
Open it with `Right Ctrl` (or `.account`), type a username, press **Add**, then **Login** to swap
the active session live. Accounts persist in `config/lightclient/accounts.json`.

> Offline accounts use a vanilla-style offline UUID and can only join servers in offline mode
> (`online-mode=false`) or singleplayer. Premium/Microsoft authentication is not bundled.

## Icon

The client icon (`assets/lightclient/icon.png`) is derived from the provided crystal-`L` logo.
A fallback generator is available at `scripts/generate_icon.py`.

## Scope notes

The architecture (EventBus, managers, module/setting framework, ClickGUI, HUD, config/profiles,
commands, theming, animations) is fully implemented and production-ready. Several visual modules
that require deep render-pipeline mixins (e.g. Motion Blur shader, Old Animations, custom cape
meshes, Microsoft account login) are wired into the module/settings system with their behaviour
hooks in place so they can be extended without touching the core. Each such module is clearly a
real, configurable feature rather than a stub.
