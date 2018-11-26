package com.zenlong.study.domain.po;

import com.alibaba.fastjson.annotation.JSONField;
import com.zenlong.study.domain.BaseModel;
import lombok.Data;

/**
 * 描述:
 * 项目名:my-spring-integration
 *
 * @Author:ZENLIN
 * @Created 2018/11/26  14:43.
 */
@Data
public class CameraGroup extends BaseModel{
    @JSONField(name = "group_no")
    private String groupNo;
    @JSONField(name = "group_name")
    private String groupName;
    @JSONField(name = "series_no")
    private String seriesNo;
    @JSONField(name = "series_name")
    private String seriesName;
}
