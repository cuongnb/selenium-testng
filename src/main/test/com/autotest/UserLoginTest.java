package com.autotest;

import com.autotest.data.TestResult;
import com.autotest.data.User;
import com.autotest.utils.ReadExcelUtil;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.util.List;

public class UserLoginTest extends TestBase {
    private User user;

    @BeforeTest
    public void loadUser() {
        List<User> users = ReadExcelUtil.readExcel(User.class, "result/data.xls", "user");
        user = users.get(0);
    }

    @Test(description = "Opens the TestNG Demo Website for Login Test", priority = 1)
    public void LaunchWebsite() throws Exception {
        try {
            driver.get("http://phptravels.net/login");
            driver.manage().window().maximize();
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
            username.sendKeys(user.getUsername());

            // Get the password element
            WebElement password = driver.findElement(By.name("password"));
            password.sendKeys(user.getPassword());

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
            Assert.assertEquals(user.getName(), onlineuser.getText());
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
}
