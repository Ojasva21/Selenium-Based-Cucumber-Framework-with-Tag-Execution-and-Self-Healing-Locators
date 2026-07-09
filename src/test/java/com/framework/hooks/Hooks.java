package com.framework.hooks;

import com.framework.driver.DriverFactory;
import com.framework.healing.ai.HealingContext;
import com.framework.utils.ExecutionHistoryManager;
import com.framework.utils.ScreenshotUtil;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;

public class Hooks {

    private static final Logger logger =
            LogManager.getLogger(Hooks.class);

    @Before
    public void setUp(Scenario scenario) {

        logger.info(
                "Starting scenario: "
                        + scenario.getName());

        DriverFactory.getDriver();
    }

    @After
    public void tearDown(Scenario scenario) {

        WebDriver driver =
                DriverFactory.getDriver();

        if (scenario.isFailed()) {

            logger.error(
                    "Scenario FAILED: "
                            + scenario.getName());

            byte[] screenshot =
                    ScreenshotUtil.captureScreenshotAsBytes(
                            driver);

            scenario.attach(
                    screenshot,
                    "image/png",
                    scenario.getName());

        } else {

            logger.info(
                    "Scenario PASSED: "
                            + scenario.getName());
        }

        if (HealingContext.getHealingMode() != null) {

            scenario.log(
                    "========== HEALING DETAILS ==========");

            scenario.log(
                    "Healing Mode : "
                            + HealingContext.getHealingMode());

            scenario.log(
                    "Failed Locator : "
                            + HealingContext.getFailedLocator());

            scenario.log(
                    "Healed Locator : "
                            + HealingContext.getHealedLocator());

            scenario.log(
                    "Confidence Score : "
                            + HealingContext.getConfidenceScore());

            scenario.log(
                    "Status : "
                            + HealingContext.getStatus());
        }

        // Save every execution into history dashboard
        ExecutionHistoryManager.logExecution(
                scenario.getName(),
                scenario.getStatus().name());

        HealingContext.clear();

        DriverFactory.quitDriver();
    }
}