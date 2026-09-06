# 🗺️ Master Version Compatibility & Lifecycle Matrix

🌐 **Languages**: [[🇺🇸 English|Home]] | [[🇨🇳 简体中文|zh_cn-Home]] | [[🇭🇰 繁體中文|zh_tw-Home]] | [[🇷🇺 Русский|ru_ru-Home]] | [[🇪🇸 Español|es_es-Home]] | [[🇩🇪 Deutsch|de_de-Home]] | [[🇫🇷 Français|fr_fr-Home]] | [[🇧🇷 Português|pt_br-Home]] | [[🇯🇵 日本語|ja_jp-Home]] | [[🇮🇩 Bahasa Indonesia|id_id-Home]] | [[🇰🇷 한국어|ko_kr-Home]]

> 📌 **Repository Source Disclaimer**: The documentation in this Wiki reflects the **current source code state in the repository**, which may include recent unreleased commits or developmental features ahead of public release builds on CurseForge and Modrinth.

## 1. Multi-Era Version Support Matrix

Hyper Quality Screenshots maintains strict lockstep parity across all 6 targeted Minecraft version anchors under the **1 Jar 1 Version Law**. Every version is compiled natively against its official mappings and toolchain to guarantee zero runtime reflection hacks, zero binary mismatches, and zero client-side crashes.

| Minecraft Anchor | Generational Era | Target Java | Loom Toolchain | Fabric Loader | Next Queued Release | Dedicated Portal |
| :--- | :--- | :---: | :---: | :---: | :---: | :--- |
| **MC 1.20.1** | 🏛️ Legacy Anchor Era | `Java 17` | `Loom 1.7.4` | `>=0.15.0` | `1.0.0+1.20.1` | [[👉 Enter MC 1.20.1 Wiki|1.20.1-Home]] |
| **MC 1.21.1** | 🌿 Transitional Early Era | `Java 21` | `Loom 1.7.4` | `>=0.16.0` | `1.0.0+1.21.1` | [[👉 Enter MC 1.21.1 Wiki|1.21.1-Home]] |
| **MC 1.21.11** | ❄️ Transitional Late Era | `Java 21` | `Loom 1.9+` | `>=0.16.0` | `1.0.0+1.21.11` | [[👉 Enter MC 1.21.11 Wiki|1.21.11-Home]] |
| **MC 26.1** | 👑 Modern Sovereign Drop (26.1.2) | `Java 25` | `Loom 1.11+` | `>=0.19.1` | `1.0.0+26.1.2` | [[👉 Enter MC 26.1 Wiki|26.1-Home]] |
| **MC 26.2** | 🎯 Modern Predecessor | `Java 25` | `Loom 1.11+` | `>=0.19.1` | `1.0.0+26.2` | [[👉 Enter MC 26.2 Wiki|26.2-Home]] |
| **MC 26.3** | ⚡ Modern Lead / Snapshot | `Java 25` | `Loom 1.11+` | `>=0.19.1` | `1.0.0+26.3` | [[👉 Enter MC 26.3 Wiki|26.3-Home]] |

---

## 2. Dependency & Environment Matrix

| Dependency | Required / Optional | Supported Versions | Purpose |
| :--- | :---: | :--- | :--- |
| **Fabric Loader** | Mandatory | `>=0.15.0` (MC 1.20.1) / `>=0.19.1` (MC 26.x) | Mod bootstrap and Mixin loading |
| **Fabric API** | Mandatory | Match anchor version | Client command registration callbacks (`ClientCommandRegistrationCallback`) |
| **Java Runtime (JDK)** | Mandatory | Java 17 (1.20.1), Java 21 (1.21.x), Java 25 (26.x) | Modern language runtime and garbage collection |
| **YetAnotherConfigLib (YACL)** | Optional | `v3.x` | Interactive in-game settings GUI screen |
| **ModMenu** | Optional | Any compatible build | Adds config button in the mod list |

---

## 3. Client-Only Scope & Server Invariant

```
   +-------------------------------------------------------------+
   |                     CLIENT ENVIRONMENT ONLY                 |
   |  * Intercepts KeyboardHandler (F2, Ctrl+F2, Alt+F2)         |
   |  * Modifies GameRenderer & Offscreen Framebuffer            |
   |  * Dispatches NativeImage writes to Util.ioPool()           |
   +-------------------------------------------------------------+
                                  |
                  No network packets / C2S / S2C
                                  v
   +-------------------------------------------------------------+
   |                 DEDICATED / VANILLA SERVERS                 |
   |  * ZERO mod installation required on the server side        |
   |  * Players can freely join ANY vanilla or modded server     |
   +-------------------------------------------------------------+
```

* **100% Client-Side Safe**: Hyper Quality Screenshots contains zero server-side logic, custom packets, entity attributes, or world data.
* **Safe Mid-Game Addition**: Install into `mods/` at any time without resetting worlds, profiles, or options.
* **Safe Uninstallation**: Remove the mod JAR at any time. Standard vanilla F2 screenshots immediately resume normal function without leaving residual data.

---

## 4. Hardware Bounds & Safety Policy

1. **GPU Texture Limits**: If a requested resolution exceeds the OpenGL device limit (`GL_MAX_TEXTURE_SIZE`), the engine automatically engages tiled frustum rendering or safely clamps dimensions to prevent GPU driver crashes.
2. **RAM & Disk Throttling**: A 300 ms debounce cooldown (`CAPTURE_COOLDOWN_MS`) prevents users from spamming captures and overflowing background I/O queues.
3. **Proportional UI Scaling**: Preserves Minecraft HUD readability across 2K, 4K, 8K, and 16K without requiring manual GUI scale changes.
