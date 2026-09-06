# 🗺️ 版本相容性與生命週期矩陣 — 繁體中文

🌐 **Languages**: [[🇺🇸 English|Home]] | [[🇨🇳 简体中文|zh_cn-Home]] | [[🇭🇰 繁體中文|zh_tw-Home]] | [[🇷🇺 Русский|ru_ru-Home]] | [[🇪🇸 Español|es_es-Home]] | [[🇩🇪 Deutsch|de_de-Home]] | [[🇫🇷 Français|fr_fr-Home]] | [[🇧🇷 Português|pt_br-Home]] | [[🇯🇵 日本語|ja_jp-Home]] | [[🇮🇩 Bahasa Indonesia|id_id-Home]] | [[🇰🇷 한국어|ko_kr-Home]]

> 📌 **倉庫原始碼免責聲明**：本 Wiki 中的文件反映了**倉庫中的當前原始碼狀態**，可能包含先於 CurseForge 與 Modrinth 平台公開發布版本的最新未發布提交或開發中功能。

[[🏠 繁體中文 Home|zh_tw-Home]] | [[Master Home|Home]]

---

## 1. Multi-Era Support Matrix

| Minecraft 版本 | 世代分類 | Java | Loom | Loader | 專屬文件傳送門 |
| :--- | :--- | :---: | :---: | :---: | :--- |
| **MC 1.20.1** | 🏛️ Legacy Anchor Era | `Java 17` | `Loom 1.7.4` | `>=0.15.0` | [[👉 進入 MC 1.20.1 Wiki|1.20.1-Home]] |
| **MC 1.21.1** | 🌿 Transitional Early Era | `Java 21` | `Loom 1.7.4` | `>=0.16.0` | [[👉 進入 MC 1.21.1 Wiki|1.21.1-Home]] |
| **MC 1.21.11** | ❄️ Transitional Late Era | `Java 21` | `Loom 1.9+` | `>=0.16.0` | [[👉 進入 MC 1.21.11 Wiki|1.21.11-Home]] |
| **MC 26.1** | 👑 Modern Sovereign Drop (26.1.2) | `Java 25` | `Loom 1.11+` | `>=0.19.1` | [[👉 進入 MC 26.1 Wiki|26.1-Home]] |
| **MC 26.2** | 🎯 Modern Predecessor | `Java 25` | `Loom 1.11+` | `>=0.19.1` | [[👉 進入 MC 26.2 Wiki|26.2-Home]] |
| **MC 26.3** | ⚡ Modern Lead / Snapshot | `Java 25` | `Loom 1.11+` | `>=0.19.1` | [[👉 進入 MC 26.3 Wiki|26.3-Home]] |

---

## 2. Client Safety & Features
* **100% Client-Side**: No server mod required. Fully compatible with vanilla servers.
* **Tiled Supersampling**: Bypasses GPU texture limits via frustum slicing.
* **Async Disk Writes**: Uses background thread pool (`Util.ioPool()`) to prevent game stutter.

---

<p align="center">由 **Dasik (Rifaditya)** 用 ❤️ 研發 | 基於 GNU 通用公共許可證 v3.0 (GPLv3) 開源授權</p>
