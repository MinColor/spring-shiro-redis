package com.spring.shiro.redis.springshiroredis.model;

import lombok.Data;

/**
 * @Classname User
 * @Description TODO
 * @Date 2019/7/13 9:01
 * @Created by 颜小能
 */
@Data
public class User {
    private Long id;
    private String userName;
    private String password;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
