package com.zenlong.study.es;

import com.alibaba.fastjson.JSONObject;
import com.zenlong.study.common.ServerResponse;
import com.zenlong.study.es.utils.ElasticsearchUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.junit4.SpringRunner;
import sun.plugin.javascript.JSObject;

import java.util.HashMap;
import java.util.Map;

/**
 * 描述:
 * 项目名:my-spring-integration
 *
 * @Author:ZENLIN
 * @Created 2018/10/23  11:59.
 */
@Slf4j
@SpringBootTest
@RunWith(SpringRunner.class)
public class ElasticsearchUtilTest {
    @Autowired
    public ElasticsearchUtil util;

    public String index = "indexgpsrecord";
    public String type = "gps_record";

    @Test
    public void testInsert() {
        JSONObject object = new JSONObject();
        object.put("deviceNo", "test-01");
        object.put("deviceName", "测试数据新增");
        ServerResponse serverResponse = util.insert(index, type, object);
        log.info("新增结果:{},success:{}", serverResponse, serverResponse.isSuccess());
    }


    @Test
    public void testDeletedById() {
        ServerResponse serverResponse = util.getById(index, type, "ab94a00d4e1344278f4350f7f8aed0c");
        log.info("删除结果:{},success:{}", serverResponse, serverResponse.isSuccess());
    }

    @Test
    public void testGet() {
        ServerResponse serverResponse = util.getById(index, type, "ab94a00d4e1344278f4350f7f8aed0c");
        log.info("查询结果:{},success:{}", serverResponse, serverResponse.isSuccess());
    }

    @Test
    public void testExist() throws Exception {
        boolean result = util.isIndexExist(index);
        log.info("查询结果:{},success:{}", result, result);
    }

    @Test
    public void testQueryByTerm() {
        Map map = new HashMap();
        map.put("device_no", "test01");
        ServerResponse serverResponse = util.queryByTerm(index, type, map);
        System.out.println("serverResponse = " + serverResponse);
    }

    @Test
    public void test() {
        String x = " 123";
        String trim = x.trim();
        log.info("trim:{}", trim);
    }
}
