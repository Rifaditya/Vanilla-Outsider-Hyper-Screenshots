<p align="center">
  <a href="https://fabricmc.net/"><img src="https://img.shields.io/badge/Fabric-API_Required-blue?style=for-the-badge&logo=fabric" alt="Fabric API Required"></a>
  <a href="https://www.java.com/"><img src="https://img.shields.io/badge/Language-Java-orange?style=for-the-badge&logo=openjdk" alt="Language Java"></a>
  <a href="https://www.gnu.org/licenses/gpl-3.0.html"><img src="https://img.shields.io/badge/License-GPLv3-green?style=for-the-badge" alt="License GPLv3"></a>
  <a href="https://modrinth.com/mod/hyper-screenshots"><img src="https://img.shields.io/badge/Minecraft-1.20.1_to_26.3+-brightgreen?style=for-the-badge" alt="Minecraft 1.20.1 to 26.3+"></a>
</p>

# 📸 Hyper Quality Screenshots

> **"Capture crystal-clear 2K, 4K, 8K, and 16K ultra-high-resolution screenshots without window resizing, screen freezing, or GPU crashes."**

Have you ever lined up the most breathtaking view of your base or landscape in Minecraft, pressed `F2`, only to find out the resulting screenshot is locked to your monitor's display resolution with jagged edges and pixelated distant terrain? 

**Hyper Quality Screenshots** completely revamps Minecraft's screenshot capture pipeline. By combining isolated offscreen framebuffer supersampling, chunked sub-frustum tiled rendering, and asynchronous non-blocking background disk writing, you can now take stunning, wallpaper-grade **2K (1440p)**, **4K (2160p)**, **8K (4320p)**, and **16K (8640p)** screenshots with zero client window flickering or lag.

Part of the **Vanilla Outsider Collection** — modern, non-intrusive quality-of-life enhancements designed to integrate seamlessly into vanilla gameplay.

---

## ✨ Features & Architecture

### 🖼️ Multi-Resolution Supersampling Engine
Take high-resolution screenshots far beyond your physical monitor's display limits:
* **Normal**: Standard native display resolution capture.
* **2K Quad HD (1440p)**: $2560 \times 1440$ baseline resolution (or $1.33\times$ aspect-scaled).
* **4K Ultra HD (2160p)**: $3840 \times 2160$ crisp wallpaper standard.
* **8K Super UHD (4320p)**: $7680 \times 4320$ ultra-detailed photography.
* **16K Extreme QUHD (8640p)**: $15360 \times 8640$ maximum quality for large prints and panoramic showcases.
* **Custom Multiplier**: Freely customize supersampling from $1.0\times$ up to $16.0\times$ scale.

### 🧩 Tiled Frustum Grid Rendering (Anti-Crash & Anti-TDR)
Rendering an uncompressed 16K frame directly in a single GPU pass requires over 500MB of raw framebuffer memory, often causing GPU driver timeouts (TDR) and out-of-memory crashes on mid-range hardware.
* **Smart Grid Slicing**: 8K captures are automatically rendered across a $2 \times 2$ grid (4 tiles), and 16K captures across a $4 \times 4$ grid (16 tiles).
* **Sub-Frustum Projection**: Each tile renders a precise sub-frustum viewport bounding box in normalized device coordinates $[-1, 1]$.
* **Seamless Pixel Stitching**: Sub-tiles are composited into the final `NativeImage` buffer with zero visible seams or distortion.

### 🛡️ Isolated Offscreen Framebuffer (FBO)
* Screenshots are rendered directly into a dedicated offscreen `RenderTarget`.
* **Zero Window Flickering**: Your Minecraft game window and HUD never resize, shake, or stretch during capture.

### ⚡ Non-Blocking Asynchronous Disk Writing
* PNG compression and disk I/O are offloaded entirely to `Util.ioPool()` in the background.
* Instant return to gameplay with zero input freeze.
* Chat notification provides a clickable file link to open the saved screenshot directly in your OS image viewer.
* Optional experience orb audio chime confirms successful save.

### ⌨️ Dual Keybind Architecture
* **`F2`**: Captures a screenshot using your configured active preset in ModMenu / YACL.
* **`Ctrl + F2`**: Instant shortcut to take a maximum 16K Extreme QUHD screenshot on demand.

