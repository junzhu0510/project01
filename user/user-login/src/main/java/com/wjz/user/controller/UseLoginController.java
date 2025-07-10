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
            @RequestParam @Pattern(regexp = "^\\S{5,16}$") String password,
            @RequestParam @Pattern(regexp = "^1[3-9]\\d{9}$") String phone) {
        logger.debug("注册请求: username={}, phone={}", username, phone);
        // 查询用户是否存在
        User u = userServiceLogin.findByUserName(username);
        if (u != null) {
            logger.debug("用户名已被占用: {}", username);
            return User.error("用户名已被占用");
        }
        // 查询手机号是否已被注册
        User phoneUser = userServiceLogin.findByPhone(phone);
        if (phoneUser != null) {
            logger.debug("手机号已被注册: {}", phone);
            return User.error("手机号已被注册");
        }
        User result = userServiceLogin.register(username, password, phone);
        logger.debug("注册结果: {}", result);
        return result;
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
        String phone = request.get("phone");
        
        // 验证参数
        if (username == null || password == null || phone == null || 
            username.trim().length() < 5 || username.trim().length() > 16 ||
            password.trim().length() < 5 || password.trim().length() > 16 ||
            !phone.matches("^1[3-9]\\d{9}$")) {
            return User.error("用户名和密码必须是5-16位字符，手机号必须是11位有效手机号");
        }
        
        logger.debug("JSON注册请求: username={}, phone={}", username, phone);
        // 查询用户是否存在
        User u = userServiceLogin.findByUserName(username);
        if (u != null) {
            logger.debug("用户名已被占用: {}", username);
            return User.error("用户名已被占用");
        }
        // 查询手机号是否已被注册
        User phoneUser = userServiceLogin.findByPhone(phone);
        if (phoneUser != null) {
            logger.debug("手机号已被注册: {}", phone);
            return User.error("手机号已被注册");
        }
        User result = userServiceLogin.register(username, password, phone);
        logger.debug("注册结果: {}", result);
        return result;
    }

    // 支持JSON格式的登录接口
    @PostMapping("/login-json")
    public User loginJson(@RequestBody Map<String, String> request) {
        String username = request.get("username");
        String password = request.get("password");
        
        // 验证参数
        if (username == null || password == null || 
            username.trim().length() < 5 || username.trim().length() > 16 ||
            password.trim().length() < 5 || password.trim().length() > 16) {
            return User.error("用户名和密码必须是5-16位字符");
        }
        
        logger.debug("JSON登录请求: username={}", username);
        User result = userServiceLogin.login(username, password);
        logger.debug("登录结果: {}", result);
        return result;
    }
    
    // 忘记密码 - 验证用户名和手机号后四位
    @PostMapping("/verify-reset-info")
    public User verifyResetInfo(@RequestBody Map<String, String> request) {
        String username = request.get("username");
        String phoneSuffix = request.get("phoneSuffix");
        
        // 验证参数
        if (username == null || phoneSuffix == null || 
            username.trim().length() < 5 || username.trim().length() > 16 ||
            phoneSuffix.length() != 4 || !phoneSuffix.matches("\\d{4}")) {
            return User.error("用户名必须是5-16位字符，手机号后四位必须是4位数字");
        }
        
        logger.debug("验证重置密码信息: username={}, phoneSuffix={}", username, phoneSuffix);
        User user = userServiceLogin.findByUsernameAndPhoneSuffix(username, phoneSuffix);
        if (user == null) {
            return User.error("用户名或手机号后四位不正确");
        }
        
        return User.success("验证成功");
    }
    
    // 重置密码
    @PostMapping("/reset-password")
    public User resetPassword(@RequestBody Map<String, String> request) {
        String username = request.get("username");
        String phoneSuffix = request.get("phoneSuffix");
        String newPassword = request.get("newPassword");
        
        // 验证参数
        if (username == null || phoneSuffix == null || newPassword == null ||
            username.trim().length() < 5 || username.trim().length() > 16 ||
            phoneSuffix.length() != 4 || !phoneSuffix.matches("\\d{4}") ||
            newPassword.trim().length() < 5 || newPassword.trim().length() > 16) {
            return User.error("参数格式不正确");
        }
        
        logger.debug("重置密码请求: username={}, phoneSuffix={}", username, phoneSuffix);
        User result = userServiceLogin.resetPassword(username, phoneSuffix, newPassword);
        logger.debug("重置密码结果: {}", result);
        return result;
    }
}
