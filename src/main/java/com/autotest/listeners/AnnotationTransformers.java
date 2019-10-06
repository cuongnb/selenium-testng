package com.autotest.listeners;

import org.testng.IAnnotationTransformer;
import org.testng.annotations.ITestAnnotation;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

public class AnnotationTransformers implements IAnnotationTransformer {

    public boolean isTestRunning(ITestAnnotation ins) {
        if (ins.getAlwaysRun()) {
            return true;
        }
        return false;
    }

    public void transform(ITestAnnotation annotation, Class testClass,
                          Constructor testConstructor, Method testMethod) {
        if (isTestRunning(annotation)) {
            annotation.setEnabled(false);
        }
    }
}