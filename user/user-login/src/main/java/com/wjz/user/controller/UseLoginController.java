package com.wjz.user.controller;

import com.wjz.user.entity.Result;
import com.wjz.user.entity.User;
import com.wjz.user.service.UserServiceLogin;
import jakarta.validation.constraints.Pattern;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@Validated
public class UseLoginController {

    @Autowired
    private UserServiceLogin userServiceLogin;

    @PostMapping("/register")
    public Result register(
            @RequestParam @Pattern(regexp = "^\\S{5,16}$") String username,
            @RequestParam @Pattern(regexp = "^\\S{5,16}$") String password) {
        // 查询用户是否存在
        User u = userServiceLogin.findByUserName(username);
        if (u != null) {
            return Result.error("用户名已被占用");
        }
        return userServiceLogin.register(username, password);
    }

    @PostMapping("/login")
    public Result login(
            @RequestParam @Pattern(regexp = "^\\S{5,16}$") String username,
            @RequestParam @Pattern(regexp = "^\\S{5,16}$") String password) {
        return userServiceLogin.login(username, password);
    }
}
