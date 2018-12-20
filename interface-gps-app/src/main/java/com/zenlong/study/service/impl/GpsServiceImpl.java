package com.zenlong.study.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.zenlong.study.common.ServerResponse;
import com.zenlong.study.domain.constant.Constant;
import com.zenlong.study.domain.po.GpsRecord;
import com.zenlong.study.es.utils.ElasticsearchUtil;
import com.zenlong.study.redis.utils.RedisUtil;
import com.zenlong.study.service.IGpsService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * 描述:
 * 项目名:my-spring-integration
 *
 * @Author:ZENLIN
 * @Created 2018/11/27  9:53.
 */
@Service
public class GpsServiceImpl implements IGpsService {
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private ElasticsearchUtil elasticsearchUtil;
    @Value("${tdp.gps.time-out}")
    private String time;

    /**
     * 同步gps数据记录
     *
     * @param requestBody
     * @return
     */
    @Override
    public ServerResponse sync(JSONObject requestBody) {
        JSONObject data = requestBody.getJSONObject("data");
        GpsRecord gpsRecord = new GpsRecord();
        BeanUtils.copyProperties(data, gpsRecord);
        redisUtil.set(Constant.REDIS_GPS_REAL_TIME + gpsRecord.getDeviceNo(), String.valueOf(gpsRecord));
        return elasticsearchUtil.insert(GpsRecord.INDEX, GpsRecord.TYPE, gpsRecord);
    }
}
