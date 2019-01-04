package com.zenlong.study.domain.po;

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
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class GpsRecord implements Serializable {
    public final static transient String INDEX = "index_gps_record";
    public final static transient String TYPE = "type_gps_record";
    /**
     * 主键id
     */
    private String id;
    /**
     * 设备编号
     */
    //@SerializedName(value = "device_no")
    private String deviceNo;
    /**
     * 设备名称
     */
    //@SerializedName(value = "device_name")
    private String deviceName;
    /**
     * 经度
     */
    private String longitude;
    /**
     * 纬度
     */
    private String latitude;
    /**
     * 数据产生时间(String展示Long计算)
     */
    //@SerializedName(value = "gps_time")
    private String gpsTime;
    //@SerializedName(value = "gps_time_long")
    private Long gpsTimeLong;
    /**
     * 数据写入时间
     */
    private String createTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    private int speed;
    /**
     * 方向
     */
    //@SerializedName(value = "direction_no")
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