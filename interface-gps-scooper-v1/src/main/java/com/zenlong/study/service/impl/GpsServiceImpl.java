package com.zenlong.study.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zenlong.study.common.ServerResponse;
import com.zenlong.study.common.excpetion.ExceptionUtil;
import com.zenlong.study.common.httpclient.HttpClientUtil;
import com.zenlong.study.common.httpclient.HttpClientUtil.Result;
import com.zenlong.study.service.IGpsService;
import com.zenlong.study.utils.GpsSignUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

/**
 * 描述:
 * 项目名:my-spring-integration
 *
 * @Author:ZENLIN
 * @Created 2019/1/4  17:01.
 */
@Slf4j
@Service
@PropertySource("classpath:gps.yml")
public class GpsServiceImpl implements IGpsService {
    @Value(value = "${tdp.gps.url}")
    private String url;
    @Value(value = "${tdp.gps.userName}")
    private String userName;
    @Value(value = "${tdp.gps.password}")
    private String password;

    private String token;

    private void getToken() {
        if (StringUtils.isEmpty(userName) || StringUtils.isEmpty(password)) {
            log.error(" userName or password is empty");
            return;
        }
        String passWord = GpsSignUtil.sha256Pass(password);
        String requestUrl = url + "?accUsername=" + userName + "&accPassword=" + passWord;
        Result invoke;
        try {
            invoke = HttpClientUtil.GET.invoke(requestUrl, null, null);
        } catch (Exception e) {
            log.error("获取token异常:{}", ExceptionUtil.hand(e));
            return;
        }
        int status = invoke.getStatus();
        if (HttpStatus.SC_OK != status) {
            log.error("获取token失败");
            return;
        }
        try {
            String content = invoke.getContent();
            JSONObject object = JSON.parseObject(content);
            token = object.getString("data");
        } catch (Exception e) {
            log.error("获取token接口响应结果解析异常:{}", ExceptionUtil.hand(e));
            return;
        }
    }

    /**
     * 获取所有在线信息
     *
     * @return
     */
    @Override
    public ServerResponse listActionState() {
        if (StringUtils.isEmpty(token)) {
            getToken();
        }
        if (StringUtils.isEmpty(token)) {
            return ServerResponse.createByErrorMessage("获取token失败");
        }
        String requestUrl = url + "getAllActionState?token=" + token;
        Result invoke;
        try {
            invoke = HttpClientUtil.GET.invoke(requestUrl, null, null);
        } catch (Exception e) {
            log.error("获取在线信息接口请求异常:{}", ExceptionUtil.hand(e));
            return ServerResponse.createByErrorMessage("获取在线信息失败");
        }
        int status = invoke.getStatus();
        String message = invoke.getContent();
        if (HttpStatus.SC_OK != status) {
            log.error("获取在线信息接口请求失败:{}", message);
            return ServerResponse.createByErrorMessage("获取在线信息失败");
        }
        try {
            JSONObject object = JSONObject.parseObject(message);
            log.info("data:{}", object);
            return ServerResponse.createBySuccess(object);
        } catch (Exception e) {
            log.error("获取在线信息接口响应结果解析异常:{}", ExceptionUtil.hand(e));
            return ServerResponse.createByErrorMessage("获取在线信息失败");
        }
    }
}
