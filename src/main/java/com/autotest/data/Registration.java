package com.autotest.data;

import com.autotest.config.annotation.ExcelField;
import lombok.Data; // thư viện lombok auto sinh ra get/set

@Data
public class Registration extends ExcelRow {
    private String purpose;
    private String step;
    private String expected;
    private String message;
    private String username;
    private String password;
    private String name;
    @ExcelField(position = 7)
    private String actual;
    @ExcelField(position = 8)
    private String realMessage;
}
