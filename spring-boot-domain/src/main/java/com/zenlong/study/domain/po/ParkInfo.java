package com.zenlong.study.domain.po;

import com.alibaba.fastjson.annotation.JSONField;
import com.zenlong.study.domain.BaseModel;
import lombok.Data;

/**
 * 描述:停车场信息实体
 * 项目名:my-spring-integration
 *
 * @Author:ZENLIN
 * @Created 2018/10/23  15:42.
 */
@Data
public class ParkInfo extends BaseModel {
    /**
     * 停车场编号
     */
    @JSONField(name = "park_no")
    private String parkNo;
    /**
     * 停车场名称
     */
    @JSONField(name = "park_name")
    private String parkName;
    /**
     * 车位总数
     */
    @JSONField(name = "park_total_num")
    private Integer parkTotalNum;
    /**
     * 已使用车位数
     */
    @JSONField(name = "park_used_num")
    private Integer parkUsedNum;
    /**
     * 剩余车位数
     */
    @JSONField(name = "park_remainder_num")
    private Integer parkRemainderNum;
}
