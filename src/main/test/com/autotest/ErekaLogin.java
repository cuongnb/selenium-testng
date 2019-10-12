package com.autotest;

import com.autotest.data.User;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.Test;

public class ErekaLogin extends TestBase {
    private User user;

    public ErekaLogin(User user) {
        this.user = user;
    }

    @Test()
    public void DoLogin() throws Exception {
        try {
            driver.get("https://ereka.vn");
            driver.manage().window().maximize();
            Thread.sleep(1000);

            // click vào label đăng nhâp
            WebElement loginLabel = driver.findElement(By.xpath("//button[@class='button text-type login-button']"));
            loginLabel.click();
            Thread.sleep(1000);

            // Get the username element
            WebElement username = driver.findElement(By.id("username"));
            username.sendKeys(user.getUsername());

            // Get the password element
            WebElement password = driver.findElement(By.id("password"));
            password.sendKeys(user.getPassword());

            Thread.sleep(1000);
            // Click on the Login button
            WebElement login = driver.findElement(By.xpath("//div//button[@class='button main-type big login-button']"));
            login.click();

            Thread.sleep(1000);

            WebElement element = driver.findElement(By.xpath("//img[@class='image avatar user-avatar']"));

            Assert.assertTrue(element.getSize().height > 0);
        } catch (AssertionError e) {
            Assert.assertTrue(false);
        }
    }
}
