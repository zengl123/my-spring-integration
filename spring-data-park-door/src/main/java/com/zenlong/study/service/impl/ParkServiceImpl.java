package com.zenlong.study.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zenlong.study.common.ServerResponse;
import com.zenlong.study.common.httpclient.HttpClientUtil;
import com.zenlong.study.common.httpclient.HttpClientUtil.Result;
import com.zenlong.study.common.utils.CheckUtil;
import com.zenlong.study.common.utils.DateTimeUtil;
import com.zenlong.study.constant.InterfaceConstant;
import com.zenlong.study.domain.ParkEntryRecordThird;
import com.zenlong.study.domain.po.ParkExportRecord;
import com.zenlong.study.domain.po.ParkInfo;
import com.zenlong.study.es.utils.ElasticsearchUtil;
import com.zenlong.study.service.IParkService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpStatus;
import org.elasticsearch.action.search.SearchRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * 描述:停车场接口实现
 * 项目名:my-spring-integration
 *
 * @Author:ZENLIN
 * @Created 2018/10/23  16:11.
 */
@Slf4j
@Service
public class ParkServiceImpl implements IParkService {
    @Value("${tdp.park.door-url}")
    private String host;
    @Value("${tdp.park.door-park-nos}")
    private String parkNos;
    @Value("${tdp.park.door-park-names}")
    private String parkNames;

    @Autowired
    private ElasticsearchUtil client;

    /**
     * 获取停车场信息列表
     *
     * @return
     */
    @Override
    public ServerResponse<List<ParkInfo>> listParkInfo() {
        String url = host + InterfaceConstant.GET_PARK_INFO;
        //停车场编号集合
        List<String> listParkNo = Arrays.asList(parkNos.split(","));
        //停车场名称集合
        List<String> listParkName = Arrays.asList(parkNames.split(","));
        final int[] i = {0};
        List<ParkInfo> list = new ArrayList<>();
        listParkNo.stream().forEach(parkNo -> {
            String requestUrl = url + parkNo.trim();
            Result invoke;
            try {
                invoke = HttpClientUtil.GET.invoke(url, null, null);
            } catch (Exception e) {
                log.error("请求第三方获取停车场信息列表接口异常,requestUrl:{},errorMessage:{}", requestUrl, e.getMessage());
                return;
            }
            int status = invoke.getStatus();
            String content = invoke.getContent();
            if (HttpStatus.SC_OK != status) {
                //请求响应失败
                log.error("请求第三方获取停车场信息列表接口失败,errorMessage:{}", content);
                return;
            }
            //请求响应成功
            Integer integer;
            try {
                JSONObject object = JSONObject.parseObject(content);
                integer = object.getJSONObject("head").getInteger("status");
                //响应结果错误
                if (!InterfaceConstant.RESPONSE_SUCCESS_CODE.equals(integer)) {
                    String message = object.getJSONObject("head").getString("message");
                    log.error("第三方获取停车场信息列表接口数据响应结果错误,errorMessage:{}", message);
                    return;
                }
                JSONObject body = object.getJSONObject("body");
                //停车场编号
                parkNo = body.getString("parkId");
                //总车位
                JSONObject totalJson = body.getJSONObject("total");
                //vip车位 (混合车位时,舍弃改值)
                Integer fixed = totalJson.getInteger("fixed");
                //临时车位 (混合车位时,舍弃改值)
                Integer temporary = totalJson.getInteger("temporary");
                //车位数合计(混合车位时使用)
                Integer total = totalJson.getInteger("total");
                //剩余空车位
                JSONObject idleJson = body.getJSONObject("idle");
                Integer fixedIdle = idleJson.getInteger("fixed");
                Integer temporaryIdle = idleJson.getInteger("temporary");
                Integer totalIdle = idleJson.getInteger("total");
                //已使用车位
                Integer totalUsed = total - totalIdle;
                String parkName = listParkName.get(i[0]);
                i[0]++;
                ParkInfo parkInfo = new ParkInfo();
                parkInfo.setParkNo(parkNo);
                parkInfo.setParkName(parkName);
                parkInfo.setParkTotalNum(total);
                parkInfo.setParkUsedNum(totalUsed);
                parkInfo.setParkRemainderNum(totalIdle);
                list.add(parkInfo);
            } catch (Exception e) {
                log.error("第三方获取停车场信息列表接口数据响应结果解析异常,errorMessage{}", e.getMessage());
                return;
            }
        });
        if (CollectionUtils.isNotEmpty(list)) {
            return ServerResponse.createBySuccess(list);
        } else {
            return ServerResponse.createByErrorMessage("获取停车场信息列表失败");
        }
    }

