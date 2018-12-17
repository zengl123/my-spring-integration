package com.zenlong.study;

import com.alibaba.fastjson.JSONObject;
import com.zenlong.study.common.ServerResponse;
import com.zenlong.study.domain.po.GpsRecord;

import java.util.List;

/**
 * 描述:
 * 项目名:my-spring-integration
 *
 * @Author:ZENLIN
 * @Created 2018/12/17  9:47.
 */
public interface IGpsService {
    /**
     * 新增记录
     *
     * @param requestBody
     * @return
     */
    ServerResponse save(JSONObject requestBody);

    /**
     * 获取实时在线设备
     *
     * @param requestBody
     * @return
     */
    ServerResponse<GpsRecord> listOnlineDevice(JSONObject requestBody);

    /**
     * 根据日期获取在线设备
     *
     * @return
     */
    ServerResponse<List<GpsRecord>>listDeviceInfoByDate(JSONObject requestBody);

    /**
     * 根据设备编号和时间段获取历史记录
     *
     * @param requestBody
     * @return
     */
    ServerResponse<GpsRecord> listHistoryRecordByDeviceAndTime(JSONObject requestBody);
}
