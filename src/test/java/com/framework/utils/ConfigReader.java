package com.framework.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfigReader {

    private static Properties properties;

    static {
        try {
            String path = "src/test/resources/config.properties";
            FileInputStream fis = new FileInputStream(path);

            properties = new Properties();
            properties.load(fis);

        } catch (IOException e) {
            throw new RuntimeException(
                    "config.properties not found at src/test/resources/config.properties");
        }
    }

    public static String get(String key) {
        return properties.getProperty(key);
    }

    public static boolean isHealingEnabled() {
        return Boolean.parseBoolean(
                properties.getProperty("healing.enabled", "false"));
    }

    public static String getHealingMode() {
        return properties.getProperty(
                "healing.mode",
                "NON_AI");
    }
}