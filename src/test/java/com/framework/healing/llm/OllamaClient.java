package com.framework.healing.llm;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class OllamaClient {

    public String askLlama(String prompt) {

        try {

            URL url =
                    new URL("http://localhost:11434/api/generate");

            HttpURLConnection connection =
                    (HttpURLConnection) url.openConnection();

            connection.setRequestMethod("POST");
            connection.setRequestProperty(
                    "Content-Type",
                    "application/json");

            connection.setDoOutput(true);
            connection.setConnectTimeout(30000);
            connection.setReadTimeout(30000);

            String escapedPrompt =
                    prompt
                            .replace("\\", "\\\\")
                            .replace("\"", "\\\"")
                            .replace("\n", "\\n")
                            .replace("\r", "");

            String payload =
                    "{"
                            + "\"model\":\"llama3\","
                            + "\"prompt\":\"" + escapedPrompt + "\","
                            + "\"stream\":false,"
                            + "\"temperature\":0,"
                            + "\"top_p\":0.2,"
                            + "\"repeat_penalty\":1.0"
                            + "}";

            connection.getOutputStream()
                    .write(payload.getBytes("UTF-8"));

            connection.getOutputStream().flush();
            connection.getOutputStream().close();

            BufferedReader reader =
                    new BufferedReader(
                            new InputStreamReader(
                                    connection.getInputStream()));

            StringBuilder response =
                    new StringBuilder();

            String line;

            while ((line = reader.readLine()) != null) {

                response.append(line);
            }

            reader.close();

            String fullResponse = response.toString();

            System.out.println("\n========== RAW OLLAMA RESPONSE ==========");
            System.out.println(fullResponse);
            System.out.println("=========================================");

// Extract only the "response" field
            String marker = "\"response\":\"";

            int start = fullResponse.indexOf(marker);

            if (start == -1) {
                return null;
            }

            start += marker.length();

            int end = fullResponse.indexOf("\",\"done\"", start);

            if (end == -1) {
                end = fullResponse.indexOf("\"}", start);
            }

            if (end == -1) {
                return null;
            }

            String llmResponse =
                    fullResponse.substring(start, end)
                            .replace("\\\"", "\"")
                            .replace("\\n", "\n")
                            .replace("\\\\", "\\");

            System.out.println("\n========== EXTRACTED LLM RESPONSE ==========");
            System.out.println(llmResponse);
            System.out.println("============================================");

            return llmResponse;

        } catch (Exception e) {

            e.printStackTrace();
        }

        return null;
    }
}