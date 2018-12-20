package com.zenlong.study.domain.po;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;

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
@Document(indexName = "index_gps_record", type = "type_gps_record")
public class GpsRecord implements Serializable {
    public final static transient String INDEX = "index_gps_record";
    public final static transient String TYPE = "type_gps_record";
    /**
     * 主键id
     */
    @Id
    private String id;
    /**
     * 设备编号
     */
    //@SerializedName(value = "device_no")
    @Field
    private String deviceNo;
    /**
     * 设备名称
     */
    //@SerializedName(value = "device_name")
    @Field
    private String deviceName;
    /**
     * 经度
     */
    @Field
    private String longitude;
    /**
     * 纬度
     */
    @Field
    private String latitude;
    /**
     * 数据产生时间(String展示Long计算)
     */
    //@SerializedName(value = "gps_time")
    @Field
    private String gpsTime;
    //@SerializedName(value = "gps_time_long")
    @Field
    private Long gpsTimeLong;
    /**
     * 数据写入时间
     */
    @Field
    private String createTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    @Field
    private int speed;
    /**
     * 方向
     */
    //@SerializedName(value = "direction_no")
    @Field
    private int directionNo;
    /**
     * 方向名称
     */
    @Transient
    private String directionName;
    /**
     * 海拔
     */
    @Field
    private int elevation;
}