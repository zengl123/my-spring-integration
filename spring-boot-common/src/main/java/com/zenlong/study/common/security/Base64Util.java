package com.zenlong.study.common.security;

import org.apache.commons.net.util.Base64;
import sun.misc.BASE64Decoder;

import java.io.IOException;

/**
 * 描述:
 * 项目名:my-spring-integration
 *
 * @Author:ZENLIN
 * @Created 2018/12/14  16:29.
 */
public class Base64Util {

    /**
     * 加密
     * 加密byte[]类型，密文为字符串
     *
     * @param b
     * @return
     */
    public static String encodeByte(byte[] b) {
        return new String(new Base64().encode(b));
    }

    /**
     * 解密
     * 将字符串解密为String类型
     *
     * @param source
     * @return
     */
    public static String decodeToByte(String source) {
        return new String(new Base64().decode(source.getBytes()));
    }

    /**
     * 解密
     * 将字符串解密为String类型
     *
     * @param s
     * @param charSet 字符编码
     * @return
     */
    public static String getFromBase64(String s, String charSet) throws IOException {
        byte[] b;
        String result = null;
        if (s != null) {
            BASE64Decoder decoder = new BASE64Decoder();
            b = decoder.decodeBuffer(s);
            result = new String(b, charSet);
        }
        return result;
    }
}
