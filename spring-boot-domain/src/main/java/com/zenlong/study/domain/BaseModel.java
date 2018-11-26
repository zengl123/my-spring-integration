package com.zenlong.study.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @Project my-spring-integration
 * @Package com.zenlong.study.domain
 * @ClassName BaseModel
 * @Author ZENLIN
 * @Date 2018-09-11 22:03
 * @Description TODO
 * @Version
 * @Modified By
 */
@Data
public class BaseModel implements Serializable {
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(value = "主键")
    private String id;
    @ApiModelProperty(value = "创建者", hidden = true)
    private String creator = "SYSTEM";
    @ApiModelProperty(value = "修改者", hidden = true)
    private String modifier = "SYSTEM";
    @ApiModelProperty(value = "创建时间", hidden = true)
    private String createTime;
    @ApiModelProperty(value = "修改时间", hidden = true)
    private String modifiedTime;
    @ApiModelProperty(value = "是否已删除", hidden = true)
    private String isDeleted = "N";
}
