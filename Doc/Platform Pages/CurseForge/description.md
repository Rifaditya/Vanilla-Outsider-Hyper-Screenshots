<p align="center">
  <a href="https://fabricmc.net/"><img src="https://img.shields.io/badge/Fabric-API_Required-blue?style=for-the-badge&logo=fabric" alt="Fabric API Required"></a>
  <a href="https://www.java.com/"><img src="https://img.shields.io/badge/Language-Java-orange?style=for-the-badge&logo=openjdk" alt="Language Java"></a>
  <a href="https://www.gnu.org/licenses/gpl-3.0.html"><img src="https://img.shields.io/badge/License-GPLv3-green?style=for-the-badge" alt="License GPLv3"></a>
  <a href="https://curseforge.com/minecraft/mc-mods/hyper-screenshots"><img src="https://img.shields.io/badge/Minecraft-1.20.1_to_26.3+-brightgreen?style=for-the-badge" alt="Minecraft 1.20.1 to 26.3+"></a>
</p>

<h2>📸 Hyper Quality Screenshots</h2>

<blockquote>
  <strong>"Capture crystal-clear 2K, 4K, 8K, and 16K ultra-high-resolution screenshots without window resizing, screen freezing, or GPU crashes."</strong>
</blockquote>

<p>Have you ever lined up the most breathtaking view of your base or landscape in Minecraft, pressed <strong>F2</strong>, only to find out the resulting screenshot is locked to your monitor's display resolution with jagged edges and pixelated distant terrain?</p>

<p><strong>Hyper Quality Screenshots</strong> completely revamps Minecraft's screenshot capture pipeline. By combining isolated offscreen framebuffer supersampling, chunked sub-frustum tiled rendering, and asynchronous non-blocking background disk writing, you can now take stunning, wallpaper-grade <strong>2K (1440p)</strong>, <strong>4K (2160p)</strong>, <strong>8K (4320p)</strong>, and <strong>16K (8640p)</strong> screenshots with zero client window flickering or lag.</p>

<p>Part of the <strong>Vanilla Outsider Collection</strong> &mdash; modern, non-intrusive quality-of-life enhancements designed to integrate seamlessly into vanilla gameplay.</p>

<hr>

<h2>✨ Features & Architecture</h2>

<h3>🖼️ Multi-Resolution Supersampling Engine</h3>
<p>Take high-resolution screenshots far beyond your physical monitor's display limits:</p>
<ul>
  <li><strong>Normal</strong>: Standard native display resolution capture.</li>
  <li><strong>2K Quad HD (1440p)</strong>: 2560 &times; 1440 baseline resolution (or 1.33&times; aspect-scaled).</li>
  <li><strong>4K Ultra HD (2160p)</strong>: 3840 &times; 2160 crisp wallpaper standard.</li>
  <li><strong>8K Super UHD (4320p)</strong>: 7680 &times; 4320 ultra-detailed photography.</li>
  <li><strong>16K Extreme QUHD (8640p)</strong>: 15360 &times; 8640 maximum quality for large prints and panoramic showcases.</li>
  <li><strong>Custom Multiplier</strong>: Freely customize supersampling from 1.0&times; up to 16.0&times; scale.</li>
</ul>

<h3>🧩 Tiled Frustum Grid Rendering (Anti-Crash & Anti-TDR)</h3>
<p>Rendering an uncompressed 16K frame directly in a single GPU pass requires over 500MB of raw framebuffer memory, often causing GPU driver timeouts (TDR) and out-of-memory crashes on mid-range hardware.</p>
<ul>
  <li><strong>Smart Grid Slicing</strong>: 8K captures are automatically rendered across a 2 &times; 2 grid (4 tiles), and 16K captures across a 4 &times; 4 grid (16 tiles).</li>
  <li><strong>Sub-Frustum Projection</strong>: Each tile renders a precise sub-frustum viewport bounding box in normalized device coordinates [-1, 1].</li>
  <li><strong>Seamless Pixel Stitching</strong>: Sub-tiles are composited into the final buffer with zero visible seams or distortion.</li>
