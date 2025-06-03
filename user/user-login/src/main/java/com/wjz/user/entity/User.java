package com.wjz.user.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.springframework.data.relational.core.mapping.Column;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private Integer id;
    private String username;
    private String password;
    @Column("create_time")
    private LocalDateTime createTime;
    private Integer code;
    private String message;
    private Object data;

    public User(Integer id, String username, String password, LocalDateTime createTime) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.createTime = createTime;
    }

    public static User success(Object data) {
        User response = new User();
        response.setCode(0);
        response.setMessage("操作成功");
        response.setData(data);
        return response;
    }

    public static User success() {
        return success(null);
    }

    public static User error(String message) {
        User response = new User();
        response.setCode(1);
        response.setMessage(message);
        return response;
    }
}
