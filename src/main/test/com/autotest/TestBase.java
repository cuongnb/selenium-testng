package com.autotest;

import com.autotest.data.TestResult;
import com.autotest.utils.SaveExcelUtils;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public abstract class TestBase {
    protected WebDriver driver;
    protected List<TestResult> testResults;

    @BeforeClass(alwaysRun = true)
    public void suiteSetUp() {
        testResults = new ArrayList<>();
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

    @Parameters({"filePath", "sheetName"})
    @AfterClass
    public void suiteTearDown(String filePath, String sheetName) {
        SaveExcelUtils.saveResult(testResults, filePath, sheetName);
        // close the browser
        driver.close();
        driver.quit();
    }
}
