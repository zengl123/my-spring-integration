package com.zenlong.study.domain;

/**
 * 描述:
 * 项目名:my-spring-integration
 *
 * @Author:ZENLIN
 * @Created 2018/12/14  9:53.
 */

import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class SysLogBO {
    /**
     * 描述
     */
    private String remark;
    /**
     * ip地址
     */
    @SerializedName(value = "ip_address")
    private String ipAddress;
    /**
     * 请求地址
     */
    @SerializedName(value = "request_url")
    private String requestUrl;
    /**
     * 请求参数
     */
    @SerializedName(value = "request_body")
    private String requestBody;
    /**
     * 请求类
     */
    @SerializedName(value = "class_name")
    private String className;
    /**
     * 请求方法
     */
    @SerializedName(value = "method_name")
    private String methodName;
    /**
     * 耗时
     */
    @SerializedName(value = "execution_time")
    private Long executionTime;
    /**
     * 创建时间
     */
    @SerializedName(value = "create_time")
    private String createTime;
}