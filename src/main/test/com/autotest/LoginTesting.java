package com.autotest;

import com.autotest.data.TestResult;
import com.autotest.utils.SaveExcelUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.concurrent.TimeUnit;

public class LoginTesting extends TestBase {

    @Test(description = "Opens the TestNG Demo Website for Login Test", priority = 1)
    public void LaunchWebsite() throws Exception {
        try {
            driver.get("http://phptravels.net/login");
            driver.manage().window().maximize();
            TestNGResults.put("2", new Object[]{1d,
                    "Navigate to demo website", "Site gets opened", "Pass"});
            testResults.add(new TestResult().setOrder(1).setStep(1)
                    .setAction("Navigate to demo website")
                    .setExpected("Site gets opened").setActual("Pass"));
        } catch (Exception e) {
            testResults.add(new TestResult().setOrder(1).setStep(1)
                    .setAction("Navigate to demo website")
                    .setExpected("Site gets opened").setActual("Fail"));
            Assert.assertTrue(false);
        }
    }

    @Test(description = "Fill the Login Details", priority = 2)
    public void FillLoginDetails() throws Exception {

        try {
            // Get the username element
            WebElement username = driver.findElement(By.name("username"));
            username.sendKeys("user@phptravels.com");

            // Get the password element
            WebElement password = driver.findElement(By.name("password"));
            password.sendKeys("demouser");

            Thread.sleep(1000);
            testResults.add(new TestResult().setOrder(2).setStep(2)
                    .setAction("Fill Login form data (Username/Password)")
                    .setExpected("Login details gets filled").setActual("Pass"));
        } catch (Exception e) {
            testResults.add(new TestResult().setOrder(2).setStep(2)
                    .setAction("Fill Login form data (Username/Password)")
                    .setExpected("Login form gets filled").setActual("Fail"));
            Assert.assertTrue(false);
        }
    }

    @Test(description = "Perform Login", priority = 3)
    public void DoLogin() throws Exception {
        try {
            // Click on the Login button
            WebElement login = driver.findElement(By.className("loginbtn"));
            login.click();

            Thread.sleep(1000);
            // Assert the user login by checking the Online user
            WebElement onlineuser = driver.findElement(By.className("RTL"));
            Assert.assertEquals("Hi, John Smith", onlineuser.getText());
            testResults.add(new TestResult().setOrder(3).setStep(3)
                    .setAction("Click Login and verify welcome message")
                    .setExpected("Login success").setActual("Pass"));
        } catch (AssertionError e) {
            testResults.add(new TestResult().setOrder(3).setStep(3)
                    .setAction("Click Login and verify welcome message")
                    .setExpected("Login success").setActual("Fail"));
            Assert.assertTrue(false);
        }
    }

    @BeforeClass(alwaysRun = true)
    public void suiteSetUp() {

        // create a new work book
        workbook = new HSSFWorkbook();
        // create a new work sheet
        sheet = workbook.createSheet("TestNG Result Summary");
        TestNGResults = new LinkedHashMap<>();
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

    @AfterClass
    public void suiteTearDown() {
        SaveExcelUtils.saveResult(testResults, "result/autotest.xls", "login");
        // close the browser
        driver.close();
        driver.quit();
    }
}