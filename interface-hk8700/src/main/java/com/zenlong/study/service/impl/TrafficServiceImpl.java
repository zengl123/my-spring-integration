package com.zenlong.study.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zenlong.study.common.ServerResponse;
import com.zenlong.study.common.excpetion.CusException;
import com.zenlong.study.common.excpetion.ExceptionUtil;
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

import javax.lang.model.SourceVersion;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

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
    @Value("${tdp.hk.url}")
    private String host;
    @Value("${tdp.hk.key}")
    private String appKey;
    @Value("${tdp.hk.secret}")
    private String secret;
    @Value("${tdp.hk.default_current_time_sync_time}")
    private String dataSyncTime;
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
    public ServerResponse dataCurrentTimeSync() {
        if (StringUtils.isEmpty(defaultUuid)) {
            ServerResponse serverResponse = commonUtil.getDefaultUserUuid(host, appKey, secret);
            if (!serverResponse.isSuccess()) {
                return serverResponse;
            }
            defaultUuid = serverResponse.getData().toString();
        }
        ServerResponse<List<TrafficPointInfo>> serverResponse = listDeviceInfo();
        if (!serverResponse.isSuccess()) {
            log.error("获取客流监控点列表数据失败:{}", serverResponse.getMessage());
            serverResponse.setMessage("获取客流监控点列表数据失败");
            return serverResponse;
        }
        if (serverResponse.getData().size() <= 0) {
            serverResponse.setMessage("客流监控点列表数据不存在");
            return serverResponse;
        }
        //客流监控点集合
        List<TrafficPointInfo> trafficPointInfoList = serverResponse.getData();
        ServerResponse<String> syncTime = getSyncTime();
        if (!syncTime.isSuccess()) {
            log.error("获取原始数据同步时间失败:{}", syncTime.getMessage());
            syncTime.setMessage("获取原始数据同步时间失败");
            return syncTime;
        }
        //同步时间
        String beginTime = syncTime.getData();
        beginTime = DateTimeUtil.stringMinusOrPlusSecond(beginTime, 1);
        //当前时间
        String currentDatetime = DateTimeUtil.getCurrentDatetime();
        String endTime;
        JSONObject param = new JSONObject();
        param.put("appkey", appKey);
        param.put("opUserUuid", defaultUuid);
        try {
            do {
                endTime = DateTimeUtil.stringMinusOrPlusDay(beginTime, 1);
                if (endTime.compareTo(currentDatetime) > 0) {
                    endTime = currentDatetime;
                }
                param.put("footfallStartTime", DateTimeUtil.stringToMilli(beginTime));
                param.put("footfallEndTime", DateTimeUtil.stringToMilli(endTime));
                Integer pageNo = Hk87Constant.PAGE_NO;
                String finalBeginTime = beginTime;
                String finalEndTime = endTime;
                //收集每次请求时间段内所有监控客流数据记录
                List<TrafficRecord> list = new ArrayList<>();
                trafficPointInfoList.stream().forEach(trafficPointInfo -> {
                    param.put("time", System.currentTimeMillis());
                    param.put("cameraUuid", trafficPointInfo.getDeviceNo());
                    String url = SignUtil.postBuildToken(host, Hk87Constant.INTERFACE_GET_FOOTFALL_DATA, param, secret);
                    Result result;
                    try {
                        result = HttpClientUtil.POST.applicationJson(url, null, param.toJSONString(), null);
                    } catch (Exception e) {
                        log.error("请求第三方监控客流数据接口异常:{}", ExceptionUtil.hand(e));
                        throw new CusException("请求第三方监控客流数据接口异常");
                    }
                    int status = result.getStatus();
                    String content = result.getContent();
                    if (HttpStatus.SC_OK != status) {
                        log.error("请求第三方监控客流数据接口失败:{}", content);
                        throw new CusException("请求第三方监控客流数据接口失败");
                    }
                    JSONObject object = JSONObject.parseObject(content);
                    Integer errorCode = object.getInteger("errorCode");
                    //响应结果失败
                    if (!Hk87Constant.RESPONSE_SUCCESS_CODE.equals(errorCode)) {
                        String message = object.getString("errorMessage");
                        log.error("第三方监控客流数据接口响应失败:{}", message);
                        throw new CusException("第三方监控客流数据接口响应失败");
                    }
                    JSONArray data = object.getJSONArray("data");
                    if (CollectionUtils.isEmpty(data)) {
                        log.info("{}:{}-{}该时间段内未产生客流数据", trafficPointInfo.getDeviceName(), finalBeginTime, finalEndTime);
                        return;
                    }
                    try {
                        data.stream().map(obj -> JSON.parseObject(JSON.toJSONString(obj), TrafficRecordThird.class)).forEach(trafficRecordThird -> {
                            TrafficRecord trafficRecord = new TrafficRecord();
                            trafficRecord.setDeviceNo(trafficPointInfo.getDeviceNo());
                            trafficRecord.setDeviceName(trafficPointInfo.getDeviceName());
                            trafficRecord.setDeviceNoId(trafficPointInfo.getId());
                            trafficRecord.setEntryNum(trafficRecordThird.getPassengersIn());
                            trafficRecord.setExportNum(trafficRecordThird.getPassengersOut());
                            trafficRecord.setAccessTime(trafficRecordThird.getFootfallStartTime());
                            list.add(trafficRecord);
                        });
                    } catch (Exception e) {
                        log.error("第三方监控客流数据接口响应数据解析异常:{}", ExceptionUtil.hand(e));
                        throw new CusException("第三方监控客流数据接口响应数据解析异常");
                    }
                });
                //获取数据存储
                if (CollectionUtils.isNotEmpty(list)) {
                    ServerResponse response = elasticsearchUtil.bulk(TrafficRecord.INDEX, TrafficRecord.TYPE, list);
                    if (response.isSuccess()) {
                        log.info("{}-{}时间段内第三方监控客流数据接口响应数据存储成功", beginTime, endTime);
                        beginTime = endTime;
                    } else {
                        log.error("第三方监控客流数据接口响应数据存储失败:{}", response.getMessage());
                        throw new CusException("第三方监控客流数据接口响应数据存储失败");
                    }
                }
            } while (endTime.compareTo(currentDatetime) > 0);
            return ServerResponse.createBySuccess();
        } catch (Exception e) {
            log.error("客流原始数据同步失败:{}", ExceptionUtil.hand(e));
            return ServerResponse.createByErrorMessage("客流原始数据同步失败");
        }
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
        searchSourceBuilder.query(QueryBuilders.termQuery("is_deleted.keyword", "N"));
        searchSourceBuilder.size(1000);
        searchRequest.source(searchSourceBuilder);
        return elasticsearchUtil.queryByTerm(searchRequest, TrafficPointInfo.class);
    }

    /**
     * 获取原始数据同步时间
     *
     * @return
     */
    private ServerResponse getSyncTime() {
        SearchRequest searchRequest = new SearchRequest();
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        //按指定字段降序排序
        searchSourceBuilder.sort(new FieldSortBuilder("access_time.keyword").order(SortOrder.DESC));
        searchSourceBuilder.size(1);
        searchRequest.source(searchSourceBuilder);
        ServerResponse<List<TrafficRecord>> serverResponse = elasticsearchUtil.queryByTerm(searchRequest, TrafficRecord.class);
        //针对开始无index特殊处理
        if (!serverResponse.isSuccess()) {
            String message = serverResponse.getMessage();
            String no = "No mapping found for [access_time.keyword] in order to sort on";
            if (message.contains(no)) {
                return ServerResponse.createBySuccess(dataSyncTime);
            } else {
                return ServerResponse.createByErrorMessage(serverResponse.getMessage());
            }
        }
        return ServerResponse.createBySuccess(serverResponse.getData().get(0).getAccessTime());
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
        return elasticsearchUtil.queryByTerm(searchRequest, TrafficRecord.class);
    }
}
