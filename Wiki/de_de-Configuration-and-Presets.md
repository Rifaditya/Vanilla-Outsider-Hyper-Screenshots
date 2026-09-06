# ⚙️ Konfiguration & Auflösungs-Presets — Deutsch

🌐 **Languages**: [[🇺🇸 English|Home]] | [[🇨🇳 简体中文|zh_cn-Home]] | [[🇭🇰 繁體中文|zh_tw-Home]] | [[🇷🇺 Русский|ru_ru-Home]] | [[🇪🇸 Español|es_es-Home]] | [[🇩🇪 Deutsch|de_de-Home]] | [[🇫🇷 Français|fr_fr-Home]] | [[🇧🇷 Português|pt_br-Home]] | [[🇯🇵 日本語|ja_jp-Home]] | [[🇮🇩 Bahasa Indonesia|id_id-Home]] | [[🇰🇷 한국어|ko_kr-Home]]

> 📌 **Repository-Quellcode-Haftungsausschluss**: Die Dokumentation spiegelt den **aktuellen Quellcode-Stand im Repository** wider, einschließlich unvollendeter Commits vor Veröffentlichungen auf CurseForge und Modrinth.

[[🏠 Deutsch Home|de_de-Home]] | [[Master Home|Home]]

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

<p align="center">Entwickelt mit ❤️ von **Dasik (Rifaditya)** | Lizenziert unter GNU GPLv3</p>
