package com.autotest;

import com.autotest.data.User;
import com.autotest.utils.ExcelUtil;

import java.util.List;


public class Main {
    public static void main(String[] args) {
        List<User> users = ExcelUtil.readExcel(User.class, "data/api-data.xls", "User");
        System.out.println(users);
        for (User user : users) {
            test(user);
        }
    }

    private static void test(User user) {
        System.out.println(user.getId() + "\t" + user.getUsername());
    }

}
