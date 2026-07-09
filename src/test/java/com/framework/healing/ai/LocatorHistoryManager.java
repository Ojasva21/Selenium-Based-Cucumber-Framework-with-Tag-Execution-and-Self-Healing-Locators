package com.framework.healing.ai;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class LocatorHistoryManager {

    private static final String HISTORY_FILE =
            "src/test/resources/healing/healing-history.txt";

    public void saveHealingRecord(
            String failedLocator,
            String healedLocator) {

        try (BufferedWriter writer =
                     new BufferedWriter(
                             new FileWriter(
                                     HISTORY_FILE,
                                     true))) {

            writer.write(
                    failedLocator +
                            "=" +
                            healedLocator);

            writer.newLine();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<String> getHealingHistory() {

        try {

            if (!Files.exists(Paths.get(HISTORY_FILE))) {
                return new ArrayList<>();
            }

            return Files.readAllLines(
                    Paths.get(HISTORY_FILE));

        } catch (Exception e) {

            e.printStackTrace();

            return new ArrayList<>();
        }
    }
}