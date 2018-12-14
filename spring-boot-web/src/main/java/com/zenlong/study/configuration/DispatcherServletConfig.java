package com.zenlong.study.configuration;

import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import java.util.ArrayList;
import java.util.List;


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
     * 配置静态访问资源
     * 访问时不需要前缀
     * 默认:优先级
     * /**映射到
     * classpath:/META-INF/resources
     * classpath:/resources
     * classpath:/static
     * classpath:/public
     *
     * @param registry
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("swagger-ui.html").addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
        //服务器部署的tomcat下html路径
        registry.addResourceHandler("/js/**").addResourceLocations("html/js/");
        registry.addResourceHandler("/css/**").addResourceLocations("html/css/");
        registry.addResourceHandler("/img/**").addResourceLocations("html/img/");
        registry.addResourceHandler("/resources/**");
        super.addResourceHandlers(registry);
    }

    /**
     * 视图配置
     *
     * @param registry
     */
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        //路由配置
        registry.addViewController("druid").setViewName("druid/index.html");
        //重定向
        registry.addRedirectViewController("doc", "swagger-ui.html");
        registry.setOrder(Ordered.HIGHEST_PRECEDENCE);
        super.addViewControllers(registry);
    }

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

    /**
     * 扩展消息转换器，增加fastJson
     *
     * @param converters
     */
    @Override
    public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        converters.add(getFastJsonHttpMessageConverter());
        super.configureMessageConverters(converters);
    }

    @Bean
    public InternalResourceViewResolver setupViewResolver() {
        InternalResourceViewResolver resolver = new InternalResourceViewResolver();
        //设置视图路径的前缀
        resolver.setPrefix("/WEB-INF/view/");
        //设置视图路径的后缀
        resolver.setSuffix(".jsp");
        return resolver;
    }

    /**
     * fastJson消息转换器(处理@RequestBody/@ResponseBody注解的入参或返回值)
     *
     * @return
     */
    @Bean
    public HttpMessageConverter getFastJsonHttpMessageConverter() {
        FastJsonHttpMessageConverter messageConverter = new FastJsonHttpMessageConverter();
        //设置支持的Content-Type
        List<MediaType> mediaTypes = new ArrayList<>();
        mediaTypes.add(MediaType.APPLICATION_JSON_UTF8);
        mediaTypes.add(MediaType.APPLICATION_FORM_URLENCODED);
        mediaTypes.add(MediaType.MULTIPART_FORM_DATA);
        mediaTypes.add(MediaType.APPLICATION_OCTET_STREAM);
        mediaTypes.add(MediaType.TEXT_HTML);
        messageConverter.setSupportedMediaTypes(mediaTypes);
        //不忽略对象属性中的null值
        FastJsonConfig fastJsonConfig = new FastJsonConfig();
        fastJsonConfig.setSerializerFeatures(
                SerializerFeature.PrettyFormat,
                //输出所有为null的字段
                SerializerFeature.WriteMapNullValue,
                //包装类字段如果为null,输出为0,而非null
                //SerializerFeature.WriteNullNumberAsZero,
                //字符类型字段如果为null,输出为"",而非null
                //SerializerFeature.WriteNullStringAsEmpty,
                //List字段如果为null,输出为[],而非null
                SerializerFeature.WriteNullListAsEmpty);
        messageConverter.setFastJsonConfig(fastJsonConfig);
        return messageConverter;
    }
}

