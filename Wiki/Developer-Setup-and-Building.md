# 🛠️ Developer Setup & Building Guide

🌐 **Languages**: [[🇺🇸 English|Home]] | [[🇨🇳 简体中文|zh_cn-Home]] | [[🇭🇰 繁體中文|zh_tw-Home]] | [[🇷🇺 Русский|ru_ru-Home]] | [[🇪🇸 Español|es_es-Home]] | [[🇩🇪 Deutsch|de_de-Home]] | [[🇫🇷 Français|fr_fr-Home]] | [[🇧🇷 Português|pt_br-Home]] | [[🇯🇵 日本語|ja_jp-Home]] | [[🇮🇩 Bahasa Indonesia|id_id-Home]] | [[🇰🇷 한국어|ko_kr-Home]]

> 📌 **Repository Source Disclaimer**: The documentation in this Wiki reflects the **current source code state in the repository**, which may include recent unreleased commits or developmental features ahead of public release builds on CurseForge and Modrinth.

## 1. Prerequisites & Toolchains

To build Hyper Quality Screenshots from source across its multi-version subprojects, ensure the following tools are installed:

* **Git**: For repository version control (`git clone`).
* **JDK 17**: For building `MC 1.20.1` subprojects.
* **JDK 21**: For building `MC 1.21.1` and `MC 1.21.11` subprojects.
* **JDK 25**: For building `MC 26.1`, `MC 26.2`, and `MC 26.3` modern subprojects.

---

## 2. Repository Cloning & Navigation

Clone the master repository:
```bash
git clone https://github.com/Rifaditya/Vanilla-Outsider-Hyper-Screenshots.git
cd Vanilla-Outsider-Hyper-Screenshots
```

Each supported Minecraft version is maintained in its own isolated subproject directory with a standalone `build.gradle` and Loom configuration:
* `Hyper Quality Screenshots v1.20.1/Hyper Quality Screenshots 1.20.1/`
* `Hyper Quality Screenshots v1.21.1/Hyper Quality Screenshots 1.21.1/`
* `Hyper Quality Screenshots v1.21.11/Hyper Quality Screenshots 1.21.11/`
* `Hyper Quality Screenshots v26.1/Hyper Quality Screenshots 26.1.2/`
* `Hyper Quality Screenshots v26.2/Hyper Quality Screenshots 26.2/`
* `Hyper Quality Screenshots v26.3/Hyper Quality Screenshots 26.3/`

---

## 3. Building With Loom Gradle

To compile and produce release JARs for any specific version anchor, navigate to its directory and run Gradle:

```bash
# Build MC 26.3 modern JAR
cd "Hyper Quality Screenshots v26.3/Hyper Quality Screenshots 26.3"
./gradlew build --no-daemon

# Execute unit test suite
./gradlew test --no-daemon
```

Artifacts are output to `build/libs/`: `hyper-screenshots-<version>.jar`.

---

## 4. Testing & Verification Suite

Automated unit tests in `src/test/java/` verify core mathematical and configuration invariants without launching a graphical Minecraft instance:
1. `ResolutionPresetTest.java`: Validates aspect ratio preservation, even dimension parity ($W \bmod 2 = 0$, $H \bmod 2 = 0$), and tile grid partitioning logic.
2. `ConfigSerializationTest.java`: Validates atomic JSON file writes, schema validation, clamp bounds, and default fallback recovery.

```bash
./gradlew check --no-daemon
```

---

## 5. Architectural Standards
* **Single-Line GPLv3 License Header**: Every `.java` file begins with `// Copyright (C) 2026 Dasik (Rifaditya) | GNU GPLv3`.
* **Clean 1 File 1 Purpose**: Classes are tightly scoped (~100–250 LOC soft guideline).
* **Client-Side Safety**: All mixins and GUI code are isolated to `client` and `modmenu` entrypoints.
