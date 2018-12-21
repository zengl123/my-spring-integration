package com.zenlong.study.common.excpetion;

import com.alibaba.fastjson.JSONObject;
import com.zenlong.study.common.ServerResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Description 异常处理相关的工具类。
 * @Project my-spring-integration
 * @Package com.zenlong.study.common.excpetion
 * @ClassName ExceptionUtil
 * @Author 曾灵
 * @QQ|Email 3195690389|17363645521@163.com
 * @Date 2018-09-13 23:51
 * @Version 1.0
 * @Modified By
 */
@Slf4j
@RestControllerAdvice
public class ExceptionUtil {

    /**
     * 针对ServletException堆栈追踪信息获取方式的不同，做单独处理。
     */
    private static final String SERVLET_EXCEPTION_NAME = "javax.servlet.ServletException";

    /**
     * 发生ServletException异常时使用的方法名-用来获取发生Servlet异常的原因。
     */
    private static final String GET_ROOT_CAUSE = "getRootCause";

    /**
     * 获取指定异常的堆栈追踪信息。
     * <p/>
     * <p>
     * 如果能够获取该异常的产生原因，递归获取异常的堆栈信息。
     * 针对ServletException时，需使用getRootCause()方法。
     * </p>
     *
     * @param throwable 异常
     * @return 异常堆栈追踪信息
     */
    public static String getStackTrace(Throwable throwable) {
        StringBuilder sb = new StringBuilder();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        while (throwable != null) {
            byteArrayOutputStream.reset();
            throwable.printStackTrace(new PrintStream(byteArrayOutputStream));
            sb.append(byteArrayOutputStream.toString());
            // 获取throwable的Class对象(添加合适的泛型来修改throwableClass的声明)
            Class<?> throwableClass = throwable.getClass();
            // 异常为ServletException时使用getRootCause方法
            if (SERVLET_EXCEPTION_NAME.equals(throwableClass.getName())) {
                try {
                    // 执行Class的指定方法
                    Method method = throwableClass.getMethod(GET_ROOT_CAUSE);
                    throwable = (Throwable) method.invoke(throwable);
                } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                    // 未找到对应的方法
                    log.error(e.getMessage());
                    throwable = null;
                }
            } else {
                throwable = throwable.getCause();
            }
        }
        return sb.toString();
    }

    @ExceptionHandler(CusException.class)
    public ServerResponse handException(CusException e) {
        String message = e.getMessage();
        return ServerResponse.createByErrorMessage(message);
    }

    @ExceptionHandler(Exception.class)
    public ServerResponse handException(Exception e) {
        log.error("程序异常:{}", hand(e).getMessage());
        return ServerResponse.createByErrorMessage("程序异常");
    }


    public static ServerResponse hand(Exception e) {
        List<Map> collect = Arrays.asList(e.getStackTrace()).stream().map(stackTraceElement -> {
            Map linkedHashMap = new LinkedHashMap();
            linkedHashMap.put("lineNumber", stackTraceElement.getLineNumber());
            linkedHashMap.put("fileNme", stackTraceElement.getFileName());
            linkedHashMap.put("className", stackTraceElement.getClassName());
            linkedHashMap.put("methodName", stackTraceElement.getMethodName());
            return linkedHashMap;
        }).collect(Collectors.toList());
        //
        List throwableList = Arrays.asList(e.getSuppressed()).stream().map(throwable -> throwable.getCause()).collect(Collectors.toList());
        String message = CollectionUtils.isEmpty(throwableList) ? e.getMessage() : throwableList.toString();
        JSONObject json = new JSONObject();
        json.put("message", message);
        json.put("info", collect);
        return ServerResponse.createByErrorMessage(json.toJSONString());
    }
}
