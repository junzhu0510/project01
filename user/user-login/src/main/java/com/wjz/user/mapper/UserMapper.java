package com.wjz.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wjz.user.entity.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper extends BaseMapper<User> {
    /**
     * 根据用户名查找用户
     */
    @Select("SELECT * FROM user WHERE username = #{username}")
    User findByUserName(@Param("username") String username);

    /**
     * 注册新用户
     */
    @Insert("INSERT INTO user (username, password, create_time) VALUES (#{username}, #{password}, NOW())")
    int registerUser(@Param("username") String username, @Param("password") String password);
}
