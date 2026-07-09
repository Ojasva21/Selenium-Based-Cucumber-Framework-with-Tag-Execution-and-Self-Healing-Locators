package com.framework.healing.llm;

import com.framework.healing.ai.LocatorMetadata;

import java.util.List;

public class PromptBuilder {

    public String buildPrompt(
            LocatorMetadata failedElement,
            List<LocatorMetadata> candidates) {

        StringBuilder prompt =
                new StringBuilder();

        prompt.append(
                "You are an AI locator healing engine.\n\n");

        prompt.append(
                "Your task is to identify the single best matching HTML element.\n");

        prompt.append(
                "Use semantic understanding together with locator similarity.\n" +
                        "Navigation links pointing to the same page are stronger matches than unrelated links.\n" +
                        "Ignore '_WRONG' suffixes while comparing href values.\n");

        prompt.append(
                "Failed Element:\n");

        prompt.append("TAG = ")
                .append(failedElement.getTagName())
                .append("\n");

        prompt.append("ID = ")
                .append(failedElement.getId())
                .append("\n");

        prompt.append("NAME = ")
                .append(failedElement.getName())
                .append("\n");

        prompt.append("TEXT = ")
                .append(failedElement.getText())
                .append("\n");

        prompt.append("CLASS = ")
                .append(failedElement.getClassName())
                .append("\n");

        prompt.append("HREF = ")
                .append(failedElement.getHref())
                .append("\n");

        prompt.append("DATA BUTTON NAME = ")
                .append(failedElement.getDataButtonName())
                .append("\n");

        prompt.append("ROLE = ")
                .append(failedElement.getRole())
                .append("\n");

        prompt.append("TITLE = ")
                .append(failedElement.getTitle())
                .append("\n");

        prompt.append("TYPE = ")
                .append(failedElement.getType())
                .append("\n\n");

        prompt.append(
                "Candidate Elements:\n\n");

        int count = 1;

        for (LocatorMetadata candidate : candidates) {

            prompt.append("Candidate ")
                    .append(count++)
                    .append(" ------------------\n");

            prompt.append("TAG = ")
                    .append(candidate.getTagName())
                    .append("\n");

            prompt.append("ID = ")
                    .append(candidate.getId())
                    .append("\n");

            prompt.append("NAME = ")
                    .append(candidate.getName())
                    .append("\n");

            prompt.append("TEXT = ")
                    .append(candidate.getText())
                    .append("\n");

            prompt.append("CLASS = ")
                    .append(candidate.getClassName())
                    .append("\n");

            prompt.append("HREF = ")
                    .append(candidate.getHref())
                    .append("\n");

            prompt.append("DATA BUTTON NAME = ")
                    .append(candidate.getDataButtonName())
                    .append("\n");

            prompt.append("ROLE = ")
                    .append(candidate.getRole())
                    .append("\n");

            prompt.append("TITLE = ")
                    .append(candidate.getTitle())
                    .append("\n");

            prompt.append("TYPE = ")
                    .append(candidate.getType())
                    .append("\n");
            prompt.append("END OF CANDIDATE\n\n");
            prompt.append("----------------------------\n");
        }

        prompt.append(
                "IMPORTANT:\n");

        prompt.append(
                "The failed locator may contain '_WRONG'. Ignore this suffix while matching.\n");

        prompt.append(
                "Choose the element that represents the same page or navigation target.\n\n");

        prompt.append(
                "Choose ONLY ONE candidate.\n\n");

        prompt.append(
                "Selection Rules:\n");

        prompt.append(
                "- Prefer elements whose HREF is most similar to the failed HREF.\n");

        prompt.append(
                "- Prefer elements having the same TAG.\n");

        prompt.append(
                "- Prefer elements having similar visible TEXT.\n");

        prompt.append(
                "- Prefer elements having similar DATA BUTTON NAME.\n");

        prompt.append(
                "- Never choose Cookie Policy, Privacy Policy, Terms, Careers, Contact or footer links unless they exactly match the failed element.\n");

        prompt.append(
                "- Never return Candidate numbers.\n");

        prompt.append(
                "- Never explain your answer.\n");

        prompt.append(
                "- Return ONLY one locator value.\n\n");

        prompt.append(
                "Priority:\n");

        prompt.append(
                "1. ID\n");

        prompt.append(
                "2. HREF\n");

        prompt.append(
                "3. DATA BUTTON NAME\n");

        prompt.append(
                "4. TEXT\n\n");

        prompt.append(
                "Return ONLY valid JSON like:\n");

        prompt.append(
                "{\"best_locator\":\"locator_value\"}");

        return prompt.toString();
    }
}