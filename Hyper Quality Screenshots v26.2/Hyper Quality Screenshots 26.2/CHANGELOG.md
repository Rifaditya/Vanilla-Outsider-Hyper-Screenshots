# Changelog

## [1.0.1+26.2] - 2026-08-26

### Added
- Created `fabric.mod.json` client manifest declaring `"environment": "client"` and open dependency bounds `">=26.2-"`.
- Implemented `HyperScreenshotsClient` Fabric client entrypoint with Knot classloader `ModVersionGuard`.
- Configured mixins definition `hyper-screenshots.mixins.json` at `JAVA_25`.
- Added standard assets and baseline `en_us.json` localization.

## [1.0.0+26.2] - 2026-08-26

### Added
- Initial subproject toolchain scaffolding targeting Minecraft 26.2.
- Configured Fabric Loom 1.15+ with Java 25 toolchain and non-obfuscated runtime.
- Added compile-only dependency hooks for YetAnotherConfigLib v3 and ModMenu.
- Added automated release archive lifecycle task copying to `Archive Jar of all versions/MC 26.2/`.
