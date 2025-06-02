package com.wjz.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wjz.user.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserMapper extends BaseMapper<User> {
    /**
     * 根据用户名查找用户
     */
    User findByUserName(@Param("username") String username);

    /**
     * 注册新用户
     */
    int registerUser(@Param("username") String username, @Param("password") String password);
}
