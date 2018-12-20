package com.zenlong.study.service;

import com.zenlong.study.common.ServerResponse;

/**
 * 描述:
 * 项目名:my-spring-integration
 *
 * @Author:ZENLIN
 * @Created 2018/12/20  14:41.
 */
public interface ITrafficService {
    /**
     * 原始数据同步
     *
     * @return
     */
    ServerResponse dataSync();

    /**
     * 客流量天数据
     *
     * @return
     */
    ServerResponse dataDaySync();
}
