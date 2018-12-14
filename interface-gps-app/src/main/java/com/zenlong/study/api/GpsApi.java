package com.zenlong.study.api;

import com.alibaba.fastjson.JSONObject;
import com.zenlong.study.common.ServerResponse;
import com.zenlong.study.service.IGpsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * 描述:
 * 项目名:my-spring-integration
 *
 * @Author:ZENLIN
 * @Created 2018/11/27  9:54.
 */
@RestController
@RequestMapping(value = "/tdp/gps/")
public class GpsApi {
    @Autowired
    private IGpsService service;

    @RequestMapping(value = "sync", method = RequestMethod.POST)
    private ServerResponse sync(@RequestBody JSONObject requestBody) {
        return service.sync(requestBody);
    }
}
