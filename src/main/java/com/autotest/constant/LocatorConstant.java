package com.autotest.constant;

import org.openqa.selenium.By;
// https://www.guru99.com/xpath-selenium.html
// Xpath=//tagname[@attribute='value']
public class LocatorConstant {
    public final static By REGISTRATION_NAME = By.id("name");
    public final static By REGISTRATION_USERNAME = By.id("username");
    public final static By REGISTRATION_PASSWORD = By.id("password");
    public final static By REGISTRATION_MESSAGE_ERROR = By.xpath("//p[@class='message-content']");
    public final static By REGISTRATION_BUTTON = By.xpath("//button[@class='button main-type big register-button']");
    public final static By REGISTRATION_NAV_BUTTON = By.xpath("//button[@class='button gtm-login-nav-btn special-mono-type big register-button']");
}
