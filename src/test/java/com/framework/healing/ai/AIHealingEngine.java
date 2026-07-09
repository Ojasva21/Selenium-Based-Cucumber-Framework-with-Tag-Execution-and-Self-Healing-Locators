package com.framework.healing.ai;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

public class AIHealingEngine {

    private static final double MINIMUM_CONFIDENCE = 25.0;
    private static final double AUTO_HEAL_THRESHOLD = 60.0;

    private final DOMAnalyzer domAnalyzer;
    private final LocatorScorer locatorScorer;
    private final LocatorHistoryManager historyManager;
    private final HealingReportManager reportManager;
    private final FailurePatternAnalyzer failurePatternAnalyzer;

    public AIHealingEngine() {
        this.domAnalyzer = new DOMAnalyzer();
        this.locatorScorer = new LocatorScorer();
        this.historyManager = new LocatorHistoryManager();
        this.reportManager = new HealingReportManager();
        this.failurePatternAnalyzer = new FailurePatternAnalyzer();
    }

    public LocatorMetadata findBestMatch(
            WebDriver driver,
            LocatorMetadata failedElement) {

        String failedIdentifier =
                getBestIdentifier(failedElement);

        if (!failedIdentifier.equals("UNKNOWN")) {

            failurePatternAnalyzer.recordFailure(
                    failedIdentifier);
        }

        List<String> historyRecords =
                historyManager.getHealingHistory();

        if (!historyRecords.isEmpty()) {

            for (String record : historyRecords) {

                String[] parts = record.split("=");

                if (parts.length == 2
                        && failedIdentifier.equals(parts[0])) {

                    System.out.println(
                            "Historical healing match found: "
                                    + parts[1]);
                }
            }
        }

        List<LocatorMetadata> candidates =
                domAnalyzer.scanPage(driver);

        LocatorMetadata bestMatch = null;
        double highestScore = 0;

        for (LocatorMetadata candidate : candidates) {

            double score =
                    locatorScorer.calculateScore(
                            failedElement,
                            candidate);

            System.out.println("--------------------------------");

            System.out.println("ID = " + candidate.getId());

            System.out.println("TEXT = " + candidate.getText());

            System.out.println("HREF = " + candidate.getHref());

            System.out.println("DATA BUTTON = " + candidate.getDataButtonName());

            System.out.println("TAG = " + candidate.getTagName());

            System.out.println("SCORE = " + score);

            if (score > highestScore) {

                highestScore = score;
                bestMatch = candidate;
            }
        }

        if (highestScore < MINIMUM_CONFIDENCE) {

            System.out.println(
                    "AI Healing Rejected. Confidence too low: "
                            + highestScore);

            return null;
        }

        String healStatus;

        if (highestScore >= AUTO_HEAL_THRESHOLD) {

            healStatus = "HIGH CONFIDENCE HEAL";

            System.out.println(
                    "HIGH CONFIDENCE HEAL ACCEPTED");

        } else {

            healStatus = "LOW CONFIDENCE HEAL";

            System.out.println(
                    "LOW CONFIDENCE HEAL ACCEPTED - REVIEW RECOMMENDED");
        }

        if (bestMatch != null
                && !getBestIdentifier(failedElement).equals("UNKNOWN")) {

            historyManager.saveHealingRecord(
                    getDisplayIdentifier(
                            failedElement,
                            failedElement.getOriginalLocator()),
                    getDisplayIdentifier(
                            bestMatch,
                            failedElement.getOriginalLocator()));

            reportManager.generateReport(
                    getDisplayIdentifier(
                            failedElement,
                            failedElement.getOriginalLocator()),
                    getDisplayIdentifier(
                            bestMatch,
                            failedElement.getOriginalLocator()),
                    highestScore);

            HealingContext.setHealingData(
                    "NON_AI",
                    failedElement.getOriginalLocator(),
                    getDisplayIdentifier(
                            bestMatch,
                            failedElement.getOriginalLocator()),
                    highestScore,
                    healStatus);

            System.out.println(
                    "AI Healed Element Found | Score = "
                            + highestScore);

            System.out.println(
                    "Failed Locator : "
                            + failedElement.getOriginalLocator());

            System.out.println(
                    "Healed Locator : "
                            + getDisplayIdentifier(
                            bestMatch,
                            failedElement.getOriginalLocator()));

            System.out.println(
                    "\n========== BEST MATCH FOUND ==========");

            System.out.println(
                    "TAG = "
                            + bestMatch.getTagName());

            System.out.println(
                    "TEXT = "
                            + bestMatch.getText());

            System.out.println(
                    "ID = "
                            + bestMatch.getId());

            System.out.println(
                    "HREF = "
                            + bestMatch.getHref());

            System.out.println(
                    "CLASS = "
                            + bestMatch.getClassName());

            System.out.println(
                    "======================================");

            failurePatternAnalyzer.printAnalysis();
        }

        return bestMatch;
    }

