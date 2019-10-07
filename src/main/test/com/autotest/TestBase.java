package com.autotest;

import com.autotest.data.TestResult;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.openqa.selenium.WebDriver;

import java.util.List;
import java.util.Map;

public abstract class TestBase {
    protected WebDriver driver;
    // Declare An Excel Work Book
    protected HSSFWorkbook workbook;
    // Declare An Excel Work Sheet
    protected HSSFSheet sheet;
    // Declare A Map Object To Hold TestNG Results
    protected Map<String, Object[]> TestNGResults;
    protected List<TestResult> testResults;
}
