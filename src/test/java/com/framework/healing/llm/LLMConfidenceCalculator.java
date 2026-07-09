package com.framework.healing.llm;

import com.framework.healing.ai.LocatorMetadata;

public class LLMConfidenceCalculator {

    public double calculateConfidence(
            LocatorMetadata failed,
            LocatorMetadata healed) {

        if (failed == null || healed == null) {
            return 0;
        }

        double score = 0;

        // Visible Text
        score += similarity(
                failed.getText(),
                healed.getText()) * 30;

        // Href
        score += similarity(
                failed.getHref(),
                healed.getHref()) * 25;

        // Data Button Name
        score += similarity(
                failed.getDataButtonName(),
                healed.getDataButtonName()) * 20;

        // ID
        score += similarity(
                failed.getId(),
                healed.getId()) * 10;

        // Tag
        score += similarity(
                failed.getTagName(),
                healed.getTagName()) * 10;

        // Class
        score += similarity(
                failed.getClassName(),
                healed.getClassName()) * 5;

        return Math.round(Math.min(score, 100));
    }

    private double similarity(
            String value1,
            String value2) {

        if (value1 == null || value2 == null) {
            return 0;
        }

        value1 = value1.toLowerCase().trim();
        value2 = value2.toLowerCase().trim();

        if (value1.equals(value2)) {
            return 1.0;
        }

        if (value1.contains(value2)
                || value2.contains(value1)) {

            return 0.85;
        }

        int commonChars = 0;

        for (char c : value1.toCharArray()) {

            if (value2.indexOf(c) >= 0) {
                commonChars++;
            }
        }

        return (double) commonChars
                / Math.max(
                value1.length(),
                value2.length());
    }
}