    public WebElement findHealedElement(
            WebDriver driver,
            LocatorMetadata failedElement) {

        System.out.println(
                "\n========== FIND HEALED ELEMENT ==========");

        LocatorMetadata healedMetadata =
                findBestMatch(driver, failedElement);

        if (healedMetadata == null) {

            System.out.println(
                    "No healed metadata returned.");

            return null;
        }

        // --------------------------------------------------
        // Search using ID
        // --------------------------------------------------

        if (healedMetadata.getId() != null
                && !healedMetadata.getId().isBlank()) {

            try {

                System.out.println(
                        "Trying ID : "
                                + healedMetadata.getId());

                WebElement element =
                        driver.findElement(
                                By.id(
                                        healedMetadata.getId()));

                System.out.println(
                        "Recovered using ID");

                return element;

            } catch (Exception e) {

                System.out.println(
                        "ID lookup failed.");
            }
        }

        // --------------------------------------------------
        // Search using HREF
        // --------------------------------------------------

        if (healedMetadata.getHref() != null
                && !healedMetadata.getHref().isBlank()) {

            try {

                System.out.println(
                        "Trying HREF : "
                                + healedMetadata.getHref());

                String href =
                        healedMetadata.getHref();

                String lastPart =
                        href.substring(
                                href.lastIndexOf("/") + 1);

                List<WebElement> elements =
                        driver.findElements(
                                By.xpath(
                                        "//*[contains(@href,'"
                                                + lastPart
                                                + "')]"));

                for (WebElement element : elements) {

                    if (element.isDisplayed()
                            && element.isEnabled()) {

                        System.out.println(
                                "Recovered using displayed HREF element");

                        return element;
                    }
                }

            } catch (Exception e) {

                System.out.println(
                        "HREF lookup failed.");
            }
        }

        // --------------------------------------------------
        // Search using data-button-name
        // --------------------------------------------------

        if (healedMetadata.getDataButtonName() != null
                && !healedMetadata.getDataButtonName().isBlank()) {

            try {

                System.out.println(
                        "Trying DATA BUTTON : "
                                + healedMetadata.getDataButtonName());

                List<WebElement> elements =
                        driver.findElements(
                                By.xpath(
                                        "//*[@data-button-name='"
                                                + healedMetadata.getDataButtonName()
                                                + "']"));

                for (WebElement element : elements) {

                    if (element.isDisplayed()
                            && element.isEnabled()) {

                        return element;
                    }
                }

            } catch (Exception e) {

                System.out.println(
                        "DATA BUTTON lookup failed.");
            }
        }

        // --------------------------------------------------
        // Search using Visible Text
        // --------------------------------------------------

        if (healedMetadata.getText() != null
                && !healedMetadata.getText().isBlank()) {

            try {

                System.out.println(
                        "Trying TEXT : "
                                + healedMetadata.getText());

                List<WebElement> elements =
                        driver.findElements(
                                By.xpath(
                                        "//*[normalize-space(text())='"
                                                + healedMetadata.getText()
                                                + "']"));

                for (WebElement element : elements) {

                    if (element.isDisplayed()
                            && element.isEnabled()) {

                        return element;
                    }
                }

            } catch (Exception e) {

                System.out.println(
                        "TEXT lookup failed.");
            }
        }

        // --------------------------------------------------
        // Search using Name
        // --------------------------------------------------

        if (healedMetadata.getName() != null
                && !healedMetadata.getName().isBlank()) {

            try {

                System.out.println(
                        "Trying NAME : "
                                + healedMetadata.getName());

                WebElement element =
                        driver.findElement(
                                By.name(
                                        healedMetadata.getName()));

                System.out.println(
                        "Recovered using NAME");

                return element;

            } catch (Exception e) {

                System.out.println(
                        "NAME lookup failed.");
            }
        }

        // --------------------------------------------------
        // Search using Class
        // --------------------------------------------------

        if (healedMetadata.getClassName() != null
                && !healedMetadata.getClassName().isBlank()) {

            try {

                System.out.println(
                        "Trying CLASS : "
                                + healedMetadata.getClassName());

                String cssSelector = healedMetadata.getTagName();

                String[] classes =
                        healedMetadata.getClassName().trim().split("\\s+");

                for (String cls : classes) {

                    cssSelector += "." + cls;
                }

                WebElement element =
                        driver.findElement(
                                By.cssSelector(cssSelector));

                System.out.println(
                        "Recovered using CSS : "
                                + cssSelector);

                return element;


            } catch (Exception e) {

                System.out.println(
                        "CLASS lookup failed.");
            }
        }

        System.out.println(
                "Healing failed. No locator strategy worked.");

        return null;
    }
    private String getBestIdentifier(
            LocatorMetadata metadata) {

        if (metadata == null) {
            return "UNKNOWN";
        }

        if (metadata.getId() != null
                && !metadata.getId().isBlank()) {
            return metadata.getId();
        }

        if (metadata.getHref() != null
                && !metadata.getHref().isBlank()) {
            return metadata.getHref();
        }

        if (metadata.getDataButtonName() != null
                && !metadata.getDataButtonName().isBlank()) {
            return metadata.getDataButtonName();
        }

        if (metadata.getText() != null
                && !metadata.getText().isBlank()) {
            return metadata.getText();
        }

        if (metadata.getClassName() != null
                && !metadata.getClassName().isBlank()) {
            return metadata.getClassName();
        }

        return "UNKNOWN";
    }
    private String getDisplayIdentifier(
            LocatorMetadata metadata,
            String originalLocator) {

        if (metadata == null || originalLocator == null) {
            return "UNKNOWN";
        }

        // ID locator
        if (originalLocator.startsWith("By.id:")) {

            if (metadata.getId() != null && !metadata.getId().isBlank()) {
                return "By.id: " + metadata.getId();
            }
        }

        // XPath locator
        if (originalLocator.startsWith("By.xpath:")) {

            if (metadata.getHref() != null && !metadata.getHref().isBlank()) {
                return "By.xpath: //a[@href='" + metadata.getHref() + "']";
            }

            if (metadata.getText() != null && !metadata.getText().isBlank()) {
                return "By.xpath: //*[normalize-space(text())='" + metadata.getText() + "']";
            }
        }

        // CSS locator
        if (originalLocator.startsWith("By.cssSelector:")) {

            if (metadata.getClassName() != null
                    && !metadata.getClassName().isBlank()) {

                String cssSelector =
                        metadata.getTagName();

                String[] classes =
                        metadata.getClassName()
                                .trim()
                                .split("\\s+");

                for (String cls : classes) {

                    cssSelector += "." + cls;
                }

                return "By.cssSelector: " + cssSelector;
            }

            if (metadata.getId() != null
                    && !metadata.getId().isBlank()) {

                return "By.cssSelector: #" + metadata.getId();
            }
        }

        return "UNKNOWN";
    }
}