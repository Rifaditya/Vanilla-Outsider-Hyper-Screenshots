# 🖼️ 分塊幀緩衝區超取樣引擎 — 繁體中文

🌐 **Languages**: [[🇺🇸 English|Home]] | [[🇨🇳 简体中文|zh_cn-Home]] | [[🇭🇰 繁體中文|zh_tw-Home]] | [[🇷🇺 Русский|ru_ru-Home]] | [[🇪🇸 Español|es_es-Home]] | [[🇩🇪 Deutsch|de_de-Home]] | [[🇫🇷 Français|fr_fr-Home]] | [[🇧🇷 Português|pt_br-Home]] | [[🇯🇵 日本語|ja_jp-Home]] | [[🇮🇩 Bahasa Indonesia|id_id-Home]] | [[🇰🇷 한국어|ko_kr-Home]]

> 📌 **倉庫原始碼免責聲明**：本 Wiki 中的文件反映了**倉庫中的當前原始碼狀態**，可能包含先於 CurseForge 與 Modrinth 平台公開發布版本的最新未發布提交或開發中功能。

[[🏠 繁體中文 Home|zh_tw-Home]] | [[Master Home|Home]]

---

## 1. Infobox

| Parameter | Technical Details |
| :--- | :--- |
| **Language** | 繁體中文 (Traditional Chinese) |
| **Engine Implementation** | `TiledRenderEngine.java`, `HyperCaptureManager.java` |
| **Grid Thresholds** | $\le 4096 \to 1\times 1$, $\le 8192 \to 2\times 2$, $> 8192 \to 4\times 4$ |

---

## 2. Mathematical Frustum Transformation
$$M_{\text{tile}} = T\left(\frac{C - 1 - 2c}{C}, \frac{2r + 1 - R}{R}, 0\right) \cdot S(C, R, 1) \cdot M_{\text{baseProj}}$$

Aspect ratio preservation:
$$R = \frac{\max(W_{\text{window}}, 128)}{\max(H_{\text{window}}, 128)}$$
$$H_{\text{target}} = H_{\text{preset}}, \quad W_{\text{target}} = \text{round}(H_{\text{target}} \times R)$$

---

## 3. Resolution Grid Table

| Preset | Target Dimensions | Grid Layout | Sub-Passes |
| :--- | :---: | :---: | :---: |
| **Normal** | Native Window | $1 \times 1$ | 1 pass |
| **2K QHD** | $2560 \times 1440$ | $1 \times 1$ | 1 pass |
| **4K UHD** | $3840 \times 2160$ | $1 \times 1$ | 1 pass |
| **8K FUHD** | $7680 \times 4320$ | $2 \times 2$ | 4 passes |
| **16K QUHD** | $15360 \times 8640$ | $4 \times 4$ | 16 passes |

---

<p align="center">由 **Dasik (Rifaditya)** 用 ❤️ 研發 | 基於 GNU 通用公共許可證 v3.0 (GPLv3) 開源授權</p>
