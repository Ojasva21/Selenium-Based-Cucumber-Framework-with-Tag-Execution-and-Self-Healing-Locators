package com.framework.driver;

import com.framework.utils.ConfigReader;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

public class DriverFactory {

    private static WebDriver driver;

    public static WebDriver getDriver() {

        if (driver == null) {

            String browser =
                    ConfigReader.get("browser");

            if (browser.equalsIgnoreCase("chrome")) {

                WebDriverManager.chromedriver().setup();

                ChromeOptions options =
                        new ChromeOptions();

                // Disable Chrome Password Manager
                Map<String, Object> prefs =
                        new HashMap<>();

                prefs.put(
                        "credentials_enable_service",
                        false);

                prefs.put(
                        "profile.password_manager_enabled",
                        false);

                prefs.put(
                        "profile.password_manager_leak_detection",
                        false);

                options.setExperimentalOption(
                        "prefs",
                        prefs);

                // Disable annoying Chrome popups
                options.addArguments(
                        "--disable-save-password-bubble");

                options.addArguments(
                        "--disable-notifications");

                options.addArguments(
                        "--disable-popup-blocking");

                driver =
                        new ChromeDriver(options);

            } else {

                throw new RuntimeException(
                        "Browser not supported: "
                                + browser);
            }

            driver.manage().window().maximize();

            int waitSeconds =
                    Integer.parseInt(
                            ConfigReader.get(
                                    "implicitWait"));

            driver.manage()
                    .timeouts()
                    .implicitlyWait(
                            Duration.ofSeconds(
                                    waitSeconds));
        }

        return driver;
    }

    public static void quitDriver() {

        if (driver != null) {

            driver.quit();

            driver = null;
        }
    }
}