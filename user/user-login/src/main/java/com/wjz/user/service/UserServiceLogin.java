package com.wjz.user.service;

import com.wjz.user.entity.Result;
import com.wjz.user.entity.User;

public interface UserServiceLogin {
    User findByUserName(String username);
    Result register(String username, String password);
    Result login(String username, String password);
}
