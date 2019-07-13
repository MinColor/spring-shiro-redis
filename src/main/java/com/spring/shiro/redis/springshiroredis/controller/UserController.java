package com.spring.shiro.redis.springshiroredis.controller;

import com.spring.shiro.redis.springshiroredis.service.UserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.Serializable;

/**
 * @Classname UserController
 * @Description TODO
 * @Date 2019/7/13 9:07
 * @Created by 颜小能
 */
@RestController
public class UserController {
    @Autowired
    private UserService userService;

    /**
     * 登录
     * @param
     * @return
     */
    @GetMapping("/login")
    public String login(String userName, String password) {
        System.out.println("登录" + userName);

        Subject subject = SecurityUtils.getSubject();

        UsernamePasswordToken token = new UsernamePasswordToken(userName, password);
        subject.login(token);

        Session session = subject.getSession();
        Serializable sessionId = session.getId();
        System.out.println("登录成功 -> " + sessionId);

        return userName + "[" + sessionId + "]";
    }

    @RequestMapping("/test1")
    @RequiresPermissions("user:test1")//权限管理;
    @ResponseBody
    public String test1(){
        return "200-test1";
    }

    @RequestMapping("/test2")
    //@RequiresPermissions("user:test2")//权限管理;
    @ResponseBody
    public String test2(){
        return "200-test2";
    }

    @GetMapping("/logout")
    public String logout() {
        SecurityUtils.getSubject().logout();
        return "退出登录成功";
    }

    /**
     * 获取当前登录用户
     * @return
     */
    @GetMapping("/findUser")
    public String findUser() {
        Subject subject = SecurityUtils.getSubject();
        PrincipalCollection collection = subject.getPrincipals();
        if (null != collection && !collection.isEmpty()) {
            String userName = (String) collection.iterator().next();
            System.out.println("获取当前登录用户" + userName);
            return userService.findOneByUserName(userName).toString();
        }
        return "{\n" +
                "    \"codeEnum\": \"OVERTIME\",\n" +
                "    \"code\": 0,\n" +
                "    \"data\": null,\n" +
                "    \"msg\": \"未登陆/登陆超时\",\n" +
                "    \"success\": false\n" +
                "}";
    }
}
