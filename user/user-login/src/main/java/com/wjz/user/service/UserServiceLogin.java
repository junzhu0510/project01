package com.wjz.user.service;

import com.wjz.user.entity.User;

public interface UserServiceLogin {
    User findByUserName(String username);
    User findByPhone(String phone);
    User findByUsernameAndPhoneSuffix(String username, String phoneSuffix);
    User register(String username, String password, String phone);
    User login(String username, String password);
    User resetPassword(String username, String phoneSuffix, String newPassword);
}
