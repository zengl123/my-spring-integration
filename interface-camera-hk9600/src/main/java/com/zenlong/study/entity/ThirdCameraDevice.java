package com.zenlong.study.entity;

import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 描述:
 * 项目名:my-spring-integration
 *
 * @Author:ZENLIN
 * @Created 2018/12/11  11:00.
 */
@Data
public class ThirdCameraDevice {
    /**
     * 组织编号
     */
    @JSONField(name = "c_org_index_code")
    private String groupNo;
    @JSONField(name = "c_index_code")
    private String deviceNo;
    @JSONField(name = "c_name")
    private String deviceName;
    @JSONField(name = "c_device_ip")
    private String ip;
    @JSONField(name = "i_device_port")
    private Integer port;
    @JSONField(name = "i_is_online")
    private Integer onlineStatus;
    @JSONField(name = "i_status")
    private Integer status;
    @JSONField(name = "i_channel_no")
    private Integer channel;
    @JSONField(name = "i_camera_type")
    private Integer cameraType;
}
