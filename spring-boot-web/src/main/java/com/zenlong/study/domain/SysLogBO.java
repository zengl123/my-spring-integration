package com.zenlong.study.domain;

/**
 * 描述:
 * 项目名:my-spring-integration
 *
 * @Author:ZENLIN
 * @Created 2018/12/14  9:53.
 */

import lombok.Data;

@Data
public class SysLogBO {
    /**
     * 描述
     */
    private String remark;
    /**
     * 请求地址
     */
    private String address;
    /**
     * 请求参数
     */
    private String params;
    /**
     * 请求类
     */
    private String className;
    /**
     * 请求方法
     */
    private String methodName;
    /**
     * 耗时
     */
    private Long executionTime;
    /**
     * 创建时间
     */
    private String createDate;
}