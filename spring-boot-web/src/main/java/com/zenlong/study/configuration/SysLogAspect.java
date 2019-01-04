package com.zenlong.study.configuration;

import com.google.gson.Gson;
import com.zenlong.study.annotation.SysLog;
import com.zenlong.study.common.utils.IpUtil;
import com.zenlong.study.domain.bo.SysLogBo;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * 描述:
 * 项目名:my-spring-integration
 *
 * @Author:ZENLIN
 * @Created 2018/12/14  9:45.
 */
@Slf4j
@Aspect
@Component
public class SysLogAspect {
    /**
     * 这里我们使用注解的形式
     * 当然，我们也可以通过切点表达式直接指定需要拦截的package,需要拦截的class 以及 method
     * 切点表达式:   execution(...)
     */
    @Pointcut("@annotation(com.zenlong.study.annotation.SysLog)")
    public void logPointCut() {
    }

    /**
     * 环绕通知 @Around  ， 当然也可以使用 @Before (前置通知)  @After (后置通知)
     *
     * @param point
     * @return
     * @throws Throwable
     */
    @Around("logPointCut()")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        long beginTime = System.currentTimeMillis();
        Object result = point.proceed();
        long time = System.currentTimeMillis() - beginTime;
        try {
            saveLog(point, time);
        } catch (Exception e) {
            log.error("SysLogAspect...日志存储异常:{}", e.getMessage());
        }
        return result;
    }

    /**
     * 保存日志
     *
     * @param joinPoint
     * @param time
     */
    private void saveLog(ProceedingJoinPoint joinPoint, long time) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        SysLogBo sysLogBo = new SysLogBo();
        sysLogBo.setExecutionTime(time);
        sysLogBo.setCreateTime(LocalDateTime.now().withNano(0).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        SysLog sysLog = method.getAnnotation(SysLog.class);
        if (sysLog != null) {
            //注解上的描述
            sysLogBo.setRemark(sysLog.value());
        }
        // 接收到请求，记录请求内容
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        StringBuffer requestURL = request.getRequestURL();
        sysLogBo.setRequestUrl(requestURL.toString());
        sysLogBo.setIpAddress(IpUtil.getIpAddress(request));
        //请求的 类名、方法名
        String className = joinPoint.getTarget().getClass().getName();
        String methodName = signature.getName();
        sysLogBo.setClassName(className);
        sysLogBo.setMethodName(methodName);
        //请求的参数
        Object[] args = joinPoint.getArgs();
        try {
            List<String> list = new ArrayList();
            for (Object o : args) {
                list.add(new Gson().toJson(o));
            }
            sysLogBo.setRequestBody(list.toString());
        } catch (Exception e) {
            log.error("解析请求参数异常:{}", e.getMessage());
        }
        //sysLogService.save(sysLogBo);
        System.out.println("sysLogBO = " + sysLogBo);
    }
}