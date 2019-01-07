package com.zenlong.study.utils;

import com.zenlong.study.common.excpetion.ExceptionUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Hex;

import java.security.MessageDigest;

/**
 * 描述:
 * 项目名:my-spring-integration
 *
 * @Author:ZENLIN
 * @Created 2019/1/4  15:19.
 */
@Slf4j
public class GpsSignUtil {
    /**
     * 数据加密
     *
     * @param accPassword
     * @return
     */
    public static String sha256Pass(String accPassword) {
        MessageDigest messageDigest;
        try {
            messageDigest = MessageDigest.getInstance("SHA-256");
            byte[] hash = messageDigest.digest(accPassword.getBytes("UTF-8"));
            return Hex.encodeHexString(hash);
        } catch (Exception e) {
            log.error("加密异常:{}", ExceptionUtil.hand(e));
            return "";
        }
    }
}
