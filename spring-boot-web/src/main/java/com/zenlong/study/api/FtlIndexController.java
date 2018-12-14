package com.zenlong.study.api;

import com.alibaba.fastjson.JSONObject;
import com.zenlong.study.annotation.SysLog;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;

/**
 * @Description
 * @Project my-spring-integration
 * @Package com.zenlong.study.api
 * @ClassName FtlIndexController
 * @Author 曾灵
 * @QQ|Email 3195690389|17363645521@163.com
 * @Date 2018-11-06 下午10:30
 * @Version 1.0
 * @Modified By
 */
@RestController
public class FtlIndexController {
    @SysLog("登陆")
    @RequestMapping(value = "/ftlIndex")
    public String ftlIndex(@RequestBody(required = false) JSONObject requestBody) {
        return "index";
    }
}
