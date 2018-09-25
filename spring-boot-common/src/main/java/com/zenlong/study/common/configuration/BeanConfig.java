package com.zenlong.study.common.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @Project spring-boot-all
 * @Package com.zenlin.mysql.web.configuration
 * @ClassName BeanConfig
 * @Author ZENLIN
 * @Date 2018-08-28 0:12
 * @Description TODO
 * @Version
 * @Modified By
 */
@Configuration
public class BeanConfig {
    /**
     * 任务线程池
     * 无线程可用的处理策略：
     * AbortPolicy(默认) 抛出RejectedExecutionException
     * CallerRunsPolicy 调用者的线程会执行该任务，如果执行器已关闭，则丢弃
     * DiscardPolicy 不能执行的任务将被丢弃
     * DiscardOldestPolicy 如果执行任务尚未关闭，则位于工作队列头部的任务将被删除，然后重试执行程序（如果再次失败，则重复此过程）
     *
     * @return
     */
    @Bean(name = "taskExecutor")
    public ThreadPoolTaskExecutor getPoolTaskExecutor() {
        ThreadPoolTaskExecutor poolTaskExecutor = new ThreadPoolTaskExecutor();
        //空闲线程的存活时间
        poolTaskExecutor.setKeepAliveSeconds(30000);
        //核心线程数
        poolTaskExecutor.setCorePoolSize(5);
        //任务队列最大长度
        poolTaskExecutor.setQueueCapacity(200);
        //最大线程数
        poolTaskExecutor.setMaxPoolSize(1000);
        //拒绝任务的处理策略
        RejectedExecutionHandler reject = new ThreadPoolExecutor.CallerRunsPolicy();
        poolTaskExecutor.setRejectedExecutionHandler(reject);
        return poolTaskExecutor;
    }
    /**
     * 文件上传解析器
     *
     * @return
     */
    @Bean(name = "multipartResolver")
    public CommonsMultipartResolver getCommonsMultipartResolver() {
        CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver();
        multipartResolver.setMaxUploadSize(5 * 1024 * 1024);
        multipartResolver.setDefaultEncoding("UTF-8");
        return multipartResolver;
    }
}
