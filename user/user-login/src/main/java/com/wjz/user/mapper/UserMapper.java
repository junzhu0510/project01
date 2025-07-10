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
     * 根据手机号查找用户
     */
    @Select("SELECT * FROM user WHERE phone = #{phone}")
    User findByPhone(@Param("phone") String phone);
    
    /**
     * 根据用户名和手机号后四位查找用户
     */
    @Select("SELECT * FROM user WHERE username = #{username} AND RIGHT(phone, 4) = #{phoneSuffix}")
    User findByUsernameAndPhoneSuffix(@Param("username") String username, @Param("phoneSuffix") String phoneSuffix);

    /**
     * 注册新用户
     */
    @Insert("INSERT INTO user (username, password, phone, create_time) VALUES (#{username}, #{password}, #{phone}, NOW())")
    int registerUser(@Param("username") String username, @Param("password") String password, @Param("phone") String phone);
    
    /**
     * 更新用户密码
     */
    @Insert("UPDATE user SET password = #{password} WHERE id = #{userId}")
    int updatePassword(@Param("userId") Integer userId, @Param("password") String password);
}
