package com.zenlong.study.service;

import com.zenlong.study.common.ServerResponse;
import com.zenlong.study.domain.po.TrafficDeviceInfo;

import java.util.List;

/**
 * 描述:
 * 项目名:my-spring-integration
 *
 * @Author:ZENLIN
 * @Created 2018/12/20  14:41.
 */
public interface ITrafficService {
    /**
     * 新增客流监控点信息
     *
     * @param deviceInfo
     * @return
     */
    ServerResponse insertTrafficDeviceInfo(TrafficDeviceInfo deviceInfo);

    /**
     * 原始数据同步
     *
     * @return
     */
    ServerResponse dataCurrentTimeSync();

    /**
     * 客流量天数据
     *
     * @return
     */
    ServerResponse dataDaySync();
}
