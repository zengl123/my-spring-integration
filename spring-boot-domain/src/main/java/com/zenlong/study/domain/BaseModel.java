package com.zenlong.study.domain;

import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;


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
    @ApiModelProperty(value = "主键", hidden = true)
    private String id;
    @ApiModelProperty(value = "创建者", hidden = true)
    private String creator = "SYSTEM";
    @ApiModelProperty(value = "修改者", hidden = true)
    private String modifier = "SYSTEM";
    @JSONField(name = "create_time")
    @ApiModelProperty(value = "创建时间", hidden = true)
    private Date createTime;
    @JSONField(name = "modified_time")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "修改时间", hidden = true)
    private Date modifiedTime;
    @ApiModelProperty(value = "是否已删除", hidden = true)
    @JSONField(name = "is_deleted")
    private String isDeleted = "N";
}
