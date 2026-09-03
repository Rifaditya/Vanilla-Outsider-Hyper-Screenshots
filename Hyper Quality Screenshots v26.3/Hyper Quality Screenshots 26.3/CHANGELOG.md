# Changelog

## [1.1.3+26.3]

### Added
- Expanded automated unit test suite (`ConfigSerializationTest`) verifying live `autoHideHand` toggle mutations, JSON serialization roundtrips, and capture manager state invariants.

## [1.1.2+26.3]

### Added
- Standardized translatable notification strings (`hyperscreenshots.notification.auto_hide_hand.enabled` and `disabled`) in `en_us.json` for the Auto-Hide Hand live toggle.

## [1.1.1+26.3]

### Added
- Live `Alt + F2` keyboard shortcut to toggle Auto-Hide Hand on the fly during gameplay with instant chat confirmation and persistent config saving.

## [1.1.0+26.3]

### Added
- Functional Auto-Hide Hand capture engine: Injects into first-person item rendering pipeline to seamlessly suppress hands and held items during active screenshot passes when `autoHideHand` is enabled.
- Dedicated render pass support for native `NORMAL` preset captures when hiding HUD or hands.

## [1.0.0+26.3] - 2026-08-26

### Added
- Complete initial release of Hyper Quality Screenshots on Minecraft 26.3.
- Multi-resolution supersampling engine with Normal, 2K (1440p), 4K (2160p), 8K (4320p), 16K (8640p), and custom multipliers ($1\times - 16\times$).
- Tiled frustum rendering engine for 8K/16K to prevent GPU VRAM exhaustion and OS TDR driver timeouts.
- Isolated offscreen `RenderTarget` (FBO) capture pipeline with zero client window resizing or flickering.
- Asynchronous non-blocking PNG disk writer on `Util.ioPool()` with interactive clickable chat notifications and success audio chimes.
- Dual keybind architecture: `F2` for active YACL preset and `Ctrl + F2` for instant 16K max capture.
- Pure client-side YetAnotherConfigLib (YACL v3) & ModMenu configuration interface (**strictly zero GameRules**).
