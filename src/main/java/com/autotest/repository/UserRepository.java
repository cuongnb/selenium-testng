package com.autotest.repository;

import com.autotest.data.User;

public class UserRepository {

    public User getUserByUsername(String username) {
        // select * from user where username = username
        return new User();
    }
}
