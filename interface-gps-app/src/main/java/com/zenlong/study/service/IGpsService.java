package com.zenlong.study.service;

import com.alibaba.fastjson.JSONObject;
import com.zenlong.study.common.ServerResponse;

/**
 * 描述:
 * 项目名:my-spring-integration
 *
 * @Author:ZENLIN
 * @Created 2018/11/27  9:44.
 */
public interface IGpsService {
    /**
     * 同步gps数据记录
     *
     * @param requestBody
     * @return
     */
    ServerResponse sync(JSONObject requestBody);
}
