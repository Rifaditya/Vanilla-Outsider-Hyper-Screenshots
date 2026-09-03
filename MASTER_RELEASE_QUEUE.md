# 🎛️ Master Release Queue: Vanilla Outsider — Hyper Quality Screenshots

> **Mod Project Master Ground-Truth Document**  
> *Last Synchronized: 2026-09-01*  
> **Modrinth ID**: *Unregistered* | **CurseForge ID**: *Unregistered* | **Lead SemVer**: `1.0.0`

---

## 📊 Multi-Version Release Matrix & Queue Status

| Target MC | Generational Era | Live on Platforms | Next Queued Version | Status & Cadence Action | Feature Highlights / Notes |
| :--- | :--- | :--- | :--- | :--- | :--- |
| **MC 26.3** | Modern Lead | *(Unreleased)* | `1.0.0+26.3` | 🟢 **Ready to Publish** | Next release: `1.0.0+26.3` (`1.1.0+26.3`, `1.1.1+26.3`, `1.1.2+26.3`, `1.1.3+26.3`, `1.1.4+26.3` queued next). |
| **MC 26.2** | Modern Predecessor | *(Unreleased)* | `1.0.0+26.2` | 🟢 **Ready to Publish** | Next release: `1.0.0+26.2` (`1.1.0+26.2`, `1.1.1+26.2`, `1.1.2+26.2`, `1.1.3+26.2`, `1.1.4+26.2` queued next). |
| **MC 26.1** | Modern Predecessor | *(Unreleased)* | `1.0.0+26.1.2` | 🟢 **Ready to Publish** | Next release: `1.0.0+26.1.2` (`1.1.0+26.1.2`, `1.1.1+26.1.2`, `1.1.2+26.1.2`, `1.1.3+26.1.2`, `1.1.4+26.1.2` queued next). |
| **MC 1.21.11** | Legacy Anchor | *(Unreleased)* | `1.0.0+1.21.11` | 🟢 **Ready to Publish** | Next release: `1.0.0+1.21.11` (`1.1.0+1.21.11`, `1.1.1+1.21.11`, `1.1.2+1.21.11`, `1.1.3+1.21.11`, `1.1.4+1.21.11` queued next). |
| **MC 1.21.1** | Legacy Anchor | *(Unreleased)* | `1.0.0+1.21.1` | 🟢 **Ready to Publish** | Next release: `1.0.0+1.21.1` (`1.1.0+1.21.1`, `1.1.1+1.21.1`, `1.1.2+1.21.1`, `1.1.3+1.21.1`, `1.1.4+1.21.1` queued next). |
| **MC 1.20.1** | Legacy Anchor | *(Unreleased)* | `1.0.0+1.20.1` | 🟢 **Ready to Publish** | Next release: `1.0.0+1.20.1` (`1.1.0+1.20.1`, `1.1.1+1.20.1`, `1.1.2+1.20.1`, `1.1.3+1.20.1`, `1.1.4+1.20.1` queued next). |

---

## 🏛️ Project Operating Rules & Architectural Invariants

1. **🏛️ Generational Era Separation**:
   - **Modern Stream (`MC 26.x`)** and **Legacy Stream (`MC 1.20.1`, `MC 1.21.x`)** operate with complete release autonomy under the 1 Jar 1 Version Law.
   - Modern holds never block Legacy releases, and Legacy releases never block Modern releases.

2. **🔢 Universal Direct SemVer Inheritance**:
   - Modern subprojects share unified SemVer milestone lineage targeting `1.0.0`.
   - Each Minecraft version anchor manages its own organic progression to ensure 100% clean, verified parity.

3. **📅 Daily Update Guard**:
   - Strict maximum of 1 release per day per targeted Minecraft version anchor across Modrinth and CurseForge.

---

## 🛠️ CLI Publisher Commands for Hyper Quality Screenshots

```powershell
# 1. Check current status across all targeted Minecraft versions
python ".agents/skills/platform-publisher/scripts/platform_publisher.py" --mod "Hyper Quality Screenshots" --status

# 2. Publish next sequential batch across all active versions
python ".agents/skills/platform-publisher/scripts/platform_publisher.py" --mod "Hyper Quality Screenshots" --publish-next --yes

# 3. Publish for a specific version anchor only (e.g. MC 26.3)
python ".agents/skills/platform-publisher/scripts/platform_publisher.py" --mod "Hyper Quality Screenshots" --mc 26.3 --publish-next --yes
```
