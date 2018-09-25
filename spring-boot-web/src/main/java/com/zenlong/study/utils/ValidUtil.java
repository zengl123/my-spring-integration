package com.zenlong.study.utils;

import com.zenlong.study.common.ServerResponse;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;

import java.util.List;

/**
 * @Description
 * @Project my-spring-integration
 * @Package com.zenlong.study.utils
 * @ClassName ValidUtil
 * @Author 曾灵
 * @QQ|Email 3195690389|17363645521@163.com
 * @Date 2018-09-22 15:23
 * @Version 1.0
 * @Modified By
 */
public class ValidUtil {
    public static ServerResponse requestBodyValid(BindingResult bindingResult) {
        StringBuilder builder = new StringBuilder();
        if (bindingResult.hasErrors()) {
            List<ObjectError> allErrors = bindingResult.getAllErrors();
            allErrors.stream().forEach(error -> {
                String defaultMessage = error.getDefaultMessage();
                builder.append(defaultMessage);
            });
            return ServerResponse.createByErrorMessage(builder.toString());
        } else {
            return ServerResponse.createBySuccess();
        }
    }
}
