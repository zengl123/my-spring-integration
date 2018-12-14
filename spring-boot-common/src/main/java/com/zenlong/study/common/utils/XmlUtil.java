package com.zenlong.study.common.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.json.XML;
import lombok.extern.slf4j.Slf4j;

/**
 * 描述:
 * 项目名:my-spring-integration
 *
 * @Author:ZENLIN
 * @Created 2018/12/10  14:55.
 */
@Slf4j
public class XmlUtil {
    /**
     * 将xml字符串转换成json对象
     *
     * @param xmlString
     * @return
     */
    public static JSONObject xml2json(String xmlString) {
        try {
            return JSON.parseObject(String.valueOf(XML.toJSONObject(xmlString)));
        } catch (Exception e) {
            log.error("xml转换异常:{}", e.getMessage());
            return null;
        }
    }
}
