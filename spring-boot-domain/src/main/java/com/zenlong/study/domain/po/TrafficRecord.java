package com.zenlong.study.domain.po;

import com.alibaba.fastjson.annotation.JSONField;
import com.zenlong.study.domain.BaseModel;
import lombok.Data;
import org.springframework.data.annotation.Id;

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
    @Id
    private String id;
    @JSONField(name = "device_no_id")
    private String deviceNoId;
    @JSONField(name = "device_no")
    private String deviceNo;
    @JSONField(name = "device_name")
    private String deviceName;
    @JSONField(name = "entry_num")
    private Integer entryNum;
    @JSONField(name = "export_num")
    private Integer exportNum;
    @JSONField(name = "total_entry")
    private Integer totalEntry;
    @JSONField(name = "total_export")
    private Integer totalExport;
    @JSONField(name = "access_time")
    private String accessTime;
}