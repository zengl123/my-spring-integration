package com.zenlong.study.common.utils;

import java.util.UUID;

/**
 * @Project spring-boot-all
 * @Package com.zenlin.mysql.web.utils
 * @ClassName UuidUtil
 * @Author ZENLIN
 * @Date 2018-08-28 22:08
 * @Description TODO
 * @Version
 * @Modified By
 */
public class UuidUtil {
    /**
     * 获取uuid
     *
     * @return
     */
    public static String createId() {
        UUID uuid = UUID.randomUUID();
        return uuid.toString().replace("-", "");
    }
}
