package com.zenlong.study.domain.po;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 描述:
 * 项目名:my-spring-integration
 *
 * @Author:ZENLIN
 * @Created 2018/11/27  9:29.
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GpsRecord implements Serializable {
    private transient String id;
    public static transient String index = "indexgpsrecord";
    public static transient String type = "gps_record";
    /**
     * 设备编号
     */
    @JSONField(name = "device_no")
    private String deviceNo;
    /**
     * 设备名称
     */
    @JSONField(name = "device_name")
    private String deviceName;
    /**
     * 经度
     */
    private String longitude;
    /**
     * 纬度
     */
    private String latitude;
    @JSONField(name = "gps_time")
    /**
     * 数据产生时间
     */
    private String gpsTime;
    /**
     * 数据写入时间
     */
    @JSONField(name = "create_time")
    private String createTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    private int speed;
    /**
     * 方向
     */
    private int directionNo;
    /**
     * 方向名称
     */
    private String directionName;
    /**
     * 海拔
     */
    private int elevation;
}