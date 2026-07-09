package com.framework.healing.ai;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class NonAIHealingReportManager {

    private static final String REPORT_FILE =
            "test-output/NON_AI-Healing-Report.txt";

    public void generateReport(
            String failedLocator,
            String healedLocator) {

        try (BufferedWriter writer =
                     new BufferedWriter(
                             new FileWriter(
                                     REPORT_FILE,
                                     true))) {

            writer.write("\n");
            writer.write("========================================");
            writer.newLine();

            writer.write(
                    "HEALING TYPE : NON_AI");
            writer.newLine();

            writer.write(
                    "FAILED LOCATOR : "
                            + failedLocator);
            writer.newLine();

            writer.write(
                    "HEALED LOCATOR : "
                            + healedLocator);
            writer.newLine();

            writer.write(
                    "STATUS : SUCCESS");
            writer.newLine();

            writer.write("========================================");
            writer.newLine();

        } catch (IOException e) {

            e.printStackTrace();
        }
    }
}