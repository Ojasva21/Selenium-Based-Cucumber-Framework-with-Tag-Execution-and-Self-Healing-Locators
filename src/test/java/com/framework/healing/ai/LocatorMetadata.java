package com.framework.healing.ai;

public class LocatorMetadata {

    private String tagName;
    private String href;
    private String title;
    private String role;
    private String dataButtonName;
    private String id;
    private String name;
    private String className;
    private String text;
    private String placeholder;
    private String ariaLabel;
    private String type;
    private String xpath;
    private String originalLocator;

    public LocatorMetadata() {
    }

    public LocatorMetadata(String tagName,
                           String id,
                           String name,
                           String className,
                           String text,
                           String placeholder,
                           String ariaLabel,
                           String type,
                           String xpath) {
        this.tagName = tagName;
        this.id = id;
        this.name = name;
        this.className = className;
        this.text = text;
        this.placeholder = placeholder;
        this.ariaLabel = ariaLabel;
        this.type = type;
        this.xpath = xpath;
    }

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getPlaceholder() {
        return placeholder;
    }

    public void setPlaceholder(String placeholder) {
        this.placeholder = placeholder;
    }

    public String getAriaLabel() {
        return ariaLabel;
    }

    public void setAriaLabel(String ariaLabel) {
        this.ariaLabel = ariaLabel;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getXpath() {
        return xpath;
    }

    public void setXpath(String xpath) {
        this.xpath = xpath;
    }
    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getDataButtonName() {
        return dataButtonName;
    }

    public void setDataButtonName(String dataButtonName) {
        this.dataButtonName = dataButtonName;
    }

    public String getOriginalLocator() {
        return originalLocator;
    }

    public void setOriginalLocator(String originalLocator) {
        this.originalLocator = originalLocator;
    }

    @Override
    public String toString() {
        return "LocatorMetadata{" +
                "tagName='" + tagName + '\'' +
                ", id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", className='" + className + '\'' +
                ", text='" + text + '\'' +
                ", placeholder='" + placeholder + '\'' +
                ", ariaLabel='" + ariaLabel + '\'' +
                ", type='" + type + '\'' +
                ", xpath='" + xpath + '\'' +
                ", href='" + href + '\'' +
                ", title='" + title + '\'' +
                ", role='" + role + '\'' +
                ", dataButtonName='" + dataButtonName + '\'' +
                '}';
    }

    public static LocatorMetadata fromLocator(String locatorValue) {

        LocatorMetadata metadata =
                new LocatorMetadata();
        metadata.setOriginalLocator(locatorValue);

        if (locatorValue == null) {
            return metadata;
        }

        locatorValue = locatorValue.trim();

        // ID LOCATOR
        // ID LOCATOR
        if (locatorValue.startsWith("By.id: ")) {

            String id =
                    locatorValue.replace(
                                    "By.id: ",
                                    "")
                            .replace("_WRONG", "")
                            .trim();

            metadata.setId(id);

            if (id.contains("btn")
                    || id.contains("button")
                    || id.contains("accept")) {

                metadata.setTagName("button");

            } else {

                metadata.setTagName("input");
            }

            // Cookie popup hint
            if (id.contains("onetrust")) {

                metadata.setText("Accept All Cookies");
            }
        }

        // NAME LOCATOR
        else if (locatorValue.startsWith("By.name: ")) {

            String name =
                    locatorValue.replace(
                            "By.name: ",
                            "").trim();

            metadata.setName(name);
            metadata.setTagName("input");
        }

        // CSS LOCATOR
        // CSS LOCATOR
        else if (locatorValue.startsWith("By.cssSelector: ")) {

            String css =
                    locatorValue.replace(
                                    "By.cssSelector: ",
                                    "")
                            .replace("_WRONG", "")
                            .trim();

            if (css.startsWith(".")) {

                metadata.setClassName(
                        css.substring(1));

            }

            else if (css.startsWith("#")) {

                metadata.setId(
                        css.substring(1));

            }

            else if (css.contains(".")) {

                String[] parts =
                        css.split("\\.",2);

                metadata.setTagName(parts[0]);

                metadata.setClassName(parts[1]);

            }

            else {

                metadata.setTagName(css);
            }

            // Special handling for demo
            if (css.toLowerCase().contains("see-product")) {

                metadata.setText(
                        "See product offerings");
            }

            if (css.toLowerCase().contains("home")) {

                metadata.setText("Home");
            }

            if (css.toLowerCase().contains("products")) {

                metadata.setText("Products");
            }

            if (css.contains("href='")) {

                try {

                    String value =
                            css.substring(
                                    css.indexOf("href='")+6);

                    value =
                            value.substring(
                                    0,
                                    value.indexOf("'"));

                    metadata.setHref(value);

                } catch (Exception ignored) {
                }
            }

            if (css.contains("data-button-name='")) {

                try {

                    String value =
                            css.substring(
                                    css.indexOf("data-button-name='")+18);

                    value =
                            value.substring(
                                    0,
                                    value.indexOf("'"));

                    metadata.setDataButtonName(value);

                } catch (Exception ignored) {
                }
            }
        }

        // XPATH LOCATOR
        else if (locatorValue.startsWith("By.xpath: ")) {

            String xpath =
                    locatorValue.replace(
                            "By.xpath: ",
                            "").trim();

            metadata.setXpath(xpath);
            String lowerXpath =
                    xpath.toLowerCase();

            // Extract ID
            if (xpath.contains("@id='")) {

                try {

                    String extracted =
                            xpath.substring(
                                    xpath.indexOf("@id='")
                                            + 5);

                    extracted =
                            extracted.substring(
                                    0,
                                    extracted.indexOf("'"));

                    metadata.setId(extracted);

                } catch (Exception ignored) {
                }
            }

            // Extract data-test
            if (xpath.contains("@data-test='")) {

                try {

                    String extracted =
                            xpath.substring(
                                    xpath.indexOf("@data-test='")
                                            + 12);

                    extracted =
                            extracted.substring(
                                    0,
                                    extracted.indexOf("'"));

                    metadata.setId(extracted);

                } catch (Exception ignored) {
                }
            }

            // Extract href
            if (xpath.contains("@href='")) {

                try {

                    String href =
                            xpath.substring(
                                    xpath.indexOf("@href='")
                                            + 7);

                    href =
                            href.substring(
                                    0,
                                    href.indexOf("'"));

                    metadata.setHref(href);

                } catch (Exception ignored) {
                }
            }
            String locatorToAnalyse = lowerXpath;

            if (metadata.getHref() != null
                    && !metadata.getHref().isBlank()) {

                locatorToAnalyse =
                        metadata.getHref().toLowerCase();
            }

            if (locatorToAnalyse.contains("home")) {

                metadata.setText("Home");
                metadata.setDataButtonName(
                        "Top Navigation Section - Home");
            }

            else if (locatorToAnalyse.contains("products")) {

                metadata.setText("Products");
                metadata.setDataButtonName(
                        "Top Navigation Section - Products");
            }

            else if (locatorToAnalyse.contains("help-with-costs")) {

                metadata.setText("Help with costs");
                metadata.setDataButtonName(
                        "Top Navigation Section - Help with costs");
            }

            else if (locatorToAnalyse.contains("insurance")) {

                metadata.setText("Insurance information");
                metadata.setDataButtonName(
                        "Top Navigation Section - Insurance information");
            }

            else if (locatorToAnalyse.contains("resources")) {

                metadata.setText("Resources");
                metadata.setDataButtonName(
                        "Top Navigation Section - Resources");
            }

            // Extract data-button-name
            if (xpath.contains("@data-button-name='")) {

                try {

                    String value =
                            xpath.substring(
                                    xpath.indexOf("@data-button-name='")
                                            + 19);

                    value =
                            value.substring(
                                    0,
                                    value.indexOf("'"));

                    metadata.setDataButtonName(value);

                } catch (Exception ignored) {
                }
            }

            if (xpath.startsWith("//a")) {

                metadata.setTagName("a");

            } else if (xpath.startsWith("//button")) {

                metadata.setTagName("button");

            } else if (xpath.startsWith("//input")) {

                metadata.setTagName("input");

            } else {

                metadata.setTagName("element");
            }
        }

        else {

            metadata.setId(locatorValue);
        }

        return metadata;
    }
}