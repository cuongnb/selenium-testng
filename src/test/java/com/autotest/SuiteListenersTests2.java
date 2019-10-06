package com.autotest;

import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

public class SuiteListenersTests2 {

    @BeforeSuite
    public void test1() {
        System.out.println("BeforeSuite method in Suite2");
    }

    @Test
    public void test2() {
        System.out.println("Main Test method 2");
    }

    @AfterSuite
    public void test3() {
        System.out.println("AfterSuite method in Suite2");
    }

}