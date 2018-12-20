package com.zenlong.study.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zenlong.study.common.ServerResponse;
import com.zenlong.study.common.httpclient.HttpClientUtil;
import com.zenlong.study.common.httpclient.HttpClientUtil.Result;
import com.zenlong.study.common.utils.DateTimeUtil;
import com.zenlong.study.constant.Hk87Constant;
import com.zenlong.study.domain.TrafficRecordThird;
import com.zenlong.study.domain.po.TrafficPointInfo;
import com.zenlong.study.domain.po.TrafficRecord;
import com.zenlong.study.es.utils.ElasticsearchUtil;
import com.zenlong.study.service.ITrafficService;
import com.zenlong.study.utils.CommonUtil;
import com.zenlong.study.utils.SignUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpStatus;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

/**
 * 描述:
 * 项目名:my-spring-integration
 *
 * @Author:ZENLIN
 * @Created 2018/12/20  14:44.
 */
@Slf4j
@Service
@PropertySource("classpath:hk8700.properties")
public class TrafficServiceImpl implements ITrafficService {
    @Value("${tdp.hk87.url}")
    private String host;
    @Value("${tdp.hk87.key}")
    private String appKey;
    @Value("${tdp.hk87.secret}")
    private String secret;
    private String defaultUuid;
    @Autowired
    private CommonUtil commonUtil;
    @Autowired
    private ElasticsearchUtil elasticsearchUtil;

    /**
     * 数据同步
     *
     * @return
     */
    @Override
    public ServerResponse dataSync() {
        if (StringUtils.isEmpty(defaultUuid)) {
            ServerResponse serverResponse = commonUtil.getDefaultUserUuid(host, appKey, secret);
            if (!serverResponse.isSuccess()) {
                return serverResponse;
            }
            defaultUuid = serverResponse.getData().toString();
        }
        ServerResponse<List<TrafficPointInfo>> serverResponse = listDeviceInfo();
        if (!serverResponse.isSuccess()) {
            return serverResponse;
        }
        //客流监控点集合
        List<TrafficPointInfo> trafficPointInfoList = serverResponse.getData();
        ServerResponse<List<TrafficRecord>> syncTime = getSyncTime();
        if (!syncTime.isSuccess()) {
            return syncTime;
        }
        //同步时间
        String beginTime = syncTime.getData().get(0).getAccessTime();
        beginTime = DateTimeUtil.stringMinusOrPlusSecond(beginTime, 1);
        //当前时间
        String currentDatetime = DateTimeUtil.getCurrentDatetime();
        String endTime;
        JSONObject param = new JSONObject();
        param.put("appkey", appKey);
        param.put("opUserUuid", defaultUuid);
        do {
            endTime = DateTimeUtil.stringMinusOrPlusDay(beginTime, 1);
            if (endTime.compareTo(currentDatetime) > 0) {
                endTime = currentDatetime;
            }
            param.put("footfallStartTime", beginTime);
            param.put("footfallEndTime", endTime);
            trafficPointInfoList.stream().forEach(trafficPointInfo -> {
                param.put("time", System.currentTimeMillis());
                param.put("cameraUuid", trafficPointInfo.getDeviceNo());
                String url = SignUtil.postBuildToken(host, Hk87Constant.INTERFACE_GET_FOOTFALL_DATA, param, secret);
                Result result;
                try {
                    result = HttpClientUtil.POST.applicationJson(url, null, param.toJSONString(), null);
                } catch (Exception e) {
                    log.error("请求第三方监控客流数据接口异常:{}", e.getMessage());
                    return;
                }
                int status = result.getStatus();
                String content = result.getContent();
                if (HttpStatus.SC_OK != status) {
                    log.error("请求第三方监控客流数据接口失败:{}", content);
                    return;
                }
                JSONObject object = JSONObject.parseObject(content);
                Integer errorCode = object.getInteger("errorCode");
                //响应结果失败
                if (!Hk87Constant.RESPONSE_SUCCESS_CODE.equals(errorCode)) {
                    log.error("");
                    return;
                }
                JSONArray data = object.getJSONArray("data");
                if (CollectionUtils.isEmpty(data)) {
                    log.info("");
                    return;
                }
                data.stream().map(obj -> JSON.parseObject(JSON.toJSONString(obj), TrafficRecordThird.class)).forEach(trafficRecordThird -> {
                    TrafficRecord trafficRecord = new TrafficRecord();
                });
            });
        } while (endTime.compareTo(currentDatetime) > 0);
        return null;
    }

    /**
     * 客流量天数据
     *
     * @return
     */
    @Override
    public ServerResponse dataDaySync() {
        if (StringUtils.isEmpty(defaultUuid)) {
            ServerResponse serverResponse = commonUtil.getDefaultUserUuid(host, appKey, secret);
            if (!serverResponse.isSuccess()) {
                return serverResponse;
            }
            defaultUuid = serverResponse.getData().toString();
        }



        return null;
    }

    /**
     * 获取客流监控点列表
     *
     * @return
     */
    private ServerResponse listDeviceInfo() {
        SearchRequest searchRequest = new SearchRequest();
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(QueryBuilders.termQuery("is_deleted", "N"));
        searchSourceBuilder.size(1000);
        searchRequest.source(searchSourceBuilder);
        try {
            return elasticsearchUtil.queryByTerm(searchRequest, TrafficPointInfo.class);
        } catch (Exception e) {
            log.error("获取客流监控点列表异常,errorMessage:{}", e.getMessage());
            return ServerResponse.createByErrorMessage("获取客流监控点列表失败");
        }
    }

    /**
     * 获取原始数据同步时间
     *
     * @return
     */
    private ServerResponse<List<TrafficRecord>> getSyncTime() {
        SearchRequest searchRequest = new SearchRequest();
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        //按指定字段降序排序
        searchSourceBuilder.sort(new FieldSortBuilder("access_time").order(SortOrder.DESC));
        searchSourceBuilder.size(1);
        searchRequest.source(searchSourceBuilder);
        try {
            return elasticsearchUtil.queryByTerm(searchRequest, TrafficRecord.class);
        } catch (IOException e) {
            log.error("获取客流同步时间异常:{}", e.getMessage());
            return ServerResponse.createByErrorMessage("获取客流同步时间失败");
        }
    }

    /**
     * 获取原始数据同步时间
     *
     * @return
     */
    private ServerResponse<List<TrafficRecord>> getDaySyncTime() {
        SearchRequest searchRequest = new SearchRequest();
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        //按指定字段降序排序
        searchSourceBuilder.sort(new FieldSortBuilder("access_time").order(SortOrder.DESC));
        searchSourceBuilder.size(1);
        searchRequest.source(searchSourceBuilder);
        try {
            return elasticsearchUtil.queryByTerm(searchRequest, TrafficRecord.class);
        } catch (IOException e) {
            log.error("获取客流同步时间异常:{}", e.getMessage());
            return ServerResponse.createByErrorMessage("获取客流同步时间失败");
        }
    }
}
