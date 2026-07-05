package com.fran.saucedemo.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public final class TestConfig {
    private static final Properties PROPERTIES = loadProperties();

    private TestConfig() {}

    public static String baseUrl() {
        return get("base.url");
    }

    public static BrowserType browser() {
        return BrowserType.from(get("browser"));
    }

    public static boolean headless() {
        return Boolean.parseBoolean(get("headless"));
    }

    public static double slowMotionMs() {
        return Double.parseDouble(get("slow.motion.ms"));
    }

    public static double timeoutMs() {
        return Double.parseDouble(get("timeout.ms"));
    }

    public static boolean traceEnabled() {
        return Boolean.parseBoolean(get("trace.enabled"));
    }

    public static boolean screenshotOnFailure() {
        return Boolean.parseBoolean(get("screenshot.on.failure"));
    }

    public static boolean videoEnabled() {
        return Boolean.parseBoolean(get("video.enabled"));
    }

    private static String get(String key) {
        return System.getProperty(key, PROPERTIES.getProperty(key));
    }

    private static Properties loadProperties() {
        Properties properties = new Properties();
        try (InputStream input =
                TestConfig.class.getClassLoader().getResourceAsStream("config.properties")) {
            if (input == null) {
                throw new IllegalStateException(
                        "config.properties was not found in test resources");
            }
            properties.load(input);
            return properties;
        } catch (IOException exception) {
            throw new IllegalStateException("Could not load test configuration", exception);
        }
    }
}
