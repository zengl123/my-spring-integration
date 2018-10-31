package com.zenlong.study.domain.pojo;

import lombok.Data;

/**
 * 描述:
 * 项目名:my-spring-integration
 *
 * @Author:ZENLIN
 * @Created 2018/10/24  10:40.
 */
@Data
public class DeviceConfigInfo<T> {
    private String factoryModelName;
    private T data;
}
