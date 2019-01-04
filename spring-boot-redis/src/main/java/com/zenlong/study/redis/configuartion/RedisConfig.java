package com.zenlong.study.redis.configuartion;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * @Description Redis配置
 * @Project my-spring-integration
 * @Package com.zenlong.study.redis.configuartion
 * @ClassName RedisConfig
 * @Author 曾灵
 * @QQ|Email 3195690389|17363645521@163.com
 * @Date 2018-09-15 12:25
 * @Version 1.0
 * @Modified By
 */
@Configuration
@PropertySource("classpath:redis.properties")
public class RedisConfig {
    @Value("${spring.redis.host}")
    private String host;
    @Value("${spring.redis.port}")
    private String port;
    @Value("${spring.redis.database}")
    private String database;
    private int timeout = 3000;
    private String password;
    private int poolMaxTotal = 1000;
    private int poolMaxIdle = 500;
    private int poolMaxWait = 500;
    private int connectionPoolSize = 64;
    private int connectionMinimumIdleSize = 10;
    private int slaveConnectionPoolSize = 250;
    private int masterConnectionPoolSize = 250;
    private String[] sentinelAddresses;
}
