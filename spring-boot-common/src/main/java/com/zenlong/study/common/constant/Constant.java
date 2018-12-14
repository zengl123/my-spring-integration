package com.zenlong.study.common.constant;

/**
 * @Project spring-boot-all
 * @Package com.zenlin.mysql.web.common
 * @ClassName Constant
 * @Author ZENLIN
 * @Date 2018-09-01 17:49
 * @Description TODO
 * @Version
 * @Modified By
 */
public class Constant {

    public static final String CURRENT_USER = "currentUser";
    public static final String EMAIL = "email";
    public static final String USER_NAME = "userName";
    public static final String PHONE = "phone";

    public interface Role {
        /**
         * 普通用户
         */
        String ROLE_CUSTOMER = "0";
        /**
         * 管理员
         */
        String ROLE_ADMIN = "1";
    }
}
