package com.zenlong.study.es.utils;

import com.zenlong.study.common.ServerResponse;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

/**
 * 描述:
 * 项目名:my-spring-integration
 *
 * @Author:ZENLIN
 * @Created 2018/10/23  11:59.
 */
@SpringBootTest
@RunWith(SpringRunner.class)
@Slf4j
public class ElasticsearchUtil {
    @Autowired
    private RestHighLevelClient highLevelClient;


    public ServerResponse get(String index, String type, String id) {
        GetRequest getRequest = new GetRequest(index, type, id);
        try {
            GetResponse documentFields = highLevelClient.get(getRequest);
            return ServerResponse.createBySuccess();
        } catch (IOException e) {
            log.error(e.getMessage());
            ServerResponse<Object> byErrorMessage = ServerResponse.createByErrorMessage(e.getMessage());
            boolean success = byErrorMessage.isSuccess();
            return null;
        }
    }

    @Test
    public void testGet() {
        ServerResponse serverResponse = get("indexvehiclegpsrecord", "vehicle_gps_record", "ab94a00d4e1344278f4350f7f8aed0ce");
        System.out.println("serverResponse = " + serverResponse);
    }
}
