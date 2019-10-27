package com.autotest.ereka.registration;

import com.autotest.data.Registration;
import com.autotest.utils.SaveExcelUtils;
import org.testng.annotations.AfterTest;
import org.testng.annotations.Factory;

import java.util.List;

import static com.autotest.utils.ReadExcelUtil.readExcel;

public class ErekaRegistrationFactoryTest {
    public List<Registration> users;

    public void loadData() {
        this.users = readExcel(Registration.class, "result/ereka.xls", "ereka_registration")
                .subList(0, 2);
    }

    @Factory
    public Object[] factoryMethod() {
        loadData();
        Object[] objects = new Object[users.size()];
        for (int i = 0; i < users.size(); i++) {
            objects[i] = new ErekaRegistrationTest(users.get(i));
        }
        return objects;
    }

    @AfterTest
    public void afterTest() {
        SaveExcelUtils.saveResult(users, "result/ereka.xls", "ereka_registration");
    }
}
