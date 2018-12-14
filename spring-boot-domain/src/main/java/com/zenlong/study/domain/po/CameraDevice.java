package com.zenlong.study.domain.po;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.zenlong.study.domain.BaseModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 描述:
 * 项目名:my-spring-integration
 *
 * @Author:ZENLIN
 * @Created 2018/11/26  16:15.
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CameraDevice extends BaseModel {
    @ApiModelProperty(name = "组织编号")
    private String groupNo;
    @ApiModelProperty(name = "监控点编号")
    private String deviceNo;
    @ApiModelProperty(name = "监控点名称")
    private String deviceName;
    @ApiModelProperty(name = "监控点ip地址")
    private String ip;
    @ApiModelProperty(name = "监控点端口号")
    private Integer port;
    @ApiModelProperty(name = "监控点通道号")
    private Integer channel;
    @ApiModelProperty(name = "监控点是否删除", example = "0", notes = "0-正常;负数-删除")
    private Integer status;
    @ApiModelProperty(name = "监控点是否在线", example = "0", notes = "0-不在线;1-在线")
    private Integer onlineStatus;
    @ApiModelProperty(name = "摄像机类型", example = "0", notes = "0-枪机;1-半球;2-快球;3-云台 ")
    private Integer cameraType;
    @ApiModelProperty(name = "登陆用户名")
    private String userName;
    @ApiModelProperty(name = "登陆密码")
    private String password;
    @ApiModelProperty(name = "回放参数")
    private String playback;
    @ApiModelProperty(name = "预览参数")
    private String preview;
}
