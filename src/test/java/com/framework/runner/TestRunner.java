package com.framework.runner;

import com.framework.reports.ReportArchiveManager;
import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;

@CucumberOptions(
        features = "src/test/resources/features/NovoCare.feature",
        glue = {
                "com.framework.stepDefinitions",
                "com.framework.hooks"
        },
        plugin = {
                "pretty",
                "html:test-output/cucumber-report.html",
                "com.aventstack.extentreports.cucumber.adapter.ExtentCucumberAdapter:"
        },
        monochrome = true,
        publish = false,
        tags = "@novocare"
)
public class TestRunner extends AbstractTestNGCucumberTests {

        static {

                ReportArchiveManager.registerShutdownHook();
        }
}