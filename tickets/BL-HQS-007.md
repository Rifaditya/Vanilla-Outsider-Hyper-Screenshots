# 🎫 [BL-HQS-007] Fix Direct ConfirmLinkScreen String Overload in YACL Ko-fi Fallback Across All & Future Anchors

- **Ticket ID**: `[BL-HQS-007]`
- **Category**: `[BUGFIX]`
- **Priority**: `[HIGH]`
- **Target Version**: `All Active Anchors (1.20.1, 1.21.1, 1.21.11, 26.1.2, 26.2, 26.3) & Future Anchors (26.x / 26.4+ / 2x.x+)`
- **Scope**: `YaclScreenHelper.java` (`createSupportButton()` fallback lambda + `1.20.1` `createFactory()` parity) & `ModMenuIntegration.java` (remove `Class.forName`) across all HQS anchors + client 5-pillar bytecode verification test
- **Status**: `✅ RESOLVED`
- **Grilling Status**: `✅ PASSED` *(Stage 1.5 Live-Code Reality Check & Director Alignment Complete — 2026-11-25)*
- **Target Repository/Root**: `Vanilla Outsider Collections/Hyper Quality Screenshots` (`E:\Minecraft Project\Vanilla Outsider Collections\Hyper Quality Screenshots`)
- **Related Tickets**: [`BL-DASIK-023`](file:///E:/Minecraft%20Project/MC-DasikLibrary/tickets/BL-DASIK-023.md) (primary Dasik Library root fix), [`BL-HQS-004`](./BL-HQS-004.md) (`UniversalConfigScreen` migration), [`BL-HQS-005`](./BL-HQS-005.md) (Anchor 7 `26.x` rolling scaffold)
- **Governance Mandate**: [Minecraft Modern Sovereign Anchors Policy](file:///E:/Minecraft%20Project/.agents/rules/Minecraft%20Modern%20Sovereign%20Anchors%20Policy.md) §6 (`ConfirmLinkScreen.confirmLinkNow(Screen parent, URI uri)`), [Optional GUI Dependencies](file:///E:/Minecraft%20Project/.agents/rules/Optional%20GUI%20Dependencies.md), [Single-Ticket Release Policy](file:///E:/Minecraft%20Project/.agents/rules/Single-Ticket%20Release%20Policy.md)
- **Date Added**: `2026-11-19`

---

## 🎯 1. Goal & Definition of Done (The 4-Pillar Objective Architecture)

1. **North Star Objective**: Replace the deleted/legacy `ConfirmLinkScreen.confirmLinkNow(screen, "https://ko-fi.com/dasikigaijin")` String overload in `YaclScreenHelper.createSupportButton()` with `ConfirmLinkScreen.confirmLinkNow(screen, URI.create("https://ko-fi.com/dasikigaijin"))` across all `1.21.1`–`26.3` active anchors and future `26.x` / `26.4+` / `2x.x+` anchors (retaining `1.20.1`'s valid 3-arg legacy call) so rebuilding against MC 26.3+ compiles cleanly and the Ko-fi fallback button opens `ConfirmLinkScreen` reliably if Dasik Library's reflection path is bypassed or absent.
2. **In-Game Experience (Before vs. After)**:
   - *Before*: On MC `26.3`+, `YaclScreenHelper.java:147` calls the deleted `ConfirmLinkScreen.confirmLinkNow(Screen, String)` method, breaking compilation against release MC `26.3` and throwing `NoSuchMethodError` if the fallback `ButtonOption` click handler executes at runtime.
   - *After*: `YaclScreenHelper` compiles cleanly across `1.20.1`–`26.3` and future `26.4+` / `2x.x+` anchors, and clicking the Ko-fi button reliably opens `ConfirmLinkScreen` for `https://ko-fi.com/dasikigaijin` whether handled by `DasikSupportHelper` or the local fallback.
3. **🚫 Anti-Goals (Off-Limits / Do Not Touch)**:
   - ❌ Do NOT perform the full `UniversalConfigScreen` refactor here — that belongs strictly to [`BL-HQS-004`](./BL-HQS-004.md) under the Single-Ticket Release Policy.
   - ❌ Do NOT scaffold the full `Hyper Quality Screenshots v26.x/` subproject directory here — that belongs strictly to [`BL-HQS-005`](./BL-HQS-005.md) (though [`BL-HQS-005`](./BL-HQS-005.md) MUST inherit the `URI.create(...)` fix).
   - ❌ Do NOT touch tiled framebuffer rendering, screenshot capture math, HUD/hand hiding mixins, or keybinding logic.
4. **✅ Definition of Done (Binary Verification Checklist)**:
   - [x] `YaclScreenHelper.java` invokes `ConfirmLinkScreen.confirmLinkNow(screen, URI.create("https://ko-fi.com/dasikigaijin"))` on `1.21.1`, `1.21.11`, `26.1.2`, `26.2`, and `26.3` (and is documented as mandatory baseline for future `26.x` / `26.4+` scaffolds).
   - [x] `Hyper Quality Screenshots v1.20.1` `YaclScreenHelper.java:148` is verified intact on `ConfirmLinkScreen.confirmLinkNow("https://ko-fi.com/dasikigaijin", screen, true)`.
   - [x] Client-only headless bytecode/AST test verifies zero `(Lnet/minecraft/client/gui/screens/Screen;Ljava/lang/String;)V` calls in `YaclScreenHelper.class` on `1.21.1+` and `26.x+` anchors.
   - [x] All active HQS anchors compile cleanly and pass tests (`./gradlew.bat test build --no-daemon --console=plain`).

---

## ❓ 2. Problem Statement & Player Impact

During the root-cause audit for [`BL-DASIK-023`](file:///E:/Minecraft%20Project/MC-DasikLibrary/tickets/BL-DASIK-023.md), workspace-wide scanning found that **Hyper Quality Screenshots** still directly calls the String overload deleted in MC `26.3` in `YaclScreenHelper.createSupportButton()` (`Velocity Render` already uses `URI.create(...)` on `1.21.1`–`26.4`, and `Camera Culling` uses it on `1.21.1`, `1.21.11`, and `26.3`; `Camera Culling v26.1`/`v26.2` remain to be filed in Stage 5):

- `Hyper Quality Screenshots v1.20.1/Hyper Quality Screenshots 1.20.1/src/main/java/net/vanillaoutsider/hyperscreenshots/config/YaclScreenHelper.java:148` → `ConfirmLinkScreen.confirmLinkNow("https://ko-fi.com/dasikigaijin", screen, true)` (valid for 1.20.1; however `ModMenuIntegration.java:20` invokes `getMethod("createFactory")` which does not exist in `1.20.1` `YaclScreenHelper`, causing the config button to return `null`).
- `Hyper Quality Screenshots v1.21.1/Hyper Quality Screenshots 1.21.1/src/main/java/net/vanillaoutsider/hyperscreenshots/config/YaclScreenHelper.java:130` → `ConfirmLinkScreen.confirmLinkNow(screen, "https://ko-fi.com/dasikigaijin")` (legacy String overload).
- `Hyper Quality Screenshots v1.21.11/Hyper Quality Screenshots 1.21.11/src/main/java/net/vanillaoutsider/hyperscreenshots/config/YaclScreenHelper.java:130` → `ConfirmLinkScreen.confirmLinkNow(screen, "https://ko-fi.com/dasikigaijin")` (legacy String overload).
- `Hyper Quality Screenshots v26.1/Hyper Quality Screenshots 26.1.2/src/main/java/net/vanillaoutsider/hyperscreenshots/config/YaclScreenHelper.java:144` → `ConfirmLinkScreen.confirmLinkNow(screen, "https://ko-fi.com/dasikigaijin")` (legacy String overload).
- `Hyper Quality Screenshots v26.2/Hyper Quality Screenshots 26.2/src/main/java/net/vanillaoutsider/hyperscreenshots/config/YaclScreenHelper.java:147` → `ConfirmLinkScreen.confirmLinkNow(screen, "https://ko-fi.com/dasikigaijin")` (legacy String overload).
- `Hyper Quality Screenshots v26.3/Hyper Quality Screenshots 26.3/src/main/java/net/vanillaoutsider/hyperscreenshots/config/YaclScreenHelper.java:147` → `ConfirmLinkScreen.confirmLinkNow(screen, "https://ko-fi.com/dasikigaijin")` (**DELETED in MC 26.3** — fails compilation against release 26.3 `ConfirmLinkScreen.java` and throws `NoSuchMethodError` if the fallback runs).

---

## 🔬 3. Architectural Blueprint & Target Classes

1. **Target Class (`1.21.1`, `1.21.11`, `26.1.2`, `26.2`, `26.3`)**:
   - `net.vanillaoutsider.hyperscreenshots.config.YaclScreenHelper` (`createSupportButton()` fallback block)
   - Add `import java.net.URI;`
   - Replace:
     ```java
     .action((screen, opt) -> ConfirmLinkScreen.confirmLinkNow(screen, "https://ko-fi.com/dasikigaijin"))
     ```
     with:
     ```java
     .action((screen, opt) -> ConfirmLinkScreen.confirmLinkNow(screen, URI.create("https://ko-fi.com/dasikigaijin")))
     ```
2. **Legacy Target Class (`1.20.1`)**:
   - Keep `ConfirmLinkScreen.confirmLinkNow("https://ko-fi.com/dasikigaijin", screen, true)` intact (`1.20.1` has no `(Screen, URI)` overload) and guard via existing `try/catch`.
3. **Note on `26.3` `gradle.properties`**:
   - `Hyper Quality Screenshots v26.3/Hyper Quality Screenshots 26.3/gradle.properties` currently lists `yacl_version=3.8.1+1.21.4-fabric` and `dasik_library_version=1.9.2` (with `yarn_mappings=26.3-snapshot-6+build.1`). Verify local compilation compatibility during execution without broadening scope into an unrelated refactor.

---

## ⚓ 4. Multi-Era Anchor Parity Specifications

| Anchor | On-Disk Subproject | `ConfirmLinkScreen` Signature | Target State |
| :--- | :--- | :--- | :--- |
| **Anchor 1 (`MC 1.20.1`)** | `Hyper Quality Screenshots v1.20.1/Hyper Quality Screenshots 1.20.1` | `confirmLinkNow(String, Screen, boolean)` | Retain `(String, Screen, true)` at `YaclScreenHelper.java:148`; verify compilation & bytecode presence. |
| **Anchor 2 (`MC 1.21.1`)** | `Hyper Quality Screenshots v1.21.1/Hyper Quality Screenshots 1.21.1` | `confirmLinkNow(Screen, URI)` | Migrate line 130 from `(screen, String)` to `(screen, URI.create(...))`. |
| **Anchor 3 (`MC 1.21.11`)** | `Hyper Quality Screenshots v1.21.11/Hyper Quality Screenshots 1.21.11` | `confirmLinkNow(Screen, URI)` | Migrate line 130 from `(screen, String)` to `(screen, URI.create(...))`. |
| **Anchor 4 (`MC 26.1.2`)** | `Hyper Quality Screenshots v26.1/Hyper Quality Screenshots 26.1.2` | `confirmLinkNow(@Nullable Screen, URI)` | Migrate line 144 from `(screen, String)` to `(screen, URI.create(...))`. |
| **Anchor 5 (`MC 26.2`)** | `Hyper Quality Screenshots v26.2/Hyper Quality Screenshots 26.2` | `confirmLinkNow(@Nullable Screen, URI)` | Migrate line 147 from `(screen, String)` to `(screen, URI.create(...))`. |
| **Anchor 6 (`MC 26.3`)** | `Hyper Quality Screenshots v26.3/Hyper Quality Screenshots 26.3` | `confirmLinkNow(@Nullable Screen, URI)` | Migrate line 147 from deleted `(screen, String)` to `(screen, URI.create(...))`. |
| **Anchor 7 & Future (`MC 26.x`, `26.4+`, `2x.x+`)** | Future scaffold (`BL-HQS-005` & beyond) | `confirmLinkNow(@Nullable Screen, URI)` | Verified intact in `26.4 snapshot 5 decompile/.../ConfirmLinkScreen.java:79-81`. `BL-HQS-005` rolling scaffold MUST inherit the `URI.create(...)` call (or migrate cleanly via `BL-HQS-004` `UniversalConfigScreen`) plus Pillar 5 bytecode descriptor check. |

---

## 🛡️ 5. Edge Cases, Collisions & Failure Modes

| Dimension | Risk / Failure Mode | Blast Radius | Mitigation & Safeguard Built-In |
| :--- | :--- | :--- | :--- |
| **Dasik Library Reflection Failure / Absence** | Local fallback `ButtonOption` executes instead of `DasikSupportHelper.createYaclButton()` | Mod config Ko-fi button | Fixed `URI.create(...)` call ensures fallback opens `ConfirmLinkScreen` without `NoSuchMethodError`. |
| **YACL Optional Dependency Absent** | `ModMenuIntegration` invokes `YaclScreenHelper` without YACL installed | Client config screen | Guard via `FabricLoader.isModLoaded(...)` and `try/catch (Throwable)` without `Class.forName`, returning `parent -> null` cleanly when YACL is absent. |
| **Client-Only Isolation** | Server accidentally loading GUI helper | Zero (`"environment": "client"` mod) | Verified `"environment": "client"` in `fabric.mod.json`; preserve `YaclScreenHelper` client reflection boundary. |
| **Screenshot Pipeline / World Saves** | None | Zero | Touches only the Ko-fi button callback in `YaclScreenHelper`. |

---

## 🚀 6. Implementation Steps (Code-Level Plan)

1. **Step 0 (Quarantine & Build Hygiene — Zero-Bump)**: Fold pending working-tree backlog/changelog edits (`BACKLOG.md`, `tickets/*`, 6× `CHANGELOG.md`), purge dead `util/ModVersionGuard.java` across all 6 anchors, remove unused banned `yarn_mappings=26.3-snapshot-6+build.1` from `26.3/gradle.properties`, and fix `26.1.2/build.gradle` (`MC 26.1` -> `MC 26.1.2`) and `26.3/build.gradle` (`Fabric 26.3ish` -> `26.3` & `Fabric 26.3`).
2. **Step 1 (`1.2.4+<mc>` Release)**:
   - Update `YaclScreenHelper.java` in `26.3`, `26.2`, `26.1.2`, `1.21.11`, and `1.21.1` to import `java.net.URI` and call `ConfirmLinkScreen.confirmLinkNow(screen, URI.create("https://ko-fi.com/dasikigaijin"))`.
   - Retain `1.20.1` `YaclScreenHelper.java:148` `(String, Screen, boolean)` and add `createFactory()` parity (`return createScreen();`).
   - Replace `Class.forName("...YaclScreenHelper")` in `ModMenuIntegration.java` across all 6 anchors with guarded direct `YaclScreenHelper.createFactory()` wrapped in `try/catch (Throwable)` and ensure `@Environment(EnvType.CLIENT)` parity across `1.20.1`–`26.3`.
   - Add Client-Only 5-Pillar `HyperScreenshotsClientContractTest.java` across all 6 anchors verifying config fuzzing, concurrency, translation keys & ModMenu absent-YACL fallback, `createFactory()` parity, and pure-JDK `DataInputStream` bytecode constant-pool descriptor (`(Lnet/minecraft/client/gui/screens/Screen;Ljava/net/URI;)V` present and ZERO `(Lnet/minecraft/client/gui/screens/Screen;Ljava/lang/String;)V` on `1.21.1`–`26.3`, plus `(Ljava/lang/String;Lnet/minecraft/client/gui/screens/Screen;Z)V` on `1.20.1`).
   - Forward-calibrate `mod_version=1.2.4+<mc>` (`max(Lead 1.2.4, Current + 1)`) in all 6 anchors, run `./gradlew.bat test build --no-daemon --console=plain`, deploy via 5-Point distribution, update `CHANGELOG.md` & `RELEASE_QUEUE.md`, and lockstep push.

---

## 🏁 7. 5-Pillar Verification Checklist (Client-Only Adaptation)

> Because `Hyper Quality Screenshots` declares `"environment": "client"` in `fabric.mod.json`, Pillars 3–5 follow the Client-Only Adaptation per `backlog-and-ticket-templates.md` (zero server Brigadier commands or Mojang GameTests).

- [x] **Pillar 1 (Client Input & Config Safety Fuzzing)**: Fuzz Ko-fi URL / URI validation and `HyperScreenshotsConfig` bounds across 10,000+ randomized inputs without unhandled exceptions.
- [x] **Pillar 2 (Concurrency Load)**: Stress headless config screen factory / support button creation across concurrent threads without race conditions.
- [x] **Pillar 3 (Headless GUI & Translation AST Verification)**: `HyperScreenshotsClientContractTest` validates `dasiklibrary.support.kofi.button` / `dasiklibrary.support.kofi.tooltip` keys and `ModMenuIntegration` graceful fallback when YACL is absent.
- [x] **Pillar 4 (Headless Client State Simulation)**: Simulate both `DasikSupportHelper` reflection-present and fallback-only paths headlessly, confirming no uncaught exceptions or `NoClassDefFoundError`.
- [x] **Pillar 5 (Client Manifest, License Hygiene & Bytecode Descriptor Audit)**: Verify `"environment": "client"`, single-line GNU GPLv3 headers on all touched `.java` files, and `.class` bytecode check proving `YaclScreenHelper.class` invokes `ConfirmLinkScreen.confirmLinkNow` with `(Lnet/minecraft/client/gui/screens/Screen;Ljava/net/URI;)V` and ZERO `(Lnet/minecraft/client/gui/screens/Screen;Ljava/lang/String;)V` on `1.21.1`–`26.3`+ (clean `./gradlew.bat test build --no-daemon --console=plain` across all 6 active anchors).

---

## 🛡️ 8. Autonomous Goal Dependability & Self-Healing Contract

- **Verified On-Disk Targets**:
  - `E:\Minecraft Project\Vanilla Outsider Collections\Hyper Quality Screenshots\Hyper Quality Screenshots v1.20.1\Hyper Quality Screenshots 1.20.1` (`mod_version=1.2.2+1.20.1`)
  - `E:\Minecraft Project\Vanilla Outsider Collections\Hyper Quality Screenshots\Hyper Quality Screenshots v1.21.1\Hyper Quality Screenshots 1.21.1` (`mod_version=1.2.1+1.21.1`)
  - `E:\Minecraft Project\Vanilla Outsider Collections\Hyper Quality Screenshots\Hyper Quality Screenshots v1.21.11\Hyper Quality Screenshots 1.21.11` (`mod_version=1.2.2+1.21.11`)
  - `E:\Minecraft Project\Vanilla Outsider Collections\Hyper Quality Screenshots\Hyper Quality Screenshots v26.1\Hyper Quality Screenshots 26.1.2` (`mod_version=1.2.2+26.1.2`)
  - `E:\Minecraft Project\Vanilla Outsider Collections\Hyper Quality Screenshots\Hyper Quality Screenshots v26.2\Hyper Quality Screenshots 26.2` (`mod_version=1.2.3+26.2`)
  - `E:\Minecraft Project\Vanilla Outsider Collections\Hyper Quality Screenshots\Hyper Quality Screenshots v26.3\Hyper Quality Screenshots 26.3` (`mod_version=1.2.3+26.3`)
- **JDK & Process Hygiene**: Run Gradle with `--no-daemon --console=plain` and PowerShell `;` separators, matching each anchor's Java target (17 for 1.20.1, 21 for 1.21.x, 25 for 26.x).
- **Self-Healing Compile Budget**: Up to 1 localized retry if `26.3` mapping/YACL dependency resolution hits cache staleness; escalate via `enter_plan_mode` if a second compile attempt fails or requires expanding dependency pins beyond scope.
- **Continuation Re-Spawn**: Re-spawn seamlessly from the last green anchor if Gradle builds time out transiently.
