package com.zenlong.study.schedule;

import com.zenlong.study.service.ITrafficService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 描述:
 * 项目名:my-spring-integration
 *
 * @Author:ZENLIN
 * @Created 2018/12/21  10:59.
 */
@Component
@EnableScheduling
@PropertySource("classpath:hk8700.properties")
public class Hk87Schedule {

    @Autowired
    private ITrafficService service;

    @Scheduled(cron = "${tdp.hk.schedule.traffic_current_time}")
    public void dataSyncSchedule() {
        service.dataCurrentTimeSync();
    }
}
