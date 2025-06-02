package com.wjz.user.entity;

import lombok.Data;
import org.springframework.data.relational.core.mapping.Column;
import java.time.LocalDateTime;

@Data
public class User {
    private Integer id;
    private String username;
    private String password;
    @Column("create_time")
    private LocalDateTime createTime;
}
