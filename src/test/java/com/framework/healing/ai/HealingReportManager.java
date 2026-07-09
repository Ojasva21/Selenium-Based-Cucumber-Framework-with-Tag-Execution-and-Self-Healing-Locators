package com.framework.healing.ai;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class HealingReportManager {

    private static final String REPORT_FILE =
            "test-output/AI-Healing-Report.txt";

    public void generateReport(
            String failedLocator,
            String healedLocator,
            double confidenceScore) {

        try (BufferedWriter writer =
                     new BufferedWriter(
                             new FileWriter(
                                     REPORT_FILE,
                                     true))) {

            String status;
            String recommendation;

            if (confidenceScore >= 70) {

                status = "HIGH CONFIDENCE HEAL";

                recommendation =
                        "Safe to update locator in Page Object";
            } else {

                status = "LOW CONFIDENCE HEAL";

                recommendation =
                        "Manual review recommended";
            }

            writer.write("\n");
            writer.write("========================================");
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
                    "CONFIDENCE SCORE : "
                            + confidenceScore);
            writer.newLine();

            writer.write(
                    "STATUS : "
                            + status);
            writer.newLine();

            writer.write(
                    "RECOMMENDATION : "
                            + recommendation);
            writer.newLine();

            writer.write("========================================");
            writer.newLine();

        } catch (IOException e) {

            e.printStackTrace();
        }
    }
}