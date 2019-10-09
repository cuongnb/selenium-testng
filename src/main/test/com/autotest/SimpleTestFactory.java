package com.autotest;

import org.testng.annotations.Factory;

public class SimpleTestFactory {
    @Factory
    public Object[] factoryMethod() {
        return new Object[]{
                new SimpleTest("one"),
                new SimpleTest("two")
        };
    }
}
