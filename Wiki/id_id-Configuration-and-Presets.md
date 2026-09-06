# ⚙️ Konfigurasi & Preset Resolusi — Bahasa Indonesia

🌐 **Languages**: [[🇺🇸 English|Home]] | [[🇨🇳 简体中文|zh_cn-Home]] | [[🇭🇰 繁體中文|zh_tw-Home]] | [[🇷🇺 Русский|ru_ru-Home]] | [[🇪🇸 Español|es_es-Home]] | [[🇩🇪 Deutsch|de_de-Home]] | [[🇫🇷 Français|fr_fr-Home]] | [[🇧🇷 Português|pt_br-Home]] | [[🇯🇵 日本語|ja_jp-Home]] | [[🇮🇩 Bahasa Indonesia|id_id-Home]] | [[🇰🇷 한국어|ko_kr-Home]]

> 📌 **Pernyataan Sumber Repositori**: Dokumentasi dalam Wiki ini mencerminkan **keadaan kode sumber saat ini di repositori**, yang mungkin mencakup komit terbaru sebelum rilis publik resmi di CurseForge dan Modrinth.

[[🏠 Bahasa Indonesia Home|id_id-Home]] | [[Master Home|Home]]

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

<p align="center">Dikembangkan dengan ❤️ oleh **Dasik (Rifaditya)** | Berlisensi di bawah GNU GPLv3</p>
