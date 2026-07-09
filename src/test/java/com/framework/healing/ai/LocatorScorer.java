package com.framework.healing.ai;

public class LocatorScorer {

    public double calculateScore(
            LocatorMetadata failedElement,
            LocatorMetadata candidateElement) {

        double score = 0;

        // Bonus if href filename matches exactly
        String failedFile =
                extractFileName(failedElement.getHref());

        String candidateFile =
                extractFileName(candidateElement.getHref());

        if (!failedFile.isBlank()
                && failedFile.equalsIgnoreCase(candidateFile)) {

            score += 25;
        }

        // ID Similarity
        score += similarity(
                failedElement.getId(),
                candidateElement.getId()) * 10;

        // Visible Text
        score += similarity(
                failedElement.getText(),
                candidateElement.getText()) * 25;

        // Strong bonus for exact text match
        if (failedElement.getText() != null
                && candidateElement.getText() != null
                && failedElement.getText().equalsIgnoreCase(candidateElement.getText())) {

            score += 15;
        }

        // Compare only filename from href
        score += similarity(
                extractFileName(failedElement.getHref()),
                extractFileName(candidateElement.getHref())) * 35;

        // data-button-name
        score += similarity(
                failedElement.getDataButtonName(),
                candidateElement.getDataButtonName()) * 20;

        // Strong bonus for exact data-button-name
        if (failedElement.getDataButtonName() != null
                && candidateElement.getDataButtonName() != null
                && failedElement.getDataButtonName()
                .equalsIgnoreCase(candidateElement.getDataButtonName())) {

            score += 20;
        }

        // Tag
        score += similarity(
                failedElement.getTagName(),
                candidateElement.getTagName()) * 5;

        // Class
        score += similarity(
                failedElement.getClassName(),
                candidateElement.getClassName()) * 2;

        // Token similarity on visible text
        score += tokenSimilarity(
                failedElement.getText(),
                candidateElement.getText()) * 3;

        return Math.min(score, 100);
    }

    private String extractFileName(String href) {

        if (href == null || href.isBlank()) {
            return "";
        }

        href = href.toLowerCase();

        int index = href.lastIndexOf('/');

        if (index >= 0) {
            href = href.substring(index + 1);
        }

        href = href.replace("_wrong", "");

        return href;
    }

    private double tokenSimilarity(
            String value1,
            String value2) {

        if (value1 == null || value2 == null) {
            return 0;
        }

        value1 = value1.toLowerCase()
                .replace("_wrong", "")
                .trim();

        value2 = value2.toLowerCase()
                .replace("_wrong", "")
                .trim();

        String[] tokens1 =
                value1.split("[\\s\\-_./]+");

        String[] tokens2 =
                value2.split("[\\s\\-_./]+");

        int matches = 0;

        for (String token1 : tokens1) {

            if (token1.isBlank()) {
                continue;
            }

            for (String token2 : tokens2) {

                if (token1.equals(token2)) {
                    matches++;
                    break;
                }
            }
        }

        return (double) matches /
                Math.max(tokens1.length,
                        tokens2.length);
    }

    private double similarity(
            String value1,
            String value2) {

        if (value1 == null || value2 == null) {
            return 0;
        }

        value1 = value1.toLowerCase()
                .replace("_wrong", "")
                .trim();

        value2 = value2.toLowerCase()
                .replace("_wrong", "")
                .trim();

        if (value1.isBlank() || value2.isBlank()) {
            return 0;
        }

        if (value1.equals(value2)) {
            return 1.0;
        }

        if (value1.contains(value2)
                || value2.contains(value1)) {

            return 0.98;
        }

        String[] tokens1 =
                value1.split("[\\s\\-_./]+");

        String[] tokens2 =
                value2.split("[\\s\\-_./]+");

        int matches = 0;

        for (String t1 : tokens1) {

            for (String t2 : tokens2) {

                if (t1.equals(t2)) {
                    matches++;
                }
            }
        }

        if (matches > 0) {

            return Math.max(
                    0.85,
                    (double) matches /
                            Math.max(tokens1.length,
                                    tokens2.length));
        }

        int commonChars = 0;

        for (char c : value1.toCharArray()) {

            if (value2.indexOf(c) >= 0) {
                commonChars++;
            }
        }

        return (double) commonChars /
                Math.max(value1.length(),
                        value2.length());
    }
}