package com.zenlong.study.dao;

import com.zenlong.study.domain.po.GpsRecord;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * 描述:
 * 项目名:my-spring-integration
 *
 * @Author:ZENLIN
 * @Created 2018/12/17  10:03.
 */
public interface GpsRepository extends ElasticsearchRepository<GpsRecord, String> {

}
