# ⚙️ 配置系统与分辨率预设 — 简体中文

🌐 **Languages**: [[🇺🇸 English|Home]] | [[🇨🇳 简体中文|zh_cn-Home]] | [[🇭🇰 繁體中文|zh_tw-Home]] | [[🇷🇺 Русский|ru_ru-Home]] | [[🇪🇸 Español|es_es-Home]] | [[🇩🇪 Deutsch|de_de-Home]] | [[🇫🇷 Français|fr_fr-Home]] | [[🇧🇷 Português|pt_br-Home]] | [[🇯🇵 日本語|ja_jp-Home]] | [[🇮🇩 Bahasa Indonesia|id_id-Home]] | [[🇰🇷 한국어|ko_kr-Home]]

> 📌 **仓库源码免责声明**：本 Wiki 中的文档反映了**仓库中的当前源代码状态**，可能包含先于 CurseForge 和 Modrinth 平台公开构建的最新未发布提交或开发中功能。

[[🏠 简体中文 Home|zh_cn-Home]] | [[Master Home|Home]]

---

## 1. Hotkeys & Controls

| Keybinding | Function |
| :--- | :--- |
| **`F2`** | Capture screenshot using active configured preset |
| **`Ctrl + F2`** | Instant 16K QUHD capture |
| **`Alt + F2`** | Toggle Auto-Hide Hand on / off |

---

## 2. Brigadier Commands
* `/hyperscreenshots status`
* `/hyperscreenshots preset <normal|2k|4k|8k|16k|custom>`
* `/hyperscreenshots capture [preset]`
* `/hyperscreenshots toggle <hud|hand|instantmax|sound|alerts>`
* `/hyperscreenshots reload`

---

## 3. JSON Configuration (`config/hyper_screenshots.json`)
```json
{
  "resolutionPreset": "FOUR_K",
  "customMultiplier": 2.0,
  "autoHideHud": false,
  "autoHideHand": false,
  "instantMaxKeyEnabled": true,
  "playSoundOnSuccess": true,
  "hardwareTransparencyAlerts": true
}
```

---

<p align="center">由 **Dasik (Rifaditya)** 用 ❤️ 研发 | 基于 GNU 通用公共许可证 v3.0 (GPLv3) 开源授权</p>
