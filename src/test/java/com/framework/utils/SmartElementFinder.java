package com.framework.utils;

import com.framework.healing.ai.AIHealingEngine;
import com.framework.healing.ai.HealingContext;
import com.framework.healing.ai.LocatorMetadata;
import com.framework.healing.llm.LLMHealingEngine;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class SmartElementFinder {

    private static final Logger logger =
            LogManager.getLogger(SmartElementFinder.class);

    public static WebElement find(
            WebDriver driver,
            By primaryLocator,
            By... fallbackLocators) {

        try {

            WebElement element =
                    driver.findElement(primaryLocator);

            logger.info(
                    "Located element using primary locator: "
                            + primaryLocator);

            return element;

        } catch (NoSuchElementException e) {

            logger.warn(
                    "Primary locator failed: "
                            + primaryLocator);

            if (!ConfigReader.isHealingEnabled()) {

                throw new NoSuchElementException(
                        "Healing disabled. Element not found: "
                                + primaryLocator);
            }

            String healingMode =
                    ConfigReader.getHealingMode();

            switch (healingMode.toUpperCase()) {

                case "NON_AI":

                    return performAIHealing(
                            driver,
                            primaryLocator);

                case "LLM_AI":

                    return performLLMHealing(
                            driver,
                            primaryLocator);

                case "HYBRID":

                    try {

                        return performAIHealing(
                                driver,
                                primaryLocator);

                    } catch (Exception ignored) {

                        logger.warn(
                                "NON_AI healing failed. Switching to LLM_AI...");
                    }

                    return performLLMHealing(
                            driver,
                            primaryLocator);

                default:

                    throw new RuntimeException(
                            "Invalid healing.mode in config.properties");
            }
        }
    }

    private static WebElement performAIHealing(
            WebDriver driver,
            By primaryLocator) {

        logger.info(
                "Executing NON_AI healing strategy...");

        AIHealingEngine aiHealingEngine =
                new AIHealingEngine();

        LocatorMetadata failedElement =
                LocatorMetadata.fromLocator(
                        primaryLocator.toString());

        WebElement healedElement =
                aiHealingEngine.findHealedElement(
                        driver,
                        failedElement);

        if (healedElement != null) {

            logger.info(
                    "HEALED (NON_AI) -> Primary ["
                            + primaryLocator
                            + "] recovered by similarity engine");

            return healedElement;
        }

        throw new NoSuchElementException(
                "NON_AI healing failed for locator: "
                        + primaryLocator);
    }

    private static WebElement performLLMHealing(
            WebDriver driver,
            By primaryLocator) {

        logger.info(
                "Executing LLM_AI healing strategy...");

        LLMHealingEngine llmHealingEngine =
                new LLMHealingEngine();

        LocatorMetadata failedElement =
                LocatorMetadata.fromLocator(
                        primaryLocator.toString());

        WebElement healedElement =
                llmHealingEngine.findHealedElement(
                        driver,
                        failedElement);

        if (healedElement != null) {

            logger.info(
                    "HEALED (LLM_AI) -> Primary ["
                            + primaryLocator
                            + "] recovered using Llama3");

            return healedElement;
        }

        throw new NoSuchElementException(
                "LLM_AI healing failed for locator: "
                        + primaryLocator);
    }
}