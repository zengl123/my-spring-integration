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
public interface Constant {

    String CURRENT_USER = "currentUser";
    String EMAIL = "email";
    String USER_NAME = "userName";
    String PHONE = "phone";

    interface Role {
        /**
         * 普通用户
         */
        String ROLE_CUSTOMER = "0";
        /**
         * 管理员
         */
        String ROLE_ADMIN = "1";
    }

    /**
     * 车牌校验正则
     */
    String REGEX = "([京津沪渝冀豫云辽黑湘皖鲁新苏浙赣鄂桂甘晋蒙陕吉闽贵粤青藏川宁琼使领 A-Z]{1}[A-Z]{1}(([0-9]{5}[DF])|([DF]([A-HJ-NP-Z0-9])[0-9]{4})))|([京津沪渝冀豫云辽黑湘皖鲁新苏浙赣鄂桂甘晋蒙陕吉闽贵粤青藏川宁琼使领 A-Z]{1}[A-Z]{1}[A-HJ-NP-Z0-9]{4}[A-HJ-NP-Z0-9 挂学警港澳]{1})";
}
