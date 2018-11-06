package com.zenlong.study.es;

import com.zenlong.study.common.ServerResponse;
import com.zenlong.study.es.utils.ElasticsearchUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

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
    private ElasticsearchUtil util;


    @Test
    public void testGet() {
        ServerResponse serverResponse = util.getById("indexvehiclegpsrecord", "vehicle_gps_record", "ab94a00d4e1344278f4350f7f8aed0c");
        System.out.println("serverResponse = " + serverResponse);
    }
}
