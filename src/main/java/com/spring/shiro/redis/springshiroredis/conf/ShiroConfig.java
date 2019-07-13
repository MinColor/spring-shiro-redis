package com.spring.shiro.redis.springshiroredis.conf;

import com.spring.shiro.redis.springshiroredis.redis.RedisSessionDAO;
import com.spring.shiro.redis.springshiroredis.shiro.UserShiroRealm;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;

/**
 * @Classname ShiroConfig
 * @Description TODO
 * @Date 2019/7/13 9:19
 * @Created by 颜小能
 */
@Configuration
public class ShiroConfig {

    //将自己的验证方式加入容器
    @Bean
    public UserShiroRealm userShiroRealm() {
        return new UserShiroRealm();
    }

    @Bean
    public SimpleCookie getSimpleCookie() {
        SimpleCookie simpleCookie = new SimpleCookie();
        simpleCookie.setName("SHRIOSESSIONID");
        return simpleCookie;
    }

    //配置shiro session 的一个管理器
    @Bean(name = "sessionManager")
    public DefaultWebSessionManager getDefaultWebSessionManager(RedisSessionDAO redisSessionDAO) {
        DefaultWebSessionManager sessionManager = new DefaultWebSessionManager();
        sessionManager.setSessionDAO(redisSessionDAO);
        sessionManager.setGlobalSessionTimeout(-1000);  //session有效期 默认值1800000 30分钟 1800000毫秒  -1000表示永久
        SimpleCookie simpleCookie = getSimpleCookie();
        simpleCookie.setHttpOnly(true);                 //设置js不可读取此Cookie
        simpleCookie.setMaxAge(3 * 365 * 24 * 60 * 60); //3年 cookie有效期
        sessionManager.setSessionIdCookie(simpleCookie);
        return sessionManager;
    }
    /**
     *  开启shiro aop注解支持.
     *  使用代理方式;所以需要开启代码支持;
     * @param securityManager
     * @return
     */
    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager){
        AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
        authorizationAttributeSourceAdvisor.setSecurityManager(securityManager);
        return authorizationAttributeSourceAdvisor;
    }

    //配置核心安全事务管理器
    @Bean(name="securityManager")
    public SecurityManager securityManager(@Qualifier("userShiroRealm") UserShiroRealm userShiroRealm,
                                           @Qualifier("sessionManager") DefaultWebSessionManager sessionManager) {
        DefaultWebSecurityManager manager = new DefaultWebSecurityManager();
        manager.setRealm(userShiroRealm);
        manager.setSessionManager(sessionManager);
        return manager;
    }

    //权限管理，配置主要是Realm的管理认证
    @Bean
    public SecurityManager securityManager() {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setRealm(userShiroRealm());
        return securityManager;
    }




    //Filter工厂，设置对应的过滤条件和跳转条件
    @Bean
    public ShiroFilterFactoryBean shiroFilterFactoryBean() {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        shiroFilterFactoryBean.setSecurityManager(securityManager());
        HashMap<String, String> map = new HashMap<>();
        //登出
        //map.put("/logout", "logout");
        //认证 /###/@@@/**

        map.put("/login","anon");
        map.put("/test2","anon");
        map.put("/logout","anon");
        map.put("/**", "authc");
        map.put("**", "authc");

//        map.put("/**/**/test2","anon");
//        map.put("/**/**/login","anon");
//        map.put("/**/**/logout","anon");
//        map.put("/**/**/**", "authc");

        //登录认证不通过跳转
        shiroFilterFactoryBean.setLoginUrl("/loginUnAuth");
        //首页
        //shiroFilterFactoryBean.setSuccessUrl("/index");
        //权限认证不通过跳转
        //shiroFilterFactoryBean.setUnauthorizedUrl("/authorUnAuth");
        shiroFilterFactoryBean.setFilterChainDefinitionMap(map);
        return shiroFilterFactoryBean;
    }

    //加入注解的使用，不加入这个注解不生效
    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor() {
        AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
        authorizationAttributeSourceAdvisor.setSecurityManager(securityManager());
        return authorizationAttributeSourceAdvisor;
    }
}
