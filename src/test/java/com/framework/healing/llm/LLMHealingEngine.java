package com.framework.healing.llm;

import com.framework.healing.ai.DOMAnalyzer;
import com.framework.healing.ai.HealingContext;
import com.framework.healing.llm.LLMConfidenceCalculator;
import com.framework.healing.ai.LocatorMetadata;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

public class LLMHealingEngine {

    private final DOMAnalyzer domAnalyzer =
            new DOMAnalyzer();

    private final PromptBuilder promptBuilder =
            new PromptBuilder();

    private final LLMConfidenceCalculator confidenceCalculator =
            new LLMConfidenceCalculator();

    private final OllamaClient ollamaClient =
            new OllamaClient();

    public WebElement findHealedElement(
            WebDriver driver,
            LocatorMetadata failedElement) {

        try {

            List<LocatorMetadata> candidates =
                    domAnalyzer.scanPage(driver);

            // Keep only the most relevant candidates before sending to the LLM
            candidates = candidates.stream()

                    .sorted((c1, c2) -> Double.compare(
                            confidenceCalculator.calculateConfidence(failedElement, c2),
                            confidenceCalculator.calculateConfidence(failedElement, c1)
                    ))

                    .limit(10)

                    .toList();

            System.out.println("\n========== TOP 10 CANDIDATES ==========");

            for (LocatorMetadata c : candidates) {

                System.out.println(
                        "ID=" + c.getId()
                                + " | HREF=" + c.getHref()
                                + " | TEXT=" + c.getText()
                                + " | BUTTON=" + c.getDataButtonName()
                );
            }

            System.out.println(
                    "\n========== LLM HEALING START ==========");

            System.out.println(
                    "Failed ID            = "
                            + failedElement.getId());

            System.out.println(
                    "Failed Text          = "
                            + failedElement.getText());

            System.out.println(
                    "Failed Href          = "
                            + failedElement.getHref());

            System.out.println(
                    "Failed Button Name   = "
                            + failedElement.getDataButtonName());

            System.out.println(
                    "Failed Tag           = "
                            + failedElement.getTagName());

            System.out.println(
                    "Total DOM Elements = "
                            + candidates.size());

            String prompt =
                    promptBuilder.buildPrompt(
                            failedElement,
                            candidates);

            System.out.println("\n========== PROMPT SENT TO LLM ==========");
            System.out.println(prompt);
            System.out.println("========================================");

            String response =
                    ollamaClient.askLlama(prompt);

            response = response.trim();

            System.out.println(
                    "\n========== LLM RESPONSE ==========");

            System.out.println(response);

            System.out.println(
                    "==================================");

            String predictedLocator =
                    extractPredictedLocator(
                            response,
                            candidates);
            LocatorMetadata healedMetadata = null;

            for (LocatorMetadata candidate : candidates) {

                if (matchesCandidate(candidate, predictedLocator)) {

                    healedMetadata = candidate;
                    break;
                }
            }

            System.out.println(
                    "Predicted Locator = "
                            + predictedLocator);

            if (predictedLocator == null
                    || predictedLocator.isBlank()) {

                System.out.println(
                        "LLM could not predict locator");

                return null;
            }

            WebElement element =
                    findElementByPrediction(
                            driver,
                            predictedLocator);

            if (element == null) {

                System.out.println("Unable to locate healed element.");

                return null;
            }

            double confidence =
                    confidenceCalculator.calculateConfidence(
                            failedElement,
                            healedMetadata);

            System.out.println(
                    "Confidence Score = "
                            + confidence);

            HealingContext.setHealingData(
                    "LLM_AI",
                    failedElement.getOriginalLocator(),
                    getDisplayIdentifier(
                            healedMetadata,
                            failedElement.getOriginalLocator()),
                    confidence,
                    "LLM HEAL SUCCESS");

            System.out.println(
                    "LLM Healing Successful");

            System.out.println(
                    "Failed Locator : "
                            + failedElement.getOriginalLocator());

            System.out.println(
                    "Predicted Locator : "
                            + predictedLocator);

            System.out.println(
                    "Healed Locator : "
                            + getDisplayIdentifier(
                            healedMetadata,
                            failedElement.getOriginalLocator()));

            System.out.println(
                    "Healing Strategy Used : "
                            + determineStrategy(predictedLocator));

            System.out.println(
                    "Confidence Score : "
                            + confidence);

            System.out.println(
                    "======================================");

            return element;

        } catch (Exception e) {

            System.out.println(
                    "LLM Healing Exception:");

            e.printStackTrace();

            return null;
        }
    }

