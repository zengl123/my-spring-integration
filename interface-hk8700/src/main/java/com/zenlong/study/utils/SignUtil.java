package com.zenlong.study.utils;

import com.alibaba.fastjson.JSONObject;
import com.zenlong.study.common.security.Md5Util;

/**
 * 描述:
 * 项目名:cloud-tdp-data-interface
 *
 * @Author:ZENLIN
 * @Created 2018/3/28  17:37.
 */
public class SignUtil {
    /**
     * 接口参数封装公共方法
     *
     * @param host
     * @param path
     * @param param
     * @param secret
     * @return
     */
    public static final String postBuildToken(String host, String path, JSONObject param, String secret) {
        StringBuilder url = new StringBuilder();
        url.append(host).append(path);
        String jsonString = JSONObject.toJSONString(param);
        StringBuilder builder = new StringBuilder();
        String md5String = builder.append(path).append(jsonString).append(secret).toString();
        String token = Md5Util.Md5EncodeUtf8(md5String).toUpperCase();
        url.append("?token=").append(token);
        return url.toString();
    }
}
