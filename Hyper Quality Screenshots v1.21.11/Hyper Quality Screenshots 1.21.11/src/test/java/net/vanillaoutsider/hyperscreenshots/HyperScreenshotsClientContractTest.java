// Copyright (C) 2026 Dasik (Rifaditya) | GNU GPLv3
package net.vanillaoutsider.hyperscreenshots;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import net.vanillaoutsider.hyperscreenshots.config.HyperScreenshotsConfig;
import net.vanillaoutsider.hyperscreenshots.config.ModMenuIntegration;
import net.vanillaoutsider.hyperscreenshots.config.ResolutionPreset;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Client-Only 5-Pillar Contract Verification Test for Hyper Quality Screenshots.
 */
class HyperScreenshotsClientContractTest {

    private static final String EXPECTED_LICENSE_HEADER = "// Copyright (C) 2026 Dasik (Rifaditya) | GNU GPLv3";
    private static final String KOFI_URL = "https://ko-fi.com/dasikigaijin";

    @Test
    @DisplayName("Pillar 1: Fuzz HyperScreenshotsConfig.validate() across 10,000+ floats and validate Ko-fi URI")
    void fuzzConfigValidationAndSupportUri() {
        Random rng = new Random(20261125L);
        ResolutionPreset[] presets = ResolutionPreset.values();
        float[] edgeValues = {
                Float.NaN, Float.POSITIVE_INFINITY, Float.NEGATIVE_INFINITY,
                -0.0f, 0.0f, -1.0f, 0.9999f, 1.0f, 16.0f, 16.0001f,
                -Float.MAX_VALUE, Float.MAX_VALUE, Float.MIN_VALUE
        };

        HyperScreenshotsConfig config = new HyperScreenshotsConfig();
        for (float edge : edgeValues) {
            config.resolutionPreset = null;
            config.customMultiplier = edge;
            config.validate();
            assertValidConfig(config);
        }

        for (int i = 0; i < 10_500; i++) {
            config.resolutionPreset = (i % 5 == 0) ? null : presets[rng.nextInt(presets.length)];
            config.customMultiplier = (i % 3 == 0)
                    ? Float.intBitsToFloat(rng.nextInt())
                    : (rng.nextFloat() * 200.0f - 100.0f);
            config.validate();
            assertValidConfig(config);
        }

        URI uri = URI.create(KOFI_URL);
        assertEquals("https", uri.getScheme());
        assertEquals("ko-fi.com", uri.getHost());
        assertEquals("/dasikigaijin", uri.getPath());
    }

    private static void assertValidConfig(HyperScreenshotsConfig config) {
        assertNotNull(config.resolutionPreset, "resolutionPreset must never remain null after validate()");
        assertFalse(Float.isNaN(config.customMultiplier), "customMultiplier must never remain NaN after validate()");
        assertTrue(config.customMultiplier >= 1.0f && config.customMultiplier <= 16.0f,
                "customMultiplier must stay clamped within [1.0, 16.0], got: " + config.customMultiplier);
    }

    @Test
    @DisplayName("Pillar 2: Concurrent ModMenuIntegration factory and config validation stress")
    void concurrentModMenuAndConfigStress() throws InterruptedException {
        int threads = 8;
        int iterations = 250;
        ExecutorService executor = Executors.newFixedThreadPool(threads);
        CountDownLatch ready = new CountDownLatch(threads);
        CountDownLatch start = new CountDownLatch(1);
        CountDownLatch done = new CountDownLatch(threads);
        AtomicReference<Throwable> failure = new AtomicReference<>();

        for (int t = 0; t < threads; t++) {
            final int threadId = t;
            executor.submit(() -> {
                ready.countDown();
                try {
                    start.await();
                    Random rng = new Random(1000L + threadId);
                    ModMenuIntegration integration = new ModMenuIntegration();
                    for (int i = 0; i < iterations; i++) {
                        ConfigScreenFactory<?> factory = integration.getModConfigScreenFactory();
                        assertNotNull(factory, "ModMenuIntegration must always return a non-null factory");

                        HyperScreenshotsConfig cfg = new HyperScreenshotsConfig();
                        cfg.resolutionPreset = (i % 4 == 0) ? null : ResolutionPreset.EIGHT_K;
                        cfg.customMultiplier = (i % 7 == 0) ? Float.NaN : (rng.nextFloat() * 64.0f - 16.0f);
                        cfg.validate();
                        assertValidConfig(cfg);
                    }
                } catch (Throwable ex) {
                    failure.compareAndSet(null, ex);
                } finally {
                    done.countDown();
                }
            });
        }

        assertTrue(ready.await(10, TimeUnit.SECONDS), "Worker threads failed to initialize");
        start.countDown();
        assertTrue(done.await(30, TimeUnit.SECONDS), "Concurrent stress tasks timed out");
        executor.shutdownNow();
        assertNull(failure.get(), "Concurrent stress test encountered unexpected exception: " + failure.get());
    }

