package com.zenlong.study.domain.po;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.zenlong.study.domain.BaseModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 描述:
 * 项目名:my-spring-integration
 *
 * @Author:ZENLIN
 * @Created 2018/11/26  14:43.
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CameraGroup extends BaseModel {
    private String uuid;
    @JSONField(name = "group_no")
    @ApiModelProperty(name = "组织编码")
    private String groupNo;
    @ApiModelProperty(name = "组织名称")
    @JSONField(name = "group_name")
    private String groupName;
    @JSONField(name = "parent_no")
    @ApiModelProperty(name = "上级组织id")
    private String parentNo;
    @JSONField(name = "series_no")
    private String seriesNo;
    @JSONField(name = "series_name")
    private transient String seriesName;
    @ApiModelProperty(name = "删除状态", notes = "0-正常,负i_id值-删除")
    private int status;
    private transient String statusName;
}
