package com.autotest;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;

import java.util.concurrent.TimeUnit;

public abstract class SeleniumDriver {
    protected WebDriver driver;

    @BeforeClass(alwaysRun = true)
    public void suiteSetUp() {
        // add test result excel file column header
        // write the header in the first row
        try {
            // Setting up Chrome driver path.
//            System.setProperty("webdriver.chrome.driver", driverPath + "chromedriver.exe");
            // Launching Chrome browser.
            driver = new ChromeDriver();
            driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
        } catch (Exception e) {
            throw new IllegalStateException("Can't start the Firefox Web Driver", e);
        }
    }

    //    @Parameters({"filePath", "sheetName"})
    @AfterClass
    public void suiteTearDown() {
        // close the browser
        driver.close();
        driver.quit();
    }
}
