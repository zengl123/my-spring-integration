package com.zenlong.study.es.configuration;

import lombok.Data;
import org.springframework.context.annotation.Configuration;

/**
 * @Description Elasticsearch连接配置类
 * @Project my-spring-integration
 * @Package com.zenlong.study.es.configuration
 * @ClassName ElasticsearchConfig
 * @Author 曾灵
 * @QQ|Email 3195690389|17363645521@163.com
 * @Date 2018-09-14 7:56
 * @Version 1.0
 * @Modified By
 */
@Configuration
@Data
public class ElasticsearchConfig {
    public static final String PREFIX = "spring.es";
    private String[] hosts;
    private int port = 9200;
    private String schema = "http";
    private int connectTimeOut;
    private int socketTimeOut;
    private int connectionRequestTimeOut;
    private int maxConnectNum;
    private int maxConnectPerRoute;
}
