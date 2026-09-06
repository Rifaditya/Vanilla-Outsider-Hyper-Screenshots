# ⚙️ 구성 설정 및 해상도 프리셋 — 한국어

🌐 **Languages**: [[🇺🇸 English|Home]] | [[🇨🇳 简体中文|zh_cn-Home]] | [[🇭🇰 繁體中文|zh_tw-Home]] | [[🇷🇺 Русский|ru_ru-Home]] | [[🇪🇸 Español|es_es-Home]] | [[🇩🇪 Deutsch|de_de-Home]] | [[🇫🇷 Français|fr_fr-Home]] | [[🇧🇷 Português|pt_br-Home]] | [[🇯🇵 日本語|ja_jp-Home]] | [[🇮🇩 Bahasa Indonesia|id_id-Home]] | [[🇰🇷 한국어|ko_kr-Home]]

> 📌 **저장소 소스 코드 면책 조항**: 본 위키의 문서는 **저장소의 최신 소스 코드 상태**를 반영하며, CurseForge 및 Modrinth에 공식 출시되기 전의 최신 미출시 커밋이 포함될 수 있습니다.

[[🏠 한국어 Home|ko_kr-Home]] | [[Master Home|Home]]

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

<p align="center">개발자: **Dasik (Rifaditya)** (❤️ 제작) | GNU GPLv3 라이선스 적용</p>
