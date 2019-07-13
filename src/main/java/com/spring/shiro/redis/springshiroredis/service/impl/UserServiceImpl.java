package com.spring.shiro.redis.springshiroredis.service.impl;

import com.spring.shiro.redis.springshiroredis.model.User;
import com.spring.shiro.redis.springshiroredis.service.UserService;
import org.springframework.stereotype.Service;

/**
 * @Classname UserServiceImpl
 * @Description TODO
 * @Date 2019/7/13 9:03
 * @Created by 颜小能
 */
@Service
public class UserServiceImpl implements UserService {

    @Override
    public User findOneByUserName(String userName) {
        User user = new User();
        user.setId(1L);
        user.setUserName("yanxiaoneng");
        user.setPassword("123456");
        return user;
    }
}
