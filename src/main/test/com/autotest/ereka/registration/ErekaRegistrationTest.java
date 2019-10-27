package com.autotest.ereka.registration;

import com.autotest.SeleniumDriver;
import com.autotest.data.Registration;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;

import static com.autotest.constant.LocatorConstant.*;

public class ErekaRegistrationTest extends SeleniumDriver {
    private Registration registration;

    public ErekaRegistrationTest(Registration registration) {
        this.registration = registration;
    }

    @Test(description = "Mở trang web và click vào đăng ký", priority = 1)
    public void launchSignUpHome() {
        try {
            driver.get("https://ereka.vn");
            driver.manage().window().maximize();
            Thread.sleep(1000);

            // click vào label đăng nhâp
            WebElement loginLabel = driver.findElement(REGISTRATION_NAV_BUTTON);
            loginLabel.click();
            Thread.sleep(1000);
        } catch (Exception e) {
            Assert.fail();
        }
    }

    @Test(description = "Điền giá trị vào các field", priority = 2)
    public void fillData() {
        try {
            driver.findElement(REGISTRATION_USERNAME).sendKeys(registration.getUsername());
            driver.findElement(REGISTRATION_PASSWORD).sendKeys(registration.getPassword());
            driver.findElement(REGISTRATION_NAME).sendKeys(registration.getName());
            Thread.sleep(1000);
        } catch (Exception e) {
            Assert.fail();
        }
    }

    @Test(description = "Thực hiện so sánh và ghi kết quả", priority = 3)
    public void doSignUp() throws Exception {
        try {
            // Click on the Login button
            WebElement login = driver.findElement(REGISTRATION_BUTTON);
            login.click();
            Thread.sleep(1000);
            Assert.assertEquals(registration.getMessage().trim(), getMessageError().trim());
            registration.setActual("PASS");
        } catch (AssertionError e) {
            registration.setActual("FAIL");
            Assert.fail();
        }
    }

    //Lấy text thông báo hệ thống
    private String getMessageError() {
        WebDriverWait wait = new WebDriverWait(driver, 10);
        wait.until(ExpectedConditions.visibilityOfElementLocated(REGISTRATION_MESSAGE_ERROR));
        return driver.findElement(REGISTRATION_MESSAGE_ERROR).getText();
    }
}
