# Changelog

## [1.1.7+1.21.11]

### Added
- Integrated OpenGL hardware bounds checking and automatic safety clamping in `HyperCaptureManager`, querying `RenderSystem.maxSupportedTextureSize()` and safely clamping oversized resolutions preserving aspect ratio to prevent OpenGL driver crashes.
- Added translatable notification `hyperscreenshots.notification.hardware_clamped` informing the user when requested resolution exceeds hardware texture bounds.

## [1.1.6+1.21.11]

### Added
- Implemented capture concurrency guard and 300ms keypress debounce in `HyperCaptureManager` and `KeyboardMixin`, preventing rapid key bounce and auto-repeat from triggering overlapping supersampled render passes.

## [1.1.5+1.21.11]

### Fixed
- Hardened configuration file saving with atomic file replacement (`.tmp` staging + `StandardCopyOption.ATOMIC_MOVE`), eliminating truncated or corrupted configuration files on sudden game exit.

## [1.1.4+1.21.11]

### Fixed
- Hardened asynchronous screenshot save dispatch with a strict executor rejection guard, guaranteeing immediate `NativeImage` off-heap memory deallocation if background thread execution fails or during JVM shutdown.

## [1.1.3+1.21.11]

### Added
- Expanded automated unit test suite (`ConfigSerializationTest`) verifying live `autoHideHand` toggle mutations, JSON serialization roundtrips, and capture manager state invariants.

## [1.1.2+1.21.11]

### Added
- Standardized translatable notification strings (`hyperscreenshots.notification.auto_hide_hand.enabled` and `disabled`) in `en_us.json` for the Auto-Hide Hand live toggle.

## [1.1.1+1.21.11]

### Added
- Live `Alt + F2` keyboard shortcut to toggle Auto-Hide Hand on the fly during gameplay with instant chat confirmation and persistent config saving.

## [1.1.0+1.21.11]

### Added
- Functional Auto-Hide Hand capture engine: Injects into first-person item rendering pipeline to seamlessly suppress hands and held items during active screenshot passes when `autoHideHand` is enabled.
- Dedicated render pass support for native `NORMAL` preset captures when hiding HUD or hands.

## [1.0.0+1.21.11] - 2026-08-26

### Added
- Complete initial release of Hyper Quality Screenshots on Minecraft 1.21.11.
- Multi-resolution supersampling engine with Normal, 2K (1440p), 4K (2160p), 8K (4320p), 16K (8640p), and custom multipliers ($1\times - 16\times$).
- Tiled frustum rendering engine for 8K/16K to prevent GPU VRAM exhaustion and OS TDR driver timeouts.
- Isolated offscreen `RenderTarget` (FBO) capture pipeline with zero client window resizing or flickering.
- Asynchronous non-blocking PNG disk writer on `Util.ioPool()` with interactive clickable chat notifications and success audio chimes.
- Dual keybind architecture: `F2` for active YACL preset and `Ctrl + F2` for instant 16K max capture.
- Pure client-side YetAnotherConfigLib (YACL v3) & ModMenu configuration interface (**strictly zero GameRules**).
