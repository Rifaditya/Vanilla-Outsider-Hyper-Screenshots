# Changelog

## [1.0.0+26.2] - 2026-08-26

### Added
- Complete initial release of Hyper Quality Screenshots on Minecraft 26.2.
- Multi-resolution supersampling engine with Normal, 2K (1440p), 4K (2160p), 8K (4320p), 16K (8640p), and custom multipliers ($1\times - 16\times$).
- Tiled frustum rendering engine for 8K/16K to prevent GPU VRAM exhaustion and OS TDR driver timeouts.
- Isolated offscreen `RenderTarget` (FBO) capture pipeline with zero client window resizing or flickering.
- Asynchronous non-blocking PNG disk writer on `Util.ioPool()` with interactive clickable chat notifications and success audio chimes.
- Dual keybind architecture: `F2` for active YACL preset and `Ctrl + F2` for instant 16K max capture.
- Pure client-side YetAnotherConfigLib (YACL v3) & ModMenu configuration interface (**strictly zero GameRules**).
