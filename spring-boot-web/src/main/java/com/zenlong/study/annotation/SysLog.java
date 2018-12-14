package com.zenlong.study.annotation;

import java.lang.annotation.*;

/**
 * 描述:自定义日志注解
 * 项目名:my-spring-integration
 *
 * @Author:ZENLIN
 * @Created 2018/12/14  9:43.
 */

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SysLog {
    String value() default "";
}
