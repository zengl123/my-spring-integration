package com.zenlong.study.api;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

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
@Controller
public class FtlIndexController {
    @RequestMapping(value = "/ftlIndex")
    public String ftlIndex(){
        return "ftlIndex";
    }
}
