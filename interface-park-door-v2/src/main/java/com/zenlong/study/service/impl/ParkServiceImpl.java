package com.zenlong.study.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zenlong.study.common.ServerResponse;
import com.zenlong.study.common.excpetion.ExceptionUtil;
import com.zenlong.study.common.httpclient.HttpClientUtil;
import com.zenlong.study.common.httpclient.HttpClientUtil.Result;
import com.zenlong.study.common.utils.CheckUtil;
import com.zenlong.study.common.utils.DateTimeUtil;
import com.zenlong.study.constant.InterfaceConstant;
import com.zenlong.study.domain.ParkEntryRecordThird;
import com.zenlong.study.domain.po.ParkExportRecord;
import com.zenlong.study.domain.po.ParkDeviceInfo;
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
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
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
    @Value("${tdp.park.door.url}")
    private String host;
    @Value("${tdp.park.door.nos}")
    private String parkNos;
    @Value("${tdp.park.door.names}")
    private String parkNames;

    @Autowired
    private ElasticsearchUtil client;

    /**
     * 获取停车场信息列表
     *
     * @return
     */
    @Override
    public ServerResponse<List<ParkDeviceInfo>> listParkInfo() {
        String url = host + InterfaceConstant.GET_PARK_INFO;
        //停车场编号集合
        List<String> listParkNo = Arrays.asList(parkNos.split(","));
        //停车场名称集合
        List<String> listParkName = Arrays.asList(parkNames.split(","));
        final int[] i = {0};
        List<ParkDeviceInfo> list = new ArrayList<>();
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
                ParkDeviceInfo parkInfo = new ParkDeviceInfo();
                parkInfo.setParkNo(parkNo);
                parkInfo.setParkName(parkName);
                parkInfo.setParkTotalNum(total);
                parkInfo.setParkUsedNum(totalUsed);
                parkInfo.setParkRemainderNum(totalIdle);
                list.add(parkInfo);
            } catch (Exception e) {
                log.error("第三方获取停车场信息列表接口数据响应结果解析异常:{}", ExceptionUtil.hand(e));
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
        final String[] beginTime = new String[1];
        try {
            beginTime[0] = getSyncTime();
        } catch (Exception e) {
            log.error("进场记录同步开始时间获取异常:{}", e.getMessage());
            return ServerResponse.createByErrorMessage("同步进场记录失败");
        }
        //当前时间
        String currentDatetime = DateTimeUtil.getCurrentDatetime();
        listParkNo.stream().forEach(parkNo -> {
            //数据请求结束时间
            String endTime;
            do {
                endTime = DateTimeUtil.stringMinusOrPlusDay(beginTime[0], 1);
                if (endTime.compareTo(currentDatetime) > 0) {
                    endTime = currentDatetime;
                }
                //默认
                Integer pageNo = InterfaceConstant.PAGE_NO;
                List<ParkExportRecord> list = new ArrayList<>();
                do {
                    String requestUrl = url + parkNo.trim() + "?startData=" + beginTime[0] + "&endData=" + endTime + "&pageSize=" + InterfaceConstant.PAGE_SIZE + "&pageNo=" + pageNo;
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
                            log.info("{}-{}时间段内未产生出场记录", beginTime[0], endTime);
                            return;
                        }
                        JSONArray body = object.getJSONArray("body");
                        //数据转换及过滤无效数据(暂时针对车牌号)
                        List<ParkExportRecord> collect = body.stream().map(obj -> JSON.parseObject(JSON.toJSONString(object), ParkEntryRecordThird.class)).filter(parkRecordPoFilter).collect(Collectors.toList()).stream().map(parkEntryRecordThird -> conversion(parkEntryRecordThird)).collect(Collectors.toList());
                        list.addAll(collect);
                        if (InterfaceConstant.PAGE_NO.equals(pageNo)) {
                            pageNo = rows / InterfaceConstant.PAGE_SIZE + 1;
                        } else {
                            pageNo--;
                        }
                    } catch (Exception e) {
                        log.error("第三方进场记录接口数据响应结果解析异常,errorMessage{}", e.getMessage());
                        return;
                    }
                    //每次请求时间段内的数据拿完之后退出
                } while (pageNo > InterfaceConstant.PAGE_NO);
                System.out.println(JSON.toJSON(list));
                beginTime[0] = endTime;
                /*ServerResponse bulk = client.bulk(ParkExportRecord.index, ParkExportRecord.type, list);
                if (bulk.isSuccess()) {
                    log.info("第三方进场记录数据存储成功");
                    beginTime[0] = endTime;
                } else {
                    log.error("第三方进场记录数据存储失败");
                    break;
                }*/
                //大于当前时间退出
            } while (endTime.compareTo(currentDatetime) > 0);
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

    public ParkExportRecord conversion(ParkEntryRecordThird parkEntryRecordThird) {
        //封装
        String carNo = parkEntryRecordThird.getCarNo();
        String cardNo = parkEntryRecordThird.getCardNo();
        String entryName = parkEntryRecordThird.getEntryName();
        String entryOptName = parkEntryRecordThird.getEntryOptName();
        String entryTime = parkEntryRecordThird.getEntryTime();
        String exitName = parkEntryRecordThird.getExitName();
        String exitOptName = parkEntryRecordThird.getExitOptName();
        String exitTime = parkEntryRecordThird.getExitTime();
        String ownerName = parkEntryRecordThird.getOwnerName();
        List<ParkEntryRecordThird.PaymentInfo> paymentInfo = parkEntryRecordThird.getPaymentInfo();
        ParkExportRecord parkExportRecord = new ParkExportRecord();
        parkExportRecord.setCardNo(cardNo);
        parkExportRecord.setPlateNumber(carNo);
        parkExportRecord.setEntryName(entryName);
        parkExportRecord.setEntryOptName(entryOptName);
        parkExportRecord.setEntryTime(entryTime);
        parkExportRecord.setExportName(exitName);
        parkExportRecord.setExportOptName(exitOptName);
        parkExportRecord.setExportTime(exitTime);
        parkExportRecord.setOwnerName(ownerName);
        if (CollectionUtils.isNotEmpty(paymentInfo)) {
            String accountCharge = paymentInfo.get(0).getAccountCharge();
            accountCharge = StringUtils.isEmpty(accountCharge) ? "0" : accountCharge;
            parkExportRecord.setReceivableAmount(new BigDecimal(accountCharge));
            String payCharge = paymentInfo.get(0).getPayCharge();
            payCharge = StringUtils.isEmpty(payCharge) ? "0" : payCharge;
            parkExportRecord.setPaidAmount(new BigDecimal(payCharge));
            String disAmount = paymentInfo.get(0).getDisAmount();
            disAmount = StringUtils.isEmpty(disAmount) ? "0" : disAmount;
            parkExportRecord.setDiscountAmount(new BigDecimal(disAmount));
        }
        return parkExportRecord;
    }
}
