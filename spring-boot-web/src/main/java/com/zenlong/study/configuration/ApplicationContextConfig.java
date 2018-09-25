package com.zenlong.study.configuration;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.CharacterEncodingFilter;

import javax.servlet.Filter;


/**
 * @Project spring-boot-all
 * @Package com.zenlin.mysql.web.common
 * @ClassName ResponseCode
 * @Author ZENLIN
 * @Date 2018-08-27 21:43
 * @Description 程序上下文配置
 * @Version
 * @Modified By
 */
@Configuration
public class ApplicationContextConfig {
    /**
     * encoding编码问题(springBoot默认已经配置好,也可以在application.yml里配置)
     *
     * @return
     */
    @Bean
    @ConditionalOnMissingBean(CharacterEncodingFilter.class)
    public Filter characterEncodingFilter() {
        CharacterEncodingFilter characterEncodingFilter = new CharacterEncodingFilter();
        characterEncodingFilter.setEncoding("UTF-8");
        characterEncodingFilter.setForceEncoding(true);
        return characterEncodingFilter;
    }
}

