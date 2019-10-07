package com.autotest.data;

import com.autotest.config.annotation.ExcelField;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class TestResult {
    private Integer order;
    @ExcelField(name = "Test Step No.", position = 0)
    private Integer step;
    @ExcelField(name = "Action", position = 1)
    private String action;
    @ExcelField(name = "Expected Output", position = 2)
    private String expected;
    @ExcelField(name = "Actual Output", position = 3)
    private String actual;
}