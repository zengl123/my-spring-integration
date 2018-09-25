package com.zenlong.study.common.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.MessageDigest;

/**
 * @Project spring-boot-all
 * @Package com.zenlin.common.security
 * @ClassName MD5Util
 * @Author ZENLIN
 * @Date 2018-05-20 11:17
 * @Description TODO
 * @Version
 * @Modified By
 */
public class Md5Util {

    private static final Logger LOGGER = LoggerFactory.getLogger(Md5Util.class);

    private static final String[] HEX_DIGITS = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f"};

    private static String encodingCharset = "UTF-8";

    private static String byteArrayToHexString(byte[] b) {
        StringBuffer resultSb = new StringBuffer();
        for (int i = 0; i < b.length; i++) {
            resultSb.append(byteToHexString(b[i]));
        }
        return resultSb.toString();
    }

    private static String byteToHexString(byte b) {
        int n = b;
        if (n < 0) {
            n += 256;
        }
        int d1 = n / 16;
        int d2 = n % 16;
        return HEX_DIGITS[d1] + HEX_DIGITS[d2];
    }

    /**
     * MD5加密
     *
     * @param origin
     * @param charsetName
     * @return
     */
    public static String Md5Encode(String origin, String charsetName) {
        String resultString;
        try {
            resultString = new String(origin);
            MessageDigest md = MessageDigest.getInstance("MD5");
            if (charsetName == null || "".equals(charsetName)) {
                resultString = byteArrayToHexString(md.digest(resultString.getBytes()));
            } else {
                resultString = byteArrayToHexString(md.digest(resultString.getBytes(charsetName)));
            }
        } catch (Exception exception) {
            resultString = "";
            LOGGER.error("Md5加密异常,error={}", exception);
        }
        return resultString;
    }

    /**
     * 普通Md5加密
     *
     * @param origin
     * @return
     */
    public static String Md5EncodeUtf8(String origin) {
        return Md5Encode(origin, encodingCharset);
    }

    /**
     * 普通Md5加密
     *
     * @param origin
     * @return
     */
    public static String Md5EncodeUtf8Salt(String origin) {
        return Md5Encode(origin, encodingCharset);
    }
}
