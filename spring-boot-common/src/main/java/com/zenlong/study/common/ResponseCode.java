package com.zenlong.study.common;

/**
 * @Project spring-boot-all
 * @Package com.zenlin.mysql.web.common
 * @ClassName ResponseCode
 * @Author ZENLIN
 * @Date 2018-08-27 21:43
 * @Description TODO
 * @Version
 * @Modified By
 */
public enum ResponseCode {
    SUCCESS(0, "SUCCESS"),
    ERROR(1, "ERROR");

    private final int code;
    private final String desc;

    ResponseCode(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public int getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}
