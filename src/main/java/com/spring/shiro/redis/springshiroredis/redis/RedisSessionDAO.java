package com.spring.shiro.redis.springshiroredis.redis;

import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.eis.EnterpriseCacheSessionDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.Serializable;

/**
 * @Classname RedisSessionDAO
 * @Description TODO
 * @Date 2019/7/13 9:14
 * @Created by 颜小能
 */
@Component
public class RedisSessionDAO extends EnterpriseCacheSessionDAO {
    @Autowired
    private RedisClusterCache redisClusterCache;
    /**
     * 创建session，保存到redis数据库
     *
     * @param session
     * @return
     */
    @Override
    protected Serializable doCreate(Session session) {
        Serializable sessionId = super.doCreate(session);
        try {
            redisClusterCache.putCache(sessionId.toString(), session);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sessionId;
    }
    /**
     * 获取session
     *
     * @param sessionId
     * @return
     */
    @Override
    protected Session doReadSession(Serializable sessionId) {
        // 先从缓存中获取session，如果没有再去数据库中获取
        Session session = super.doReadSession(sessionId);
        if (session == null) {
            try {
                session = redisClusterCache.getCache(sessionId.toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return session;
    }
    /**
     * 更新session的最后一次访问时间
     *
     * @param session
     */
    @Override
    protected void doUpdate(Session session) {
        super.doUpdate(session);
        try {
            redisClusterCache.putCache(session.getId().toString(), session);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * 删除session
     *
     * @param session
     */
    @Override
    protected void doDelete(Session session) {
        super.doDelete(session);
        try {
            redisClusterCache.delCache(session.getId().toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