    private String extractPredictedLocator(
            String response,
            List<LocatorMetadata> candidates) {

        if (response == null) {
            return null;
        }

        try {

            System.out.println(
                    "RAW RESPONSE = " + response);

            // Handle responses like:
// {"best_locator":"Candidate 1"}

            if (response.contains("Candidate")) {

                try {

                    int start =
                            response.indexOf("Candidate");

                    int end =
                            response.indexOf("\"", start);

                    String candidateString =
                            response.substring(start, end);

                    System.out.println(
                            "LLM Selected = "
                                    + candidateString);

                    String number =
                            candidateString.replace(
                                    "Candidate",
                                    "").trim();

                    int index =
                            Integer.parseInt(number) - 1;

                    if (index >= 0
                            && index < candidates.size()) {

                        LocatorMetadata candidate =
                                candidates.get(index);

                        if (candidate.getId() != null
                                && !candidate.getId().isBlank()) {

                            return candidate.getId();
                        }

                        if (candidate.getHref() != null
                                && !candidate.getHref().isBlank()) {

                            return candidate.getHref();
                        }

                        if (candidate.getDataButtonName() != null
                                && !candidate.getDataButtonName().isBlank()) {

                            return candidate.getDataButtonName();
                        }

                        if (candidate.getText() != null
                                && !candidate.getText().isBlank()) {

                            return candidate.getText();
                        }

                        if (candidate.getName() != null
                                && !candidate.getName().isBlank()) {

                            return candidate.getName();
                        }
                    }

                } catch (Exception ignored) {
                }
            }

            // First try direct candidate matching
            for (LocatorMetadata candidate : candidates) {

                if (candidate.getId() != null
                        && !candidate.getId().isBlank()
                        && response.contains(candidate.getId())) {

                    System.out.println(
                            "Matched Candidate ID = "
                                    + candidate.getId());

                    return candidate.getId();
                }

                if (candidate.getHref() != null
                        && !candidate.getHref().isBlank()
                        && response.contains(candidate.getHref())) {

                    System.out.println(
                            "Matched Candidate HREF = "
                                    + candidate.getHref());

                    return candidate.getHref();
                }

                if (candidate.getDataButtonName() != null
                        && !candidate.getDataButtonName().isBlank()
                        && response.contains(candidate.getDataButtonName())) {

                    System.out.println(
                            "Matched Candidate DATA BUTTON = "
                                    + candidate.getDataButtonName());

                    return candidate.getDataButtonName();
                }

                if (candidate.getText() != null
                        && !candidate.getText().isBlank()
                        && response.contains(candidate.getText())) {

                    System.out.println(
                            "Matched Candidate TEXT = "
                                    + candidate.getText());

                    return candidate.getText();
                }
                if (candidate.getName() != null
                        && !candidate.getName().isBlank()
                        && response.contains(candidate.getName())) {

                    System.out.println(
                            "Matched Candidate NAME = "
                                    + candidate.getName());

                    return candidate.getName();
                }
            }

        } catch (Exception e) {

            e.printStackTrace();
        }

        return null;
    }
    private boolean matchesCandidate(
            LocatorMetadata candidate,
            String prediction) {

        if (prediction == null) {
            return false;
        }

        prediction = prediction.trim();

        return prediction.equals(candidate.getId())
                || prediction.equals(candidate.getHref())
                || prediction.equals(candidate.getDataButtonName())
                || prediction.equals(candidate.getText())
                || prediction.equals(candidate.getName());
    }
    private WebElement findElementByPrediction(
            WebDriver driver,
            String prediction) {

        try {

            return driver.findElement(
                    By.id(prediction));

        } catch (Exception ignored) {
        }

        try {

            return driver.findElement(
                    By.name(prediction));

        } catch (Exception ignored) {
        }

        try {

            return driver.findElement(
                    By.xpath("//*[@href='" + prediction + "']"));

        } catch (Exception ignored) {
        }

        try {

            return driver.findElement(
                    By.xpath("//*[@data-button-name='" + prediction + "']"));

        } catch (Exception ignored) {
        }

        try {

            return driver.findElement(
                    By.xpath("//*[normalize-space(text())='" + prediction + "']"));

        } catch (Exception ignored) {
        }

        return null;
    }
    private String determineStrategy(
            String prediction) {

        if (prediction == null) {
            return "UNKNOWN";
        }

        if (prediction.startsWith("/")
                || prediction.startsWith("http://")
                || prediction.startsWith("https://")) {

            return "HREF";
        }

        if (prediction.contains("Top Navigation")) {
            return "DATA_BUTTON_NAME";
        }

        if (prediction.contains(" ")) {
            return "VISIBLE_TEXT";
        }

        return "ID";
    }
    private String getDisplayIdentifier(
            LocatorMetadata metadata,
            String originalLocator) {

        if (metadata == null || originalLocator == null) {
            return "UNKNOWN";
        }

        // ID
        if (originalLocator.startsWith("By.id:")) {

            if (metadata.getId() != null
                    && !metadata.getId().isBlank()) {

                return "By.id: " + metadata.getId();
            }
        }

        // XPath
        if (originalLocator.startsWith("By.xpath:")) {

            if (metadata.getHref() != null
                    && !metadata.getHref().isBlank()) {

                return "By.xpath: //a[@href='"
                        + metadata.getHref()
                        + "']";
            }

            if (metadata.getText() != null
                    && !metadata.getText().isBlank()) {

                return "By.xpath: //*[normalize-space(text())='"
                        + metadata.getText()
                        + "']";
            }
        }

        // CSS
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