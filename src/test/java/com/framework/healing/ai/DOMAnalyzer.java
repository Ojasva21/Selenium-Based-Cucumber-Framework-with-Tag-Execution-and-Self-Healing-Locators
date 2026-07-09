package com.framework.healing.ai;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.List;

public class DOMAnalyzer {

    public List<LocatorMetadata> scanPage(WebDriver driver) {

        List<LocatorMetadata> elementsMetadata =
                new ArrayList<>();

        List<WebElement> elements =
                driver.findElements(By.xpath("//*"));

        for (WebElement element : elements) {

            try {

                String tag =
                        element.getTagName().toLowerCase();

                // Ignore non-interactive HTML elements
                // Only consider interactive elements
                if (!(tag.equals("a")
                        || tag.equals("button")
                        || tag.equals("input")
                        || tag.equals("select")
                        || tag.equals("textarea"))) {

                    continue;
                }

                LocatorMetadata metadata =
                        new LocatorMetadata();

                // Basic HTML Information
                metadata.setTagName(tag);

                metadata.setId(
                        element.getAttribute("id"));

                metadata.setName(
                        element.getAttribute("name"));

                metadata.setClassName(
                        element.getAttribute("class"));

                metadata.setText(
                        element.getText() == null
                                ? ""
                                : element.getText().trim());

                metadata.setPlaceholder(
                        element.getAttribute("placeholder"));

                metadata.setAriaLabel(
                        element.getAttribute("aria-label"));

                metadata.setType(
                        element.getAttribute("type"));

                // NovoCare Specific Attributes
                metadata.setHref(
                        element.getAttribute("href"));

                metadata.setTitle(
                        element.getAttribute("title"));

                metadata.setRole(
                        element.getAttribute("role"));

                metadata.setDataButtonName(
                        element.getAttribute("data-button-name"));

                // Skip elements that have no useful identifying information
                boolean usefulElement =
                        (metadata.getId() != null
                                && !metadata.getId().isBlank())
                                || (metadata.getHref() != null
                                && !metadata.getHref().isBlank())
                                || (metadata.getText() != null
                                && !metadata.getText().isBlank())
                                || (metadata.getDataButtonName() != null
                                && !metadata.getDataButtonName().isBlank())
                                || (metadata.getAriaLabel() != null
                                && !metadata.getAriaLabel().isBlank());

                if (!usefulElement) {
                    continue;
                }

                elementsMetadata.add(metadata);

            } catch (Exception ignored) {
            }
        }

        return elementsMetadata;
    }
}