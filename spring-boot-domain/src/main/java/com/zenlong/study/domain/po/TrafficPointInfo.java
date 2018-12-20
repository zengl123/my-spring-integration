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
 * @Created 2018/12/20  15:39.
 */
@Data
public class TrafficPointInfo extends BaseModel {
    public final static transient String INDEX = "index_traffic_point_info";
    public final static transient String TYPE = "type_traffic_point_info";
    @Id
    private String id;
    @JSONField(name = "device_no")
    private String deviceNo;
    @JSONField(name = "device_name")
    private String deviceName;
    private String ip;
    private Integer chanel;
    private Integer peak;
}
