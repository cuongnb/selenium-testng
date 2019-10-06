package com.autotest;


import com.autotest.listeners.SuiteListeners;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

@Listeners(SuiteListeners.class)
public class SuiteListenersTests1 {

    @BeforeSuite
    public void test1() {
        System.out.println("BeforeSuite method in Suite1");
    }


    @Test
    public void test2() {
        System.out.println("Main Test method 1");
    }

    @AfterSuite
    public void test3() {
        System.out.println("AfterSuite method in Suite1");
    }

}