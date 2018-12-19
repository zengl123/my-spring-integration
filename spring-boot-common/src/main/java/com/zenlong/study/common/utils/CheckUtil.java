package com.zenlong.study.common.utils;

import com.zenlong.study.common.constant.Constant;

import java.util.regex.Pattern;

/**
 * 描述:
 * 项目名:my-spring-integration
 *
 * @Author:ZENLIN
 * @Created 2018/12/19  17:51.
 */
public class CheckUtil {
    /**
     * 校验车牌号码
     *
     * @param plateNumber
     * @return
     */
    public static boolean checkPlateNumber(String plateNumber) {
        Pattern compile = Pattern.compile(Constant.REGEX);
        return compile.matcher(plateNumber).find();
    }
}