    @Test
    @DisplayName("Pillar 3: Assert Ko-fi translation keys and ModMenu absent-YACL graceful fallback")
    void translationKeysAndAbsentYaclFallback() throws Exception {
        Path langPath = Path.of("src/main/resources/assets/hyper-screenshots/lang/en_us.json");
        assertTrue(Files.exists(langPath), "Missing en_us.json at " + langPath.toAbsolutePath());

        JsonObject lang = JsonParser.parseString(Files.readString(langPath)).getAsJsonObject();
        assertNonBlankJsonKey(lang, "dasiklibrary.support.kofi.button");
        assertNonBlankJsonKey(lang, "dasiklibrary.support.kofi.tooltip");

        ModMenuIntegration integration = new ModMenuIntegration();
        ConfigScreenFactory<?> factory = integration.getModConfigScreenFactory();
        assertNotNull(factory, "ModMenuIntegration must return non-null fallback factory");

        boolean invoked = false;
        for (Method m : ConfigScreenFactory.class.getMethods()) {
            if ("create".equals(m.getName()) && m.getParameterCount() == 1) {
                Object result = m.invoke(factory, (Object) null);
                assertNull(result, "Fallback ConfigScreenFactory must return null when YACL is absent");
                invoked = true;
                break;
            }
        }
        assertTrue(invoked, "ConfigScreenFactory.create(Screen) must be present and invokable");
    }

    private static void assertNonBlankJsonKey(JsonObject obj, String key) {
        JsonElement el = obj.get(key);
        assertNotNull(el, "Missing translation key: " + key);
        assertTrue(el.isJsonPrimitive() && el.getAsJsonPrimitive().isString(),
                "Translation key must be a JSON string: " + key);
        assertFalse(el.getAsString().isBlank(), "Translation key must be non-blank: " + key);
    }

    @Test
    @DisplayName("Pillar 4 & 5: Pure-JDK bytecode descriptor, reflection hygiene, manifest, and GPLv3 header audit")
    void bytecodeDescriptorsManifestAndLicenseHygiene() throws IOException {
        Path fabricModJson = Path.of("src/main/resources/fabric.mod.json");
        assertTrue(Files.exists(fabricModJson), "Missing fabric.mod.json");
        JsonObject modJson = JsonParser.parseString(Files.readString(fabricModJson)).getAsJsonObject();
        assertEquals("client", modJson.get("environment").getAsString(),
                "fabric.mod.json must declare environment=client");

        List<Path> javaFiles = new ArrayList<>();
        for (String srcDir : List.of("src/main/java", "src/test/java")) {
            Path root = Path.of(srcDir);
            if (Files.exists(root)) {
                try (Stream<Path> walk = Files.walk(root)) {
                    walk.filter(p -> Files.isRegularFile(p) && p.toString().endsWith(".java"))
                            .forEach(javaFiles::add);
                }
            }
        }
        assertFalse(javaFiles.isEmpty(), "Expected Java source files under src/main/java and src/test/java");

        for (Path javaFile : javaFiles) {
            String content = Files.readString(javaFile);
            String firstLine = content.lines().findFirst().orElse("");
            assertEquals(EXPECTED_LICENSE_HEADER, firstLine,
                    "Missing or malformed GPLv3 header on line 1 of " + javaFile);
            assertFalse(content.contains("ModVersion" + "Guard"),
                    "Dead guard reference found in " + javaFile);
        }

        Path yaclHelperClass = Path.of(
                "build/classes/java/main/net/vanillaoutsider/hyperscreenshots/config/YaclScreenHelper.class");
        Path modMenuClass = Path.of(
                "build/classes/java/main/net/vanillaoutsider/hyperscreenshots/config/ModMenuIntegration.class");
        assertTrue(Files.exists(yaclHelperClass), "Compiled YaclScreenHelper.class not found at " + yaclHelperClass);
        assertTrue(Files.exists(modMenuClass), "Compiled ModMenuIntegration.class not found at " + modMenuClass);

        ClassFileInfo yaclInfo = parseClassFile(yaclHelperClass);
        assertTrue(yaclInfo.methods.contains(
                        "createFactory:()Lcom/terraformersmc/modmenu/api/ConfigScreenFactory;"),
                "YaclScreenHelper must declare createFactory()Lcom/terraformersmc/modmenu/api/ConfigScreenFactory; "
                        + "found methods: " + yaclInfo.methods);

        assertTrue(yaclInfo.utf8Constants.contains("net/minecraft/client/gui/screens/ConfirmLinkScreen"),
                "YaclScreenHelper.class must reference ConfirmLinkScreen");
        assertFalse(yaclInfo.utf8Constants.contains("(Lnet/minecraft/client/gui/screens/Screen;Ljava/lang/String;)V"),
                "YaclScreenHelper.class must have ZERO deleted (Screen, String) ConfirmLinkScreen calls");

        String gradleProps = Files.readString(Path.of("gradle.properties"));
        boolean is1201 = gradleProps.contains("minecraft_version=1.20.1");
        if (is1201) {
            assertTrue(yaclInfo.utf8Constants.contains("(Ljava/lang/String;Lnet/minecraft/client/gui/screens/Screen;Z)V"),
                    "1.20.1 YaclScreenHelper.class must retain ConfirmLinkScreen.confirmLinkNow(String, Screen, boolean)");
        } else {
            assertTrue(yaclInfo.utf8Constants.contains("(Lnet/minecraft/client/gui/screens/Screen;Ljava/net/URI;)V"),
                    "1.21.1+ YaclScreenHelper.class must invoke ConfirmLinkScreen.confirmLinkNow(Screen, URI)");
        }

        ClassFileInfo modMenuInfo = parseClassFile(modMenuClass);
        assertFalse(modMenuInfo.utf8Constants.contains("forName"),
                "ModMenuIntegration.class must contain ZERO Class.forName reflection calls");
        assertFalse(modMenuInfo.utf8Constants.contains("java/lang/Class"),
                "ModMenuIntegration.class must contain ZERO java/lang/Class reflection references");
    }

