package com.zenlong.study.configuration;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;


/**
 * @Project spring-boot-all
 * @Package com.zenlin.mysql.web.common
 * @ClassName ResponseCode
 * @Author ZENLIN
 * @Date 2018-08-27 21:43
 * @Description servlet上下文配置
 * @Version
 * @Modified By
 */
@Slf4j
@Configuration
public class DispatcherServletConfig extends WebMvcConfigurationSupport {
    /**
     * 解决跨域
     *
     * @param registry
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowCredentials(true)
                .allowedOrigins("*")
                .allowedHeaders("*")
                .allowedMethods("GET", "POST", "DELETE", "PUT", "PATCH", "OPTIONS")
                .maxAge(3600);
        super.addCorsMappings(registry);
    }
}

