package com.zenlong.study.service;

import com.zenlong.study.common.ServerResponse;

/**
 * 描述:
 * 项目名:my-spring-integration
 *
 * @Author:ZENLIN
 * @Created 2019/1/4  17:01.
 */
public interface IGpsService {
    /**
     * 获取所有在线信息
     *
     * @return
     */
    ServerResponse listActionState();
}
