package com.framework.stepDefinitions;

import com.framework.driver.DriverFactory;
import com.framework.pages.NovoCareHomePage;
import io.cucumber.java.en.*;
import org.openqa.selenium.WebDriver;

import static org.testng.Assert.assertTrue;

public class NovoCareStepDefinition {

    private WebDriver driver;
    private NovoCareHomePage homePage;

    @Given("User launches NovoCare website")
    public void userLaunchesNovoCareWebsite() {

        driver = DriverFactory.getDriver();

        driver.get("https://www.novocare.com/diabetes/home.html");

        homePage = new NovoCareHomePage(driver);
    }

    @And("User accepts cookies")
    public void userAcceptsCookies() {

        homePage.acceptCookies();
    }

    @When("User clicks Home")
    public void userClicksHome() {

        homePage.clickHome();
    }

    @Then("Home page should open")
    public void homePageShouldOpen() {

        assertTrue(
                driver.getCurrentUrl().contains("home"),
                "Home page was not opened.");
    }

    @When("User opens Products menu")
    public void userOpensProductsMenu() {

        homePage.openProductsMenu();
    }

    @And("User clicks NovoFine Needles")
    public void userClicksNovoFineNeedles() {

        homePage.clickNovoFineNeedles();
    }

    @Then("NovoFine Needles page should open")
    public void novoFineNeedlesPageShouldOpen() {

        assertTrue(
                driver.getCurrentUrl().contains("novofine"),
                "NovoFine Needles page was not opened.");
    }

    @When("User clicks See Product Offerings")
    public void userClicksSeeProductOfferings() {

        homePage.clickSeeProductOfferings();
    }

    @Then("Product selection drawer should be displayed")
    public void productOfferingsPageShouldOpen() {

        assertTrue(
                homePage.isProductSelectionDrawerDisplayed(),
                "Product selection drawer was not displayed.");
    }
}