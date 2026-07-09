package com.framework.healing.ai;

import java.util.HashMap;
import java.util.Map;

public class SmartWaitPredictor {

    private final Map<String, Long> waitHistory =
            new HashMap<>();

    public void recordLoadTime(
            String elementName,
            long loadTime) {

        waitHistory.put(elementName, loadTime);
    }

    public long predictWaitTime(
            String elementName) {

        if (waitHistory.containsKey(elementName)) {

            return waitHistory.get(elementName);
        }

        return 3000; // Default 3 seconds
    }

    public void printPredictions() {

        System.out.println(
                "\n========== SMART WAIT PREDICTIONS ==========");

        for (Map.Entry<String, Long> entry :
                waitHistory.entrySet()) {

            System.out.println(
                    entry.getKey()
                            + " -> "
                            + entry.getValue()
                            + " ms");
        }

        System.out.println(
                "============================================");
    }
}