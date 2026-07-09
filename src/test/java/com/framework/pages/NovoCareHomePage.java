package com.framework.pages;

import com.framework.utils.SmartElementFinder;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class NovoCareHomePage {

    private final WebDriver driver;

    public NovoCareHomePage(WebDriver driver) {
        this.driver = driver;
    }

    // Cookie Popup
    //private final By acceptCookiesButton = By.id("onetrust-accept-btn-handler");
    private final By acceptCookiesButton = By.id("onetrust-accept-btn-handler-WRONG");

    // Home (Broken intentionally for Self-Healing Demo)
    private final By homeButton = By.xpath("//a[@href='/diabetes/home_WRONG.html']");
    //private final By homeButton = By.xpath("//a[@href='/diabetes/home.html']");

    // Products
    private final By productsMenu =
            By.xpath("//a[@href='/diabetes/products.html']");

    // NovoFine Needles
    private final By novoFineNeedles =
            By.xpath("//a[contains(@href,'novofine-needle-options')]");

    // See Product Offerings
    //private final By seeProductOfferings = By.xpath("//span[normalize-space()='See product offerings']");
    private final By seeProductOfferings = By.cssSelector("a.see-product-offerings-WRONG");
    //private final By seeProductOfferings = By.cssSelector("a.btn.btn-cvi-compliance.custom-btn-style-f33d8b5a609f2b8051497109b85e6153f7eacd52a7a703853bebfa8176dfcf79");

    private final By productSelectionHeading =
            By.xpath("//*[contains(normalize-space(.),'Which medicine or product are you interested in')]");

    public void acceptCookies() {

        try {

            WebDriverWait wait =
                    new WebDriverWait(driver, Duration.ofSeconds(10));

            WebElement button =
                    SmartElementFinder.find(driver, acceptCookiesButton);

            wait.until(
                    ExpectedConditions.elementToBeClickable(button));

            button.click();

        } catch (Exception ignored) {

            System.out.println("Cookie popup not displayed.");
        }
    }

    public void clickHome() {
        click(homeButton);
    }

    public void openProductsMenu() {
        click(productsMenu);
    }

    public void clickNovoFineNeedles() {
        click(novoFineNeedles);
    }

    public void clickSeeProductOfferings() {
        click(seeProductOfferings);
    }

    /**
     * Generic click method supporting healed elements.
     */
    private void click(By locator) {

        WebDriverWait wait =
                new WebDriverWait(driver, Duration.ofSeconds(10));

        // Heal only once
        WebElement element =
                SmartElementFinder.find(driver, locator);

        try {

            ((JavascriptExecutor) driver)
                    .executeScript(
                            "arguments[0].scrollIntoView({block:'center'});",
                            element);

            wait.until(
                    ExpectedConditions.elementToBeClickable(element));

            element.click();

        } catch (Exception e) {

            System.out.println(
                    "Normal click failed. Falling back to JavaScript click...");

            try {

                // IMPORTANT:
                // DO NOT call SmartElementFinder.find() again.
                // Reuse the already healed element.

                ((JavascriptExecutor) driver)
                        .executeScript(
                                "arguments[0].scrollIntoView({block:'center'});",
                                element);

                ((JavascriptExecutor) driver)
                        .executeScript(
                                "arguments[0].click();",
                                element);

                System.out.println(
                        "JavaScript click successful.");

            } catch (Exception ex) {

                ex.printStackTrace();

                throw new RuntimeException(
                        "Unable to click healed element.",
                        ex);
            }
        }
    }
    public boolean isProductSelectionDrawerDisplayed() {

        try {

            WebDriverWait wait =
                    new WebDriverWait(driver, Duration.ofSeconds(20));

            wait.until(ExpectedConditions.visibilityOfElementLocated(
                    productSelectionHeading));

            return true;

        } catch (Exception e) {

            System.out.println("Current URL: " + driver.getCurrentUrl());

            System.out.println("Drawer heading not found.");

            return false;
        }
    }
}