# ⚙️ 配置系統與解析度預設 — 繁體中文

🌐 **Languages**: [[🇺🇸 English|Home]] | [[🇨🇳 简体中文|zh_cn-Home]] | [[🇭🇰 繁體中文|zh_tw-Home]] | [[🇷🇺 Русский|ru_ru-Home]] | [[🇪🇸 Español|es_es-Home]] | [[🇩🇪 Deutsch|de_de-Home]] | [[🇫🇷 Français|fr_fr-Home]] | [[🇧🇷 Português|pt_br-Home]] | [[🇯🇵 日本語|ja_jp-Home]] | [[🇮🇩 Bahasa Indonesia|id_id-Home]] | [[🇰🇷 한국어|ko_kr-Home]]

> 📌 **倉庫原始碼免責聲明**：本 Wiki 中的文件反映了**倉庫中的當前原始碼狀態**，可能包含先於 CurseForge 與 Modrinth 平台公開發布版本的最新未發布提交或開發中功能。

[[🏠 繁體中文 Home|zh_tw-Home]] | [[Master Home|Home]]

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

<p align="center">由 **Dasik (Rifaditya)** 用 ❤️ 研發 | 基於 GNU 通用公共許可證 v3.0 (GPLv3) 開源授權</p>
