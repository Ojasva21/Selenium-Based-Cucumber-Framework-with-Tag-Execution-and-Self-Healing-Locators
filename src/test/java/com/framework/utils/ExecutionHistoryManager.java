package com.framework.utils;

import com.framework.healing.ai.HealingContext;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ExecutionHistoryManager {

    private static final String HISTORY_FILE =
            "test-output/ExecutionHistory.html";

    public static void logExecution(
            String scenarioName,
            String status) {

        try {

            File file = new File(HISTORY_FILE);

            if (!file.exists()) {
                createDashboardFile();
            }

            String timestamp =
                    new SimpleDateFormat(
                            "dd-MM-yyyy HH:mm:ss")
                            .format(new Date());

            String mode =
                    safe(HealingContext.getHealingMode());

            String failedLocator =
                    safe(HealingContext.getFailedLocator());

            String healedLocator =
                    safe(HealingContext.getHealedLocator());

            double confidence =
                    HealingContext.getConfidenceScore();

            String row =
                    "<tr>" +
                            "<td>" + timestamp + "</td>" +
                            "<td>" + scenarioName + "</td>" +
                            "<td><span class='mode " + mode + "'>" + mode + "</span></td>" +
                            "<td>" + failedLocator + "</td>" +
                            "<td>" + healedLocator + "</td>" +
                            "<td>" +
                            "<div class='progress-container'>" +
                            "<div class='progress-bar' style='width:" + confidence + "%'>" +
                            confidence +
                            "</div>" +
                            "</div>" +
                            "</td>" +
                            "<td><span class='status'>" + status + "</span></td>" +
                            "</tr>";

            insertRow(row);

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    private static void createDashboardFile() throws IOException {

        BufferedWriter writer =
                new BufferedWriter(
                        new FileWriter(HISTORY_FILE));

        writer.write(
                "<html>" +
                        "<head>" +
                        "<title>Execution History Dashboard</title>" +

                        "<style>" +

                        "body{" +
                        "font-family:Segoe UI;" +
                        "background:#f4f7fc;" +
                        "margin:0;" +
                        "padding:30px;" +
                        "}" +

                        ".header{" +
                        "background:#1f4e79;" +
                        "color:white;" +
                        "padding:20px;" +
                        "border-radius:12px;" +
                        "margin-bottom:25px;" +
                        "}" +

                        ".cards{" +
                        "display:flex;" +
                        "gap:20px;" +
                        "margin-bottom:25px;" +
                        "flex-wrap:wrap;" +
                        "}" +

                        ".card{" +
                        "background:white;" +
                        "padding:20px;" +
                        "border-radius:12px;" +
                        "box-shadow:0 2px 10px rgba(0,0,0,0.15);" +
                        "width:220px;" +
                        "}" +

                        ".card h3{" +
                        "margin:0;" +
                        "color:#1f4e79;" +
                        "}" +

                        ".card p{" +
                        "font-size:32px;" +
                        "font-weight:bold;" +
                        "margin-top:15px;" +
                        "}" +

                        "table{" +
                        "width:100%;" +
                        "border-collapse:collapse;" +
                        "background:white;" +
                        "box-shadow:0 2px 10px rgba(0,0,0,0.15);" +
                        "}" +

                        "th{" +
                        "background:#4472C4;" +
                        "color:white;" +
                        "padding:14px;" +
                        "}" +

                        "td{" +
                        "padding:12px;" +
                        "border-bottom:1px solid #ddd;" +
                        "}" +

                        "tr:hover{" +
                        "background:#eef5ff;" +
                        "}" +

                        ".mode{" +
                        "padding:5px 12px;" +
                        "border-radius:15px;" +
                        "color:white;" +
                        "font-weight:bold;" +
                        "}" +

                        ".AI{" +
                        "background:#1976D2;" +
                        "}" +

                        ".NON_AI{" +
                        "background:#2E7D32;" +
                        "}" +

                        ".LLM_AI{" +
                        "background:#9C27B0;" +
                        "}" +

                        ".HYBRID{" +
                        "background:#FF9800;" +
                        "}" +

                        ".status{" +
                        "background:#4CAF50;" +
                        "color:white;" +
                        "padding:5px 12px;" +
                        "border-radius:15px;" +
                        "}" +

                        ".progress-container{" +
                        "background:#ddd;" +
                        "border-radius:10px;" +
                        "overflow:hidden;" +
                        "}" +

                        ".progress-bar{" +
                        "background:#4CAF50;" +
                        "color:white;" +
                        "text-align:center;" +
                        "padding:4px;" +
                        "}" +

                        "</style>" +
                        "</head>" +

                        "<body>" +

                        "<div class='header'>" +
                        "<h1>AI-Powered Self-Healing Automation Dashboard</h1>" +
                        "<h3>Selenium-Cucumber Framework Execution History</h3>" +
                        "</div>" +

                        "<div class='cards'>" +

                        "<div class='card'>" +
                        "<h3>Total Executions</h3>" +
                        "<p id='totalRuns'>0</p>" +
                        "</div>" +

                        "<div class='card'>" +
                        "<h3>LLM AI Heals</h3>" +
                        "<p id='llmAiCount'>0</p>" +
                        "</div>" +

                        "<div class='card'>" +
                        "<h3>NON-AI Heals</h3>" +
                        "<p id='nonAiCount'>0</p>" +
                        "</div>" +

                        "<div class='card'>" +
                        "<h3>Hybrid Heals</h3>" +
                        "<p id='hybridCount'>0</p>" +
                        "</div>" +

                        "<div class='card'>" +
                        "<h3>Average Confidence</h3>" +
                        "<p id='avgConfidence'>0%</p>" +
                        "</div>" +

                        "</div>" +

                        "<table>" +

                        "<thead>" +
                        "<tr>" +
                        "<th>Timestamp</th>" +
                        "<th>Scenario</th>" +
                        "<th>Mode</th>" +
                        "<th>Failed Locator</th>" +
                        "<th>Healed Locator</th>" +
                        "<th>Confidence</th>" +
                        "<th>Status</th>" +
                        "</tr>" +
                        "</thead>" +

                        "<tbody id='historyTable'>" +
                        "</tbody>" +

                        "</table>" +

                        "<script>" +

                        "window.onload=function(){" +

                        "let rows=document.querySelectorAll('#historyTable tr');" +

                        "let total=rows.length;" +

                        "let llmAi=0;" +

                        "let nonAi=0;" +

                        "let hybrid=0;" +

                        "let confidenceTotal=0;" +

                        "rows.forEach(function(row){" +

                        "let mode=row.cells[2].innerText.trim();" +

                        "let confidence=parseFloat(row.cells[5].innerText.trim())||0;" +

                        "if(mode==='LLM_AI'){llmAi++;}" +

                        "if(mode==='NON_AI'){nonAi++;}" +

                        "if(mode==='HYBRID'){hybrid++;}" +

                        "confidenceTotal+=confidence;" +

                        "});" +

                        "let avg=total>0?(confidenceTotal/total).toFixed(1):0;" +

                        "document.getElementById('totalRuns').innerText=total;" +

                        "document.getElementById('llmAiCount').innerText=llmAi;" +

                        "document.getElementById('nonAiCount').innerText=nonAi;" +

                        "document.getElementById('hybridCount').innerText=hybrid;" +

                        "document.getElementById('avgConfidence').innerText=avg+'%';" +

                        "};" +

                        "</script>" +

                        "</body></html>"
        );

        writer.close();
    }

    private static void insertRow(String row) throws Exception {

        File file = new File(HISTORY_FILE);

        String content =
                new String(
                        java.nio.file.Files.readAllBytes(
                                file.toPath()));

        content =
                content.replace(
                        "<tbody id='historyTable'>",
                        "<tbody id='historyTable'>" + row);

        java.nio.file.Files.write(
                file.toPath(),
                content.getBytes());
    }

    private static String safe(String value) {

        return value == null ? "-" : value;
    }

}
