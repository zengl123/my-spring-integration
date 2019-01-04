package com.zenlong.study.service;

import com.zenlong.study.common.ServerResponse;
import com.zenlong.study.domain.po.ParkExportRecord;
import com.zenlong.study.domain.po.ParkDeviceInfo;

import java.util.List;

/**
 * 描述:道尔停车场数据对接接口
 * 项目名:my-spring-integration
 *
 * @Author:ZENLIN
 * @Created 2018/10/23  15:37.
 */
public interface IParkService {
    /**
     * 获取停车场信息列表
     *
     * @return
     */
    ServerResponse<List<ParkDeviceInfo>> listParkInfo();

    /**
     * 进场记录
     *
     * @return
     */
    ServerResponse<List<ParkExportRecord>> listParkExport();
}
