package com.framework.tests;

import com.framework.driver.DriverFactory;
import com.framework.pages.NovoCareHomePage;
import org.junit.Test;
import org.openqa.selenium.WebDriver;

public class NovoCareTest {

    @Test
    public void testHomeNavigation() {

        WebDriver driver =
                DriverFactory.getDriver();

        driver.get("https://www.novocare.com/diabetes/home.html");

        NovoCareHomePage page =
                new NovoCareHomePage(driver);

        page.acceptCookies();

        page.clickHome();

        DriverFactory.quitDriver();
    }

    @Test
    public void testProductsNavigation() throws InterruptedException {

        WebDriver driver =
                DriverFactory.getDriver();

        driver.get("https://www.novocare.com/diabetes/home.html");

        NovoCareHomePage page =
                new NovoCareHomePage(driver);

        page.acceptCookies();

        page.openProductsMenu();

        Thread.sleep(2000);

        page.clickNovoFineNeedles();

        DriverFactory.quitDriver();
    }

    @Test
    public void testSeeProductOfferings() {

        WebDriver driver =
                DriverFactory.getDriver();

        driver.get("https://www.novocare.com/diabetes/home.html");

        NovoCareHomePage page =
                new NovoCareHomePage(driver);

        page.acceptCookies();

        page.clickSeeProductOfferings();

        DriverFactory.quitDriver();
    }
}