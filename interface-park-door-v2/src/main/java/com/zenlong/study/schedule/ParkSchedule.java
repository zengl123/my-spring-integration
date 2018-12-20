package com.zenlong.study.schedule;

import com.zenlong.study.service.IParkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 描述:
 * 项目名:my-spring-integration
 *
 * @Author:ZENLIN
 * @Created 2018/12/20  10:38.
 */
@Component
@EnableScheduling
@PropertySource("classpath:park-door.properties")
public class ParkSchedule {
    @Autowired
    private IParkService service;

    @Scheduled(cron = "${tdp.park.schedule.info}")
    public void parkInfoSchedule() {
        service.listParkInfo();
    }
}
