package com.framework.reports;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class ReportArchiveManager {

    private static final String SOURCE_REPORT =
            "test-output/ExtentReport.html";

    private static final String REPORT_ROOT =
            "test-output/reports";

    private static final String REPORT_INDEX =
            "test-output/ReportIndex.csv";

    public static void archiveReport() {

        try {

            Path source =
                    Paths.get(SOURCE_REPORT);

            // Wait up to 30 seconds for Extent report to appear
            int retries = 30;

            while (!Files.exists(source) && retries-- > 0) {

                Thread.sleep(1000);
            }

            if (!Files.exists(source)) {

                System.out.println(
                        "Extent report not found.");

                return;
            }

            // Small wait to ensure writing is finished
            Thread.sleep(2000);

            String date =
                    LocalDate.now()
                            .format(
                                    DateTimeFormatter.ofPattern(
                                            "yyyy-MM-dd"));

            String time =
                    LocalTime.now()
                            .format(
                                    DateTimeFormatter.ofPattern(
                                            "HH-mm-ss"));

            Path destinationFolder =
                    Paths.get(
                            REPORT_ROOT,
                            date,
                            time);

            Files.createDirectories(destinationFolder);

            Path destination =
                    destinationFolder.resolve(
                            "ExtentReport.html");

            Files.copy(
                    source,
                    destination,
                    StandardCopyOption.REPLACE_EXISTING);

            System.out.println(
                    "Extent Report Archived Successfully.");

            updateReportIndex(
                    date,
                    time,
                    destination);

            ReportDashboardGenerator.generateDashboard();
        } catch (Exception e) {

            e.printStackTrace();
        }
    }
    private static void updateReportIndex(
            String date,
            String time,
            Path destination) {

        try (BufferedWriter writer =
                     new BufferedWriter(
                             new FileWriter(
                                     REPORT_INDEX,
                                     true))) {

            Path relativePath =
                    Paths.get("reports")
                            .resolve(date)
                            .resolve(time)
                            .resolve("ExtentReport.html");

            writer.write(
                    date
                            + ","
                            + time
                            + ","
                            + relativePath.toString().replace("\\", "/"));

            writer.newLine();

        } catch (IOException e) {

            e.printStackTrace();
        }
    }
    public static void registerShutdownHook() {

        Runtime.getRuntime().addShutdownHook(

                new Thread(() -> {

                    System.out.println(
                            "Archiving Extent Report...");

                    archiveReport();

                }));
    }
}