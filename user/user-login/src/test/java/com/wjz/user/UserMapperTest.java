package com.wjz.user;

import com.wjz.user.entity.User;
import com.wjz.user.mapper.UserMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class UserMapperTest {

    @Autowired
    private UserMapper userMapper;

    @Test
    public void testRegisterUser() {
        String testUsername = "testUser" + System.currentTimeMillis();
        String testPassword = "testPassword";

        // 执行注册操作
        int result = userMapper.registerUser(testUsername, testPassword);

        // 验证注册是否成功
        assertEquals(1, result, "用户注册应该成功");

        // 验证用户是否能够被查询到
        User foundUser = userMapper.findByUserName(testUsername);
        assertNotNull(foundUser, "应该能够查询到注册的用户");
        assertEquals(testUsername, foundUser.getUsername(), "用户名应该匹配");
    }
}