    private record ClassFileInfo(Set<String> utf8Constants, Set<String> methods) {}

    private static ClassFileInfo parseClassFile(Path classFile) throws IOException {
        Set<String> utf8s = new HashSet<>();
        Set<String> methods = new HashSet<>();

        try (DataInputStream in = new DataInputStream(new BufferedInputStream(Files.newInputStream(classFile)))) {
            int magic = in.readInt();
            assertEquals(0xCAFEBABE, magic, "Invalid class file magic in " + classFile);
            in.readUnsignedShort(); // minor_version
            in.readUnsignedShort(); // major_version

            int cpCount = in.readUnsignedShort();
            String[] cpUtf8 = new String[cpCount];
            for (int i = 1; i < cpCount; i++) {
                int tag = in.readUnsignedByte();
                switch (tag) {
                    case 1 -> { // CONSTANT_Utf8
                        String s = in.readUTF();
                        cpUtf8[i] = s;
                        utf8s.add(s);
                    }
                    case 3, 4 -> in.readInt(); // Integer, Float
                    case 5, 6 -> { // Long, Double (occupies 2 constant pool slots)
                        in.readLong();
                        i++;
                    }
                    case 7, 8, 16, 19, 20 -> in.readUnsignedShort(); // Class, String, MethodType, Module, Package
                    case 9, 10, 11, 12, 17, 18 -> { // Fieldref, Methodref, InterfaceMethodref, NameAndType, Dynamic, InvokeDynamic
                        in.readUnsignedShort();
                        in.readUnsignedShort();
                    }
                    case 15 -> { // MethodHandle
                        in.readUnsignedByte();
                        in.readUnsignedShort();
                    }
                    default -> throw new IOException("Unsupported constant pool tag " + tag + " in " + classFile);
                }
            }

            in.readUnsignedShort(); // access_flags
            in.readUnsignedShort(); // this_class
            in.readUnsignedShort(); // super_class

            int interfacesCount = in.readUnsignedShort();
            for (int i = 0; i < interfacesCount; i++) {
                in.readUnsignedShort();
            }

            int fieldsCount = in.readUnsignedShort();
            for (int i = 0; i < fieldsCount; i++) {
                in.readUnsignedShort(); // access_flags
                in.readUnsignedShort(); // name_index
                in.readUnsignedShort(); // descriptor_index
                skipAttributes(in);
            }

            int methodsCount = in.readUnsignedShort();
            for (int i = 0; i < methodsCount; i++) {
                in.readUnsignedShort(); // access_flags
                int nameIndex = in.readUnsignedShort();
                int descriptorIndex = in.readUnsignedShort();
                skipAttributes(in);
                String name = cpUtf8[nameIndex];
                String desc = cpUtf8[descriptorIndex];
                if (name != null && desc != null) {
                    methods.add(name + ":" + desc);
                }
            }
        }

        return new ClassFileInfo(utf8s, methods);
    }

    private static void skipAttributes(DataInputStream in) throws IOException {
        int attributesCount = in.readUnsignedShort();
        for (int i = 0; i < attributesCount; i++) {
            in.readUnsignedShort(); // attribute_name_index
            long len = Integer.toUnsignedLong(in.readInt());
            long skipped = 0;
            while (skipped < len) {
                long n = in.skip(len - skipped);
                if (n <= 0) {
                    if (in.read() < 0) {
                        throw new IOException("Unexpected EOF while skipping class attributes");
                    }
                    skipped += 1;
                } else {
                    skipped += n;
                }
            }
        }
    }
}
