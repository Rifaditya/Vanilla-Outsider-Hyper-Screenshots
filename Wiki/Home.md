# 📸 Vanilla Outsider: Hyper Quality Screenshots

🌐 **Languages**: [[🇺🇸 English|Home]] | [[🇨🇳 简体中文|zh_cn-Home]] | [[🇭🇰 繁體中文|zh_tw-Home]] | [[🇷🇺 Русский|ru_ru-Home]] | [[🇪🇸 Español|es_es-Home]] | [[🇩🇪 Deutsch|de_de-Home]] | [[🇫🇷 Français|fr_fr-Home]] | [[🇧🇷 Português|pt_br-Home]] | [[🇯🇵 日本語|ja_jp-Home]] | [[🇮🇩 Bahasa Indonesia|id_id-Home]] | [[🇰🇷 한국어|ko_kr-Home]]

[![Minecraft Multi-Era](https://img.shields.io/badge/Minecraft-1.20.1%20%7C%201.21.x%20%7C%2026.x-brightgreen.svg)](https://minecraft.net)
[![Fabric Loader](https://img.shields.io/badge/Fabric-0.15.0+-blue.svg)](https://fabricmc.net)
[![License: GPLv3](https://img.shields.io/badge/License-GPLv3-yellow.svg)](https://www.gnu.org/licenses/gpl-3.0)

Welcome to the official technical documentation portal for **Vanilla Outsider: Hyper Quality Screenshots**! This mod brings workstation-grade rendering pipelines to Minecraft Fabric, unlocking ultra-high-resolution captures (2K QHD, 4K UHD, 8K FUHD, 16K QUHD, and custom scale up to 16.0x) with zero GPU driver timeouts, offscreen framebuffer supersampling, frustum-sheared tiled rendering passes, and non-blocking asynchronous PNG disk writes.

> 📌 **Repository Source Disclaimer**: The documentation in this Wiki reflects the **current source code state in the repository**, which may include recent unreleased commits or developmental features ahead of public release builds on CurseForge and Modrinth.

Select your target Minecraft version below to enter its **dedicated, isolated Wiki** with tailored engine mechanics, resolution presets, mathematical formulas, and developer references:

---

## 📦 Select Your Minecraft Version Wiki

| Minecraft Version | Era Classification | Latest Release Tag | Dedicated Wiki Portal |
| :--- | :--- | :---: | :--- |
| **Minecraft 1.20.1** | 🏛️ Legacy Anchor Era | `1.0.0+1.20.1` | [[👉 Enter MC 1.20.1 Wiki|1.20.1-Home]] |
| **Minecraft 1.21.1** | 🌿 Transitional Early Era | `1.0.0+1.21.1` | [[👉 Enter MC 1.21.1 Wiki|1.21.1-Home]] |
| **Minecraft 1.21.11** | ❄️ Transitional Late Era | `1.0.0+1.21.11` | [[👉 Enter MC 1.21.11 Wiki|1.21.11-Home]] |
| **Minecraft 26.1** | 👑 Modern Sovereign Drop (26.1.2) | `1.0.0+26.1.2` | [[👉 Enter MC 26.1 Wiki|26.1-Home]] |
| **Minecraft 26.2** | 🎯 Modern Predecessor | `1.0.0+26.2` | [[👉 Enter MC 26.2 Wiki|26.2-Home]] |
| **Minecraft 26.3** | ⚡ Modern Lead / Snapshot | `1.0.0+26.3` | [[👉 Enter MC 26.3 Wiki|26.3-Home]] |

---

## 🗺️ Master Version Compatibility & Architecture
* [[Version Compatibility & History Matrix|Version-Compatibility]] — Complete 6-era lifecycle matrix, safe mid-game installation guide, hardware bounds, and clean uninstallation policy.
* [[Developer Setup & Building Guide|Developer-Setup-and-Building]] — Step-by-step Loom Gradle setup, JDK toolchains, Mixin target verification, and continuous testing commands.

---

## ⚡ Core Feature Highlights
1. **Tiled Framebuffer Supersampling**: Renders massive multi-megapixel scenes without hitting OpenGL maximum texture dimensions (`GL_MAX_TEXTURE_SIZE`) by slicing the camera frustum into a sub-tile grid.
2. **Asynchronous Threaded Disk I/O**: Eliminates screenshot frame stutter by delegating PNG encoding and file persistence to `Util.ioPool()` worker threads.
3. **Proportional UI Scaling**: Automatically rescales Minecraft HUD and GUI elements to prevent microscopic interfaces in high-resolution captures.
4. **Hotkeys & Shortcuts**: Standard capture (`F2`), instant 16K QUHD snapshot (`Ctrl + F2`), and dynamic first-person hand toggle (`Alt + F2`).
5. **Dual Configuration Integration**: Comprehensive in-game GUI via YetAnotherConfigLib v3 (YACL) / ModMenu, paired with full Brigadier client commands (`/hyperscreenshots`, `/hypershot`, `/hqss`).
