package com.zenlong.study.constant;

/**
 * 描述:
 * 项目名:my-spring-integration
 *
 * @Author:ZENLIN
 * @Created 2018/12/20  14:53.
 */
public interface Hk87Constant {
    /**
     * 接口成功响应状态码
     */
    Integer RESPONSE_SUCCESS_CODE = 0;
    /**
     * 默认页码
     */
    Integer PAGE_NO = 1;
    /**
     * 默认大小
     */
    Integer PAGE_SIZE = 100;
    /**
     * 获取默认用户UUID的接口地址
     */
    String INTERFACE_GET_DEFAULT_USER_UUID = "/openapi/service/base/user/getDefaultUserUuid";
    /**
     * 获取客流量原始数据接口
     */
    String INTERFACE_GET_FOOTFALL_DATA = "/openapi/service/vss/footfall/getFootfallData";
    /**
     * 客流量天数据
     */
    String INTERFACE_GET_FOOTFALL_DATA_BY_DAY = "/openapi/service/vss/footfall/getFootfallDataByDay";

}