</ul>

<h3>🛡️ Isolated Offscreen Framebuffer (FBO)</h3>
<ul>
  <li>Screenshots are rendered directly into a dedicated offscreen <code>RenderTarget</code>.</li>
  <li><strong>Zero Window Flickering</strong>: Your Minecraft game window and HUD never resize, shake, or stretch during capture.</li>
</ul>

<h3>⚡ Non-Blocking Asynchronous Disk Writing</h3>
<ul>
  <li>PNG compression and disk I/O are offloaded entirely to background worker threads.</li>
  <li>Instant return to gameplay with zero input freeze.</li>
  <li>Chat notification provides a clickable file link to open the saved screenshot directly in your OS image viewer.</li>
  <li>Optional experience orb audio chime confirms successful save.</li>
</ul>

<h3>⌨️ Dual Keybind Architecture</h3>
<ul>
  <li><strong>F2</strong>: Captures a screenshot using your configured active preset in ModMenu / YACL.</li>
  <li><strong>Ctrl + F2</strong>: Instant shortcut to take a maximum 16K Extreme QUHD screenshot on demand.</li>
</ul>

<h3>🎛️ Dedicated Compatibility & Gated Client UI</h3>
<ul>
  <li><strong>ModMenu & YetAnotherConfigLib (YACL v3)</strong>: Fully configurable via in-game settings GUI.</li>
  <li><strong>Strictly Zero GameRules</strong>: Pure client-side mod that works on any multiplayer server without server-side installation.</li>
  <li><strong>Classloading Shielding</strong>: Uses isolated screen reflection helpers to ensure crash-free execution on headless dedicated servers and vanilla clients.</li>
</ul>

<hr>

<h2>📊 Resolution & Grid Reference Matrix</h2>

<table border="1" cellpadding="6" cellspacing="0">
  <thead>
    <tr>
      <th>Preset</th>
      <th>Target Resolution (16:9)</th>
      <th>Supersampling Multiplier</th>
      <th>Tile Grid</th>
      <th>VRAM Allocation Safety</th>
    </tr>
  </thead>
  <tbody>
    <tr>
      <td><strong>Normal</strong></td>
      <td>Native Window</td>
      <td>1.0&times;</td>
      <td>1 &times; 1</td>
      <td>Native / Zero Overhead</td>
    </tr>
    <tr>
      <td><strong>2K (QHD)</strong></td>
      <td>2560 &times; 1440</td>
      <td>~1.33&times;</td>
      <td>1 &times; 1</td>
      <td>Safe for all GPUs</td>
    </tr>
    <tr>
      <td><strong>4K (UHD)</strong></td>
      <td>3840 &times; 2160</td>
      <td>~2.0&times;</td>
      <td>1 &times; 1</td>
      <td>Safe for modern GPUs</td>
    </tr>
    <tr>
      <td><strong>8K (FUHD)</strong></td>
      <td>7680 &times; 4320</td>
      <td>~4.0&times;</td>
      <td>2 &times; 2 (4 tiles)</td>
      <td>Tiled / Prevents VRAM Spikes</td>
    </tr>
    <tr>
      <td><strong>16K (QUHD)</strong></td>
      <td>15360 &times; 8640</td>
      <td>~8.0&times;</td>
      <td>4 &times; 4 (16 tiles)</td>
      <td>Tiled / Crash-Free Memory Safety</td>
    </tr>
    <tr>
      <td><strong>Custom</strong></td>
      <td>Dynamic (W &times; H)</td>
      <td>1.0&times; &ndash; 16.0&times;</td>
      <td>Dynamic</td>
      <td>Auto-computed grid</td>
    </tr>
  </tbody>
</table>

<hr>

<h2>📖 In-Depth How-To & Operational Playbook</h2>

