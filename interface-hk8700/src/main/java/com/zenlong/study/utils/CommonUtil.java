package com.zenlong.study.utils;

import com.alibaba.fastjson.JSONObject;
import com.zenlong.study.common.ServerResponse;
import com.zenlong.study.common.httpclient.HttpClientUtil;
import com.zenlong.study.common.httpclient.HttpClientUtil.Result;
import com.zenlong.study.constant.Hk87Constant;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * 描述:
 * 项目名:my-spring-integration
 *
 * @Author:ZENLIN
 * @Created 2018/12/20  14:57.
 */
@Slf4j
@Component
@PropertySource("classpath:hk8700.properties")
public class CommonUtil {
    @Value("${tdp.hk.url}")
    private String host;
    @Value("${tdp.hk.key}")
    private String appKey;
    @Value("${tdp.hk.secret}")
    private String secret;

    /**
     * 获取默认用户UUID
     *
     * @return
     */
    public ServerResponse getDefaultUserUuid(String host, String appKey, String secret) {
        String path = Hk87Constant.INTERFACE_GET_DEFAULT_USER_UUID;
        JSONObject param = new JSONObject();
        param.put("time", System.currentTimeMillis());
        param.put("appkey", appKey);
        String url = SignUtil.postBuildToken(host, path, param, secret);
        Result result;
        try {
            result = HttpClientUtil.POST.applicationJson(url, null, param.toJSONString(), null);
        } catch (Exception e) {
            log.error("请求获取默认用户UUID接口异常:{}", e.getMessage());
            return ServerResponse.createByErrorMessage("获取默认用户UUID失败");
        }
        int status = result.getStatus();
        String content = result.getContent();
        if (HttpStatus.SC_OK != status) {
            log.error("请求获取默认用户UUID接口失败:{}", content);
            return ServerResponse.createByErrorMessage("获取默认用户UUID失败");
        }
        try {
            JSONObject object = JSONObject.parseObject(content);
            String data = object.getString("data");
            if (StringUtils.isNotEmpty(data)) {
                return ServerResponse.createBySuccess(data);
            } else {
                return ServerResponse.createByErrorMessage("获取默认用户不存在");
            }
        } catch (Exception e) {
            log.error("获取默认用户UUID接口响应数据解析异常,errorMessage:{}", e.getMessage());
            return ServerResponse.createByErrorMessage("获取默认用户UUID失败");
        }
    }
}
