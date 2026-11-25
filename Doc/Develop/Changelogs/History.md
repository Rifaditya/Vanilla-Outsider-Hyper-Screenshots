# 📜 Permanent Developer Ledger & Backlog History: Hyper Quality Screenshots

## [BL-HQS-007] Fix Direct ConfirmLinkScreen String Overload in YACL Ko-fi Fallback Across All & Future Anchors
- **Category**: `[BUGFIX]`
- **Priority**: `[HIGH]`
- **Status**: `✅ RESOLVED`
- **Target Version**: `1.2.4+<mc>` across all 6 active anchors (`1.20.1`, `1.21.1`, `1.21.11`, `26.1.2`, `26.2`, `26.3`) & baseline for future anchors (`26.x` / `26.4+`)
- **Target Component(s)**: `YaclScreenHelper.java`, `ModMenuIntegration.java`, `HyperScreenshotsClientContractTest.java`, `build.gradle`, `gradle.properties`, `CHANGELOG.md`, `RELEASE_QUEUE.md`
- **Date Added**: 2026-11-19
- **Date Resolved**: 2026-11-25
- **Resolution**:
  - **ConfirmLinkScreen URI Migration (`1.21.1`–`26.3`)**: Replaced the legacy/deleted `ConfirmLinkScreen.confirmLinkNow(screen, "https://ko-fi.com/dasikigaijin")` String overload in `YaclScreenHelper.createSupportButton()` with `ConfirmLinkScreen.confirmLinkNow(screen, URI.create("https://ko-fi.com/dasikigaijin"))` across `1.21.1`, `1.21.11`, `26.1.2`, `26.2`, and `26.3`.
  - **MC `1.20.1` Parity & Factory Alignment**: Retained `ConfirmLinkScreen.confirmLinkNow("https://ko-fi.com/dasikigaijin", screen, true)` in `Hyper Quality Screenshots v1.20.1` `YaclScreenHelper` and added `createFactory()` (`return createScreen();`) so ModMenu config screen invocation works reliably.
  - **ModMenu Side-Safety & Reflection Removal (`1.20.1`–`26.3`)**: Removed `Class.forName("...YaclScreenHelper")` reflection in `ModMenuIntegration` across all 6 anchors, replacing it with a `FabricLoader.getInstance().isModLoaded(...)` and `try/catch (Throwable)` guard around direct `YaclScreenHelper.createFactory()` dispatch with `@Environment(EnvType.CLIENT)` parity.
  - **MC `1.21.1` Legacy Mixin AP Refmap Restoration**: Restored legacy Mixin Annotation Processor refmap generation (`vanilla-outsider-hyper-screenshots-refmap.json`) in `Hyper Quality Screenshots v1.21.1` `build.gradle`.
  - **5-Pillar Client Contract Verification (`108/108` tests)**: Added `HyperScreenshotsClientContractTest` across all 6 active anchors covering Ko-fi URL/config fuzzing, concurrent config factory stress, translation keys & absent-YACL ModMenu fallback, reflection/fallback simulation, and pure-JDK `DataInputStream` bytecode constant-pool verification (confirming `(Lnet/minecraft/client/gui/screens/Screen;Ljava/net/URI;)V` and zero `(Lnet/minecraft/client/gui/screens/Screen;Ljava/lang/String;)V` on `1.21.1`–`26.3`, plus `(Ljava/lang/String;Lnet/minecraft/client/gui/screens/Screen;Z)V` on `1.20.1`).
