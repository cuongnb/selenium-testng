package com.autotest;

import com.autotest.data.User;
import com.autotest.utils.ReadExcelUtil;
import org.testng.annotations.Factory;

import java.util.List;

public class SimpleTestFactory {

    @Factory
    public Object[] factoryMethod() {
        List<User> users = ReadExcelUtil.readExcel(User.class, "result/data.xls", "user");
        Object[] objects = new Object[users.size()];
        for (int i = 0; i < users.size(); i++) {
            objects[i] = new ErekaLogin(users.get(i));
        }
        return objects;
    }
}
