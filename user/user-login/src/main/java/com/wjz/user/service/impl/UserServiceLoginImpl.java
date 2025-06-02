package com.wjz.user.service.impl;

import com.wjz.user.entity.Result;
import com.wjz.user.entity.User;
import com.wjz.user.mapper.UserMapper;
import com.wjz.user.service.UserServiceLogin;
import com.wjz.user.utils.JwtUtil;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Mapper
@Service
public class UserServiceLoginImpl implements UserServiceLogin {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private JwtUtil jwtUtil;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    public User findByUserName(String username) {
        return userMapper.findByUserName(username);
    }

    @Override
    public Result register(String username, String password) {
        // 对密码进行加密
        String encodedPassword = passwordEncoder.encode(password);
        
        // 使用mapper层进行数据库操作
        int rows = userMapper.registerUser(username, encodedPassword);
        
        if (rows > 0) {
            return Result.success("注册成功");
        }
        return Result.error("注册失败");
    }

    @Override
    public Result login(String username, String password) {
        User user = userMapper.findByUserName(username);
        if (user == null) {
            return Result.error("用户不存在");
        }

        // 验证密码
        if (!passwordEncoder.matches(password, user.getPassword())) {
            return Result.error("密码错误");
        }

        // 生成 token
        String token = jwtUtil.generateToken(username);
        return Result.success(token);
    }
}
