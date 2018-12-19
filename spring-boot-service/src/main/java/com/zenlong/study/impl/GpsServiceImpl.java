package com.zenlong.study.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zenlong.study.IGpsService;
import com.zenlong.study.common.ServerResponse;
import com.zenlong.study.common.utils.DateTimeUtil;
import com.zenlong.study.dao.GpsRepository;
import com.zenlong.study.domain.po.GpsRecord;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.terms.StringTerms;
import org.elasticsearch.search.sort.SortBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.elasticsearch.index.query.QueryBuilders.termQuery;

/**
 * 描述:
 * 项目名:my-spring-integration
 *
 * @Author:ZENLIN
 * @Created 2018/12/17  9:48.
 */
@Slf4j
@Service
public class GpsServiceImpl implements IGpsService {

    @Autowired
    private GpsRepository repository;
    @Autowired
    private ElasticsearchTemplate template;

    @Override
    public ServerResponse save(JSONObject requestBody) {
        GpsRecord gpsRecord = JSON.toJavaObject(requestBody, GpsRecord.class);
        String gpsTimeString = gpsRecord.getGpsTime();
        gpsRecord.setGpsTimeLong(DateTimeUtil.stringToMilli(gpsTimeString));
        GpsRecord save = repository.save(gpsRecord);
        save.setGpsTimeLong(null);
        return ServerResponse.createBySuccess(save);
    }


    /**
     * 获取实时在线设备
     *
     * @param requestBody
     * @return
     */
    @Override
    public ServerResponse<GpsRecord> listOnlineDevice(JSONObject requestBody) {
        return null;
    }

    /**
     * 根据日期获取在线设备
     *
     * @param requestBody
     * @return
     */
    @Override
    public ServerResponse<List<GpsRecord>> listDeviceInfoByDate(JSONObject requestBody) {
        //2.构建查询
        NativeSearchQueryBuilder nativeSearchQueryBuilder = new NativeSearchQueryBuilder();
        String deviceNo = requestBody.getString("deviceNo");
        //设备编号
        if (StringUtils.isNotEmpty(deviceNo)) {
            //条件精确查询
            TermQueryBuilder termQueryBuilder = termQuery("deviceNo", deviceNo);
            nativeSearchQueryBuilder.withQuery(termQueryBuilder);
        }
        //时间分组

        //排序
        SortBuilder sort = SortBuilders.fieldSort("gpsTimeLong").order(SortOrder.DESC);
        //将排序设置到构建中
        nativeSearchQueryBuilder.withSort(sort);
        //生产NativeSearchQuery
        NativeSearchQuery query = nativeSearchQueryBuilder.build();
        Aggregations aggregations;
        try {
            aggregations = template.query(query, response -> response.getAggregations());
        } catch (Exception e) {
            log.error("根据日期获取在线设备异常:{}", e.getMessage());
            return ServerResponse.createByErrorMessage("根据日期获取在线设备失败");
        }
        //转换成map集合
        Map<String, Aggregation> aggregationMap = aggregations.asMap();
        //获得对应的聚合函数的聚合子类，该聚合子类也是个map集合,里面的value就是桶Bucket，我们要获得Bucket
        StringTerms stringTerms = (StringTerms) aggregationMap.get("agg_date");
        //获得所有的桶
        List<StringTerms.Bucket> buckets = stringTerms.getBuckets();
        GpsRecord gpsRecord = new GpsRecord();
        List<GpsRecord> device = buckets.stream().map(bucket -> {
            gpsRecord.setDeviceNo(String.valueOf(bucket.getKey()));
            return gpsRecord;
        }).collect(Collectors.toList());
        return ServerResponse.createBySuccess(device);
    }

    /**
     * 根据设备编号和时间段获取历史记录
     *
     * @param requestBody
     * @return
     */
    @Override
    public ServerResponse<GpsRecord> listHistoryRecordByDeviceAndTime(JSONObject requestBody) {
        return null;
    }
}
