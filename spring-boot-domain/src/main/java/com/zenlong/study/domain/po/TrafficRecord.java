package com.zenlong.study.domain.po;

import com.zenlong.study.domain.BaseModel;
import lombok.Data;

/**
 * 描述:
 * 项目名:my-spring-integration
 *
 * @Author:ZENLIN
 * @Created 2018/12/20  16:15.
 */
@Data
public class TrafficRecord extends BaseModel {
    public final static transient String INDEX = "index_traffic_record";
    public final static transient String TYPE = "type_traffic_record";
    private String deviceNoId;
    private String deviceNo;
    private String deviceName;
    private Integer entryNum;
    private Integer exportNum;
    private Integer totalEntry;
    private Integer totalExport;
    private String accessTime;
}