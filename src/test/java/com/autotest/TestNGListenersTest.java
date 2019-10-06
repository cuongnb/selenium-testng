package com.autotest;


import com.autotest.listeners.ListenersBlog;
import org.testng.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.SkipException;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

@Listeners(ListenersBlog.class)
public class TestNGListenersTest {


    @Test  //Passing Test
    public void sampleTest1() throws InterruptedException {
//        System.setProperty("webdriver.chrome.driver", "C:\\Users\\Lenovo-I7\\Desktop\\Selenium\\chromedriver.exe");

        WebDriver driver = new ChromeDriver();

        driver.get("https://www.apple.com/");
        driver.manage().window().maximize();

        driver.findElement(By.xpath("//*[@id=\'ac-globalnav\']/div/ul[2]/li[3]")).click();
        Thread.sleep(2000);

        driver.findElement(By.cssSelector("#chapternav &gt; div &gt; ul &gt; li.chapternav-item.chapternav-item-ipad-air &gt; a")).click();
        Thread.sleep(2000);

        driver.findElement(By.linkText("Why iPad")).click();
        Thread.sleep(2000);

        driver.quit();

    }

    @Test  //Failing Test
    public void sampleTest2() throws InterruptedException {
        System.out.println("Forcely Failed Test Method");
        Assert.assertTrue(false);
    }

    private int i = 0;

    @Test(successPercentage = 60, invocationCount = 5)  //Test Failing But Within Success Percentage
    public void sampleTest3() {
        i++;
        System.out.println("Test Failed But Within Success Percentage Test Method, invocation count: " + i);
        if (i == 1 || i == 2) {
            System.out.println("sampleTest3 Failed");
            Assert.assertEquals(i, 6);
        }
    }

    @Test  //Skipping Test
    public void sampleTest4() {
        throw new SkipException("Forcely skipping the sampleTest4");
    }

}