package com.zenlong.study.entity;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

/**
 * 描述:
 * 项目名:my-spring-integration
 *
 * @Author:ZENLIN
 * @Created 2018/12/11  11:00.
 */
@Data
public class ThirdCameraGroup {
    @JSONField(name = "i_id")
    private String uuid;
    /**
     * 上级组织id
     */
    @JSONField(name = "i_parent_id")
    private String parentNo;
    /**
     * 组织名称
     */
    @JSONField(name = "c_org_name")
    private String groupName;
    /**
     * 组织内部编码
     */
    @JSONField(name = "c_index_code")
    private String groupNo;
    /**
     * 删除状态
     */
    @JSONField(name = "i_status")
    private String status;
}
