package com.spring.shiro.redis.springshiroredis.service;

import com.spring.shiro.redis.springshiroredis.model.User;

/**
 * @Classname UserService
 * @Description TODO
 * @Date 2019/7/13 9:03
 * @Created by 颜小能
 */
public interface UserService {
    User findOneByUserName(String userName);
}
