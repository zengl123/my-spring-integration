package com.zenlong.study.api;

import com.alibaba.fastjson.JSONObject;
import com.zenlong.study.IGpsService;
import com.zenlong.study.common.ServerResponse;
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
 * @Created 2018/12/17  11:14.
 */
@RestController
@RequestMapping(value = "/es/gps/")
public class GpsController {
    @Autowired
    private IGpsService service;

    /**
     * 新增记录
     *
     * @param requestBody
     * @return
     */
    @RequestMapping(value = "save", method = RequestMethod.POST)
    public ServerResponse save(@RequestBody JSONObject requestBody) {
        return service.save(requestBody);
    }

    @RequestMapping(value = "listDeviceInfoByDate")
    public ServerResponse listDeviceInfoByDate(@RequestBody JSONObject requestBody) {
        return service.listDeviceInfoByDate(requestBody);
    }
}
