package com.zenlong.study.api;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @Description
 * @Project my-spring-integration
 * @Package com.zenlong.study.api
 * @ClassName JspController
 * @Author 曾灵
 * @QQ|Email 3195690389|17363645521@163.com
 * @Date 2018-11-06 下午10:44
 * @Version 1.0
 * @Modified By
 */
@Controller
public class JspController {
    @RequestMapping(value = "/index")
    public String jspIndex(){
        return "index";
    }
}