<h3>1. Installation & Drop-In Setup</h3>
<ol>
  <li>Download the build matching your Minecraft version (<code>1.20.1</code>, <code>1.21.1</code>, <code>1.21.11</code>, <code>26.1.2</code>, <code>26.2</code>, or <code>26.3</code>).</li>
  <li>Place the <code>.jar</code> into your Minecraft <code>.minecraft/mods</code> directory.</li>
  <li>Install <strong>Fabric API</strong>.</li>
  <li><em>(Optional but Recommended)</em> Install <strong>ModMenu</strong> and <strong>YetAnotherConfigLib (YACL)</strong> to access the configuration screen.</li>
</ol>

<h3>2. Operating the Capture Pipeline</h3>
<ul>
  <li><strong>Standard Capture</strong>: Press <strong>F2</strong> to capture with your chosen preset (defaults to <strong>4K UHD</strong>).</li>
  <li><strong>Instant 16K Super-Resolution</strong>: Press <strong>Ctrl + F2</strong> anywhere in-game to immediately take a 16K screenshot.</li>
  <li><strong>Auto-Hide HUD</strong>: Enable <code>Auto-Hide HUD</code> in config to automatically hide player health, hotbar, and chat during screenshot capture without manually pressing F1.</li>
</ul>

<h3>3. File Location & Notifications</h3>
<ul>
  <li>Screenshots are saved to <code>.minecraft/screenshots/</code> with unique timestamp and resolution tags (e.g. <code>2026-08-26_11.30.00_4k.png</code>, <code>2026-08-26_11.30.05_16k.png</code>).</li>
  <li>Clicking the green underlined filename in chat will open the image file in your system's default photo viewer.</li>
</ul>

<hr>

<h2>☕ Support the Developer</h2>

<p>I create and maintain high-performance, quality-of-life Minecraft mods independently. If you enjoy my work, consider supporting development!</p>

<p align="center">
  <a href="https://ko-fi.com/dasik"><img src="https://img.shields.io/badge/Ko--fi-Support_on_Kofi-FF5E5B?style=for-the-badge&logo=ko-fi&logoColor=white" alt="Support on Ko-fi"></a>
  <a href="https://sociobuzz.com/dasik/tribe"><img src="https://img.shields.io/badge/SocioBuzz-Support_Local-FFA500?style=for-the-badge" alt="Support on SocioBuzz"></a>
  <a href="https://saweria.co/dasik"><img src="https://img.shields.io/badge/Saweria-Support_Local-F6C90E?style=for-the-badge" alt="Support on Saweria"></a>
</p>

<hr>

<h2>📜 Credits & Licensing</h2>

<table border="1" cellpadding="6" cellspacing="0">
  <thead>
    <tr>
      <th>Attribute</th>
      <th>Details</th>
    </tr>
  </thead>
  <tbody>
    <tr>
      <td><strong>Creator / Developer</strong></td>
      <td>Dasik (Rifaditya)</td>
    </tr>
    <tr>
      <td><strong>Collection</strong></td>
      <td>Vanilla Outsider Collection</td>
    </tr>
    <tr>
      <td><strong>License</strong></td>
      <td><a href="https://www.gnu.org/licenses/gpl-3.0.html">GNU General Public License v3.0 (GPLv3)</a></td>
    </tr>
    <tr>
      <td><strong>Modpack Permission</strong></td>
      <td>100% Free to include in any public or private modpack without asking.</td>
    </tr>
  </tbody>
</table>

<p><strong>1 Jar 1 Version Policy:</strong> I build <strong>1 dedicated JAR for each Minecraft version</strong> (e.g. MC 1.20.1, MC 1.21.1, MC 1.21.11, MC 26.1.2, MC 26.2, MC 26.3). Please download the exact build that matches your Minecraft installation.</p>

<p align="center">
  <strong>Made with ❤️ for the Minecraft community</strong><br>
  <em>Part of the Vanilla Outsider Collection</em>
</p>
