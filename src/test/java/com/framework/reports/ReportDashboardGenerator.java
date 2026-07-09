package com.framework.reports;

import java.io.*;
import java.nio.file.*;
import java.util.*;

public class ReportDashboardGenerator {

    private static final String INDEX =
            "test-output/ReportIndex.csv";

    private static final String DASHBOARD =
            "test-output/AutomationDashboard.html";

    public static void generateDashboard() {

        try {

            List<String> rows =
                    Files.readAllLines(
                            Paths.get(INDEX));

            StringBuilder html =
                    new StringBuilder();

            html.append("""
<!DOCTYPE html>
<html>

<head>

<title>Automation Dashboard</title>

<style>

body{

font-family:Arial;

background:#f4f6f9;

padding:40px;

}

h1{

text-align:center;

color:#003366;

}

input{

width:350px;

padding:10px;

font-size:16px;

margin-bottom:25px;

}

table{

width:100%;

border-collapse:collapse;

background:white;

}

th{

background:#003366;

color:white;

padding:12px;

}

td{

padding:10px;

border:1px solid #ddd;

text-align:center;

}

tr:nth-child(even){

background:#f8f8f8;

}

a{

text-decoration:none;

color:#0066cc;

font-weight:bold;

}

</style>

</head>

<body>

<h1>

AI SELF HEALING AUTOMATION DASHBOARD

</h1>

<input
type="text"
id="searchBox"
placeholder="Search by Date or Time..."
onkeyup="searchTable()">

<table id="reportTable">

<tr>

<th>#</th>

<th>Date</th>

<th>Time</th>

<th>Report</th>

</tr>

""");

            int serial = 1;

            int startIndex = 0;

            if (!rows.isEmpty()
                    && rows.get(0).startsWith("Date")) {

                startIndex = 1;
            }

            for (int i = rows.size() - 1; i >= startIndex; i--) {

                String[] parts =
                        rows.get(i).split(",");

                html.append("<tr>");

                html.append("<td>")
                        .append(serial++)
                        .append("</td>");

                html.append("<td>")
                        .append(parts[0])
                        .append("</td>");

                html.append("<td>")
                        .append(parts[1])
                        .append("</td>");

                html.append("<td>");

                html.append("<a href=\"")
                        .append(parts[2])
                        .append("\" target=\"_blank\">");

                html.append("Open Report");

                html.append("</a>");

                html.append("</td>");

                html.append("</tr>");
            }

            html.append("""

</table>

<script>

function searchTable(){

let input=document.getElementById("searchBox");

let filter=input.value.toUpperCase();

let table=document.getElementById("reportTable");

let tr=table.getElementsByTagName("tr");

for(let i=1;i<tr.length;i++){

let date=tr[i].getElementsByTagName("td")[1];

let time=tr[i].getElementsByTagName("td")[2];

let txt=(date.innerText+" "+time.innerText).toUpperCase();

tr[i].style.display=txt.indexOf(filter)>-1?"":"none";

}

}

</script>

</body>

</html>

""");

            Files.writeString(

                    Paths.get(DASHBOARD),

                    html.toString());

            System.out.println(
                    "Automation Dashboard Generated.");

        }

        catch (Exception e) {

            e.printStackTrace();
        }

    }

}