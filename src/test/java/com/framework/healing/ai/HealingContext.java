package com.framework.healing.ai;

public class HealingContext {

    private static String healingMode;
    private static String failedLocator;
    private static String healedLocator;
    private static double confidenceScore;
    private static String status;

    public static void setHealingData(
            String mode,
            String failed,
            String healed,
            double confidence,
            String healStatus) {

        healingMode = mode;
        failedLocator = failed;
        healedLocator = healed;
        confidenceScore = confidence;
        status = healStatus;
    }

    public static String getHealingMode() {
        return healingMode;
    }

    public static String getFailedLocator() {
        return failedLocator;
    }

    public static String getHealedLocator() {
        return healedLocator;
    }

    public static double getConfidenceScore() {
        return confidenceScore;
    }

    public static String getStatus() {
        return status;
    }

    public static void clear() {

        healingMode = null;
        failedLocator = null;
        healedLocator = null;
        confidenceScore = 0;
        status = null;
    }
}