    /**
     * 出场记录
     *
     * @return
     */
    @Override
    public ServerResponse<List<ParkExportRecord>> listParkExport() {
        String url = host + InterfaceConstant.GET_PARK_RECORD_OUT;
        //停车场编号集合
        List<String> listParkNo = Arrays.asList(parkNos.split(","));
        //数据请求开始时间
        String beginTime;
        try {
            beginTime = getSyncTime();
        } catch (Exception e) {
            log.error("进场记录同步开始时间获取异常:{}", e.getMessage());
            return ServerResponse.createByErrorMessage("同步进场记录失败");
        }
        //数据请求结束时间
        String endTime = DateTimeUtil.getCurrentDatetime();
        List<ParkExportRecord> list = new ArrayList<>();
        listParkNo.stream().forEach(parkNo -> {
            //默认
            Integer pageNo = InterfaceConstant.PAGE_NO;
            String requestUrl = url + parkNo.trim() + "?startData=" + beginTime + "&endData=" + endTime + "&pageSize=" + InterfaceConstant.PAGE_SIZE + "&pageNo=" + pageNo;
            Result invoke;
            try {
                invoke = HttpClientUtil.GET.invoke(url, null, null);
            } catch (Exception e) {
                log.error("请求第三方进场记录接口异常,requestUrl:{},errorMessage:{}", requestUrl, e.getMessage());
                return;
            }
            int status = invoke.getStatus();
            String content = invoke.getContent();
            if (HttpStatus.SC_OK != status) {
                //请求响应失败
                log.error("请求第三方进场记录接口失败,errorMessage:{}", content);
                return;
            }
            //请求响应成功
            Integer integer;
            try {
                JSONObject object = JSONObject.parseObject(content);
                integer = object.getJSONObject("head").getInteger("status");
                //响应结果错误
                if (!InterfaceConstant.RESPONSE_SUCCESS_CODE.equals(integer)) {
                    String message = object.getJSONObject("head").getString("message");
                    log.error("第三方进场记录接口数据响应结果错误,errorMessage:{}", message);
                    return;
                }
                Integer rows = object.getJSONObject("head").getInteger("rows");
                //数据不存在
                if (rows <= 0) {
                    log.info("{}-{}时间段内未产生出场记录", beginTime, endTime);
                    return;
                }
                JSONArray body = object.getJSONArray("body");
                //数据转换及过滤无效数据(暂时针对车牌号)
                body.stream().map(obj -> JSON.parseObject(JSON.toJSONString(object), ParkEntryRecordThird.class)).collect(Collectors.toList()).stream().filter(parkRecordPoFilter).forEach(parkEntryRecordThird -> {
                    //封装
                    String carNo = parkEntryRecordThird.getCarNo();
                    String cardNo = parkEntryRecordThird.getCardNo();
                    String entryName = parkEntryRecordThird.getEntryName();
                    String entryOptName = parkEntryRecordThird.getEntryOptName();
                    String entryTime = parkEntryRecordThird.getEntryTime();
                    String exitName = parkEntryRecordThird.getExitName();
                    ParkExportRecord parkExportRecord = new ParkExportRecord();
                });

            } catch (Exception e) {
                log.error("第三方进场记录接口数据响应结果解析异常,errorMessage{}", e.getMessage());
                return;
            }
            if (CollectionUtils.isNotEmpty(list)) {
                //存储
            } else {

            }
        });
        return null;
    }

    /**
     * 获取每次同步时间
     *
     * @return
     * @throws IOException
     */
    public String getSyncTime() throws IOException {
        SearchRequest searchRequest = new SearchRequest();
        ServerResponse<List<ParkExportRecord>> listServerResponse = client.queryByTermNoId(searchRequest, ParkExportRecord.class);
        String entryTime = listServerResponse.getData().get(0).getEntryTime();
        return entryTime;
    }

    /**
     * 记录过滤器
     */
    Predicate<ParkEntryRecordThird> parkRecordPoFilter = (parkEntryRecordThird) -> (StringUtils.isNotEmpty(parkEntryRecordThird.getCardNo()) && CheckUtil.checkPlateNumber(parkEntryRecordThird.getCardNo()));
}
