package com.wjz.user.service;

import com.wjz.user.entity.User;

public interface UserServiceLogin {
    User findByUserName(String username);
    User register(String username, String password);
    User login(String username, String password);
}
