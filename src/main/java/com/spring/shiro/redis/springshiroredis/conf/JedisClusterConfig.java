package com.spring.shiro.redis.springshiroredis.conf;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;

import java.util.HashSet;
import java.util.Set;

/**
 * @Classname JedisClusterConfig
 * @Description TODO
 * @Date 2019/7/13 9:09
 * @Created by 颜小能
 */
@Configuration
@ConditionalOnClass({ JedisCluster.class })
public class JedisClusterConfig {
    @Value("${spring.redis.cache.clusterNodes}")
    private String clusterNodes;
    @Value("${spring.redis.cache.password}")
    private String password;
    @Value("${spring.redis.cache.commandTimeout}")
    private Integer commandTimeout;

    @Bean
    public JedisCluster getJedisCluster() {
        String[] serverArray = clusterNodes.split(",");
        Set<HostAndPort> nodes = new HashSet<>();
        for (String ipPort : serverArray) {
            String[] ipPortPair = ipPort.split(":");
            nodes.add(new HostAndPort(ipPortPair[0].trim(), Integer.valueOf(ipPortPair[1].trim())));
        }
        return new JedisCluster(nodes, commandTimeout, commandTimeout, 2, new GenericObjectPoolConfig());
    }
}