### 🎛️ Dedicated Compatibility & Gated Client UI
* **ModMenu & YetAnotherConfigLib (YACL v3)**: Fully configurable via in-game settings GUI.
* **Strictly Zero GameRules**: Pure client-side mod that works on any multiplayer server without server-side installation.
* **Classloading Shielding**: Uses isolated screen reflection helpers to ensure crash-free execution on headless dedicated servers and vanilla clients.

---

## 📊 Resolution & Grid Reference Matrix

| Preset | Target Resolution (16:9) | Supersampling Multiplier | Tile Grid | VRAM Allocation Safety |
| :--- | :--- | :--- | :--- | :--- |
| **Normal** | Native Window | $1.0\times$ | $1 \times 1$ | Native / Zero Overhead |
| **2K (QHD)** | $2560 \times 1440$ | $\approx 1.33\times$ | $1 \times 1$ | Safe for all GPUs |
| **4K (UHD)** | $3840 \times 2160$ | $\approx 2.0\times$ | $1 \times 1$ | Safe for modern GPUs |
| **8K (FUHD)** | $7680 \times 4320$ | $\approx 4.0\times$ | $2 \times 2$ (4 tiles) | Tiled / Prevents VRAM Spikes |
| **16K (QUHD)** | $15360 \times 8640$ | $\approx 8.0\times$ | $4 \times 4$ (16 tiles) | Tiled / Crash-Free Memory Safety |
| **Custom** | Dynamic ($W \times H$) | $1.0\times - 16.0\times$ | Dynamic | Auto-computed grid |

---

## 📖 In-Depth How-To & Operational Playbook

### 1. Installation & Drop-In Setup
1. Download the build matching your Minecraft version (`1.20.1`, `1.21.1`, `1.21.11`, `26.1.2`, `26.2`, or `26.3`).
2. Place the `.jar` into your Minecraft `.minecraft/mods` directory.
3. Install **Fabric API**.
4. *(Optional but Recommended)* Install **ModMenu** and **YetAnotherConfigLib (YACL)** to access the configuration screen.

### 2. Operating the Capture Pipeline
* **Standard Capture**: Press `F2` to capture with your chosen preset (defaults to **4K UHD**).
* **Instant 16K Super-Resolution**: Press `Ctrl + F2` anywhere in-game to immediately take a 16K screenshot.
* **Auto-Hide HUD**: Enable `Auto-Hide HUD` in config to automatically hide player health, hotbar, and chat during screenshot capture without manually pressing `F1`.

### 3. File Location & Notifications
* Screenshots are saved to `.minecraft/screenshots/` with unique timestamp and resolution tags (e.g. `2026-08-26_11.30.00_4k.png`, `2026-08-26_11.30.05_16k.png`).
* Clicking the green underlined filename in chat will open the image file in your system's default photo viewer.

---

## ☕ Support the Developer

I create and maintain high-performance, quality-of-life Minecraft mods independently. If you enjoy my work, consider supporting development!

<p align="center">
  <a href="https://ko-fi.com/dasik"><img src="https://img.shields.io/badge/Ko--fi-Support_on_Kofi-FF5E5B?style=for-the-badge&logo=ko-fi&logoColor=white" alt="Support on Ko-fi"></a>
  <a href="https://sociobuzz.com/dasik/tribe"><img src="https://img.shields.io/badge/SocioBuzz-Support_Local-FFA500?style=for-the-badge" alt="Support on SocioBuzz"></a>
  <a href="https://saweria.co/dasik"><img src="https://img.shields.io/badge/Saweria-Support_Local-F6C90E?style=for-the-badge" alt="Support on Saweria"></a>
</p>

---

## 📜 Credits & Licensing

| Attribute | Details |
| :--- | :--- |
| **Creator / Developer** | Dasik (Rifaditya) |
| **Collection** | Vanilla Outsider Collection |
| **License** | [GNU General Public License v3.0 (GPLv3)](https://www.gnu.org/licenses/gpl-3.0.html) |
| **Modpack Permission** | 100% Free to include in any public or private modpack without asking. |

**1 Jar 1 Version Policy:** I build **1 dedicated JAR for each Minecraft version** (e.g. MC 1.20.1, MC 1.21.1, MC 1.21.11, MC 26.1.2, MC 26.2, MC 26.3). Please download the exact build that matches your Minecraft installation.

<p align="center">
  <strong>Made with ❤️ for the Minecraft community</strong><br>
  <em>Part of the Vanilla Outsider Collection</em>
</p>
