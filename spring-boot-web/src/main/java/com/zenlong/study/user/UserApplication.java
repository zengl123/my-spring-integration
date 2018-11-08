package com.zenlong.study.user;

import com.zenlong.study.IEcUserService;
import com.zenlong.study.common.ServerResponse;
import com.zenlong.study.domain.pojo.EcUser;
import com.zenlong.study.utils.ValidUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * @Description
 * @Project my-spring-integration
 * @Package com.zenlong.study.web.user
 * @ClassName UserApplication
 * @Author 曾灵
 * @QQ|Email 3195690389|17363645521@163.com
 * @Date 2018-09-20 23:29
 * @Version 1.0
 * @Modified By
 */

@Api(tags = "USER", description = "用户模块")
@RestController
@RequestMapping("/user/")
public class UserApplication {
    @Autowired
    private IEcUserService service;

    @ApiOperation(value = "register", notes = "用户注册")
    @RequestMapping(value = "register", method = RequestMethod.POST)
    public ServerResponse register(@RequestBody @Valid EcUser ecUser, BindingResult bindingResult) {
        ServerResponse serverResponse = ValidUtil.requestBodyValid(bindingResult);
        if (serverResponse.isSuccess()) {
            return service.register(ecUser);
        } else {
            return serverResponse;
        }
    }
}
