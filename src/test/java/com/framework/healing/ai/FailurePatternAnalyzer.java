package com.framework.healing.ai;

import java.util.HashMap;
import java.util.Map;

public class FailurePatternAnalyzer {

    private final Map<String, Integer> locatorFailures =
            new HashMap<>();

    private int totalHealingAttempts = 0;

    public void recordFailure(String locator) {

        locatorFailures.put(
                locator,
                locatorFailures.getOrDefault(locator, 0) + 1
        );

        totalHealingAttempts++;
    }

    public String getMostFailedLocator() {

        String mostFailedLocator = "N/A";
        int maxFailures = 0;

        for (Map.Entry<String, Integer> entry :
                locatorFailures.entrySet()) {

            if (entry.getValue() > maxFailures) {

                maxFailures = entry.getValue();
                mostFailedLocator = entry.getKey();
            }
        }

        return mostFailedLocator;
    }

    public int getFailureCount(String locator) {

        return locatorFailures.getOrDefault(locator, 0);
    }

    public int getTotalHealingAttempts() {

        return totalHealingAttempts;
    }

    public void printAnalysis() {

        System.out.println("\n========== FAILURE ANALYSIS ==========");

        System.out.println(
                "Most Failed Locator : "
                        + getMostFailedLocator());

        System.out.println(
                "Total Healing Attempts : "
                        + totalHealingAttempts);

        System.out.println(
                "======================================");
    }
}