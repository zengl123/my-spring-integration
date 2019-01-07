package com.zenlong.study.api;

import com.zenlong.study.common.ServerResponse;
import com.zenlong.study.service.IGpsService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 描述:
 * 项目名:my-spring-integration
 *
 * @Author:ZENLIN
 * @Created 2019/1/7  10:37.
 */
@RestController
@RequestMapping(value = "/gps/")
public class GpsApi {
    private IGpsService service;

    public ServerResponse pushSave() {
        return null;
    }

    /**
     * 获取所有在线信息
     *
     * @return
     */
    @RequestMapping(value = "listActionState")
    public ServerResponse listActionState() {
        return service.listActionState();
    }
}
