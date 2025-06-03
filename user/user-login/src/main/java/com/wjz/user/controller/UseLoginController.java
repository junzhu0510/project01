package com.wjz.user.controller;

import com.wjz.user.entity.User;
import com.wjz.user.service.UserServiceLogin;
import jakarta.validation.constraints.Pattern;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/user")
@Validated
public class UseLoginController {

    private static final Logger logger = LoggerFactory.getLogger(UseLoginController.class);

    @Autowired
    private UserServiceLogin userServiceLogin;

    // 注册接口
    @PostMapping("/register")
    public User register(
            // 进行参数校验，确保用户名和密码是5-16位非空字符
            @RequestParam @Pattern(regexp = "^\\S{5,16}$") String username,
            @RequestParam @Pattern(regexp = "^\\S{5,16}$") String password) {
        logger.debug("注册请求: username={}", username);
        // 查询用户是否存在
        User u = userServiceLogin.findByUserName(username);
        if (u != null) {
            logger.debug("用户名已被占用: {}", username);
            return User.error("用户名已被占用");
        }
        User result = userServiceLogin.register(username, password);
        logger.debug("注册结果: {}", result);
        return result.success();
    }

    @PostMapping("/login")
    public User login(
            @RequestParam @Pattern(regexp = "^\\S{5,16}$") String username,
            @RequestParam @Pattern(regexp = "^\\S{5,16}$") String password) {
        logger.debug("登录请求: username={}", username);
        User result = userServiceLogin.login(username, password);
        logger.debug("登录结果: {}", result);
        return result;
    }

    // 支持JSON格式的注册接口
    @PostMapping("/register-json")
    public User registerJson(@RequestBody Map<String, String> request) {
        String username = request.get("username");
        String password = request.get("password");
        
        // 验证参数
        if (username == null || password == null || 
            !username.matches("^\\S{5,16}$") || !password.matches("^\\S{5,16}$")) {
            return User.error("用户名和密码必须是5-16位非空字符");
        }
        
        logger.debug("JSON注册请求: username={}", username);
        // 查询用户是否存在
        User u = userServiceLogin.findByUserName(username);
        if (u != null) {
            logger.debug("用户名已被占用: {}", username);
            return User.error("用户名已被占用");
        }
        User result = userServiceLogin.register(username, password);
        logger.debug("注册结果: {}", result);
        return result.success();
    }

    // 支持JSON格式的登录接口
    @PostMapping("/login-json")
    public User loginJson(@RequestBody Map<String, String> request) {
        String username = request.get("username");
        String password = request.get("password");
        
        // 验证参数
        if (username == null || password == null || 
            !username.matches("^\\S{5,16}$") || !password.matches("^\\S{5,16}$")) {
            return User.error("用户名和密码必须是5-16位非空字符");
        }
        
        logger.debug("JSON登录请求: username={}", username);
        User result = userServiceLogin.login(username, password);
        logger.debug("登录结果: {}", result);
        return result;
    }
}
