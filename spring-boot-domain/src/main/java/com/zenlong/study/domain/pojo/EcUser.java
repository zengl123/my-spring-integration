package com.zenlong.study.domain.pojo;

import com.zenlong.study.domain.BaseModel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.*;
import java.io.Serializable;

/**
 * @author zengling
 * @description
 * @date 2018/09/11 21:52
 */
@ApiModel(value = "EcUser", description = "电商用户对象")
@Data
public class EcUser extends BaseModel implements Serializable {
    /**
     * 正则表达式：验证用户名
     */
    public static final String REGEX_USERNAME = "^[a-zA-Z]\\w{5,30}$";
    /**
     * 正则表达式：验证密码
     */
    public static final String REGEX_PASSWORD = "^[a-zA-Z0-9]{8,16}$";
    /**
     * 正则表达式：验证手机号
     */
    public static final String REGEX_MOBILE = "^((13[0-9])|(15[^4,\\D])|(14[57])|(17[0])|(17[7])|(18[0,0-9]))\\d{8}$";
    private static final long serialVersionUID = 2994330064111958108L;
    @NotBlank(message = "用户名不为空")
    @Size(min = 2, max = 30)
    @Pattern(regexp = REGEX_USERNAME, message = "用户名不合法")
    @ApiModelProperty(value = "用户名", name = "userName", example = "用户名", required = true)
    private String userName;
    @NotBlank(message = "密码不为空")
    @Size(min = 6, max = 12)
    @Pattern(regexp = REGEX_PASSWORD, message = "密码不合法")
    @ApiModelProperty(value = "密码", name = "password", example = "密码", required = true)
    private String password;
    @NotBlank(message = "邮箱地址不能为空")
    @Email(message = "邮箱地址格式不正确")
    @ApiModelProperty(value = "邮箱地址", name = "email", example = "邮箱地址", required = true)
    private String email;
    @NotBlank(message = "电话号码不能为空")
    @Pattern(regexp = REGEX_MOBILE, message = "电话号码不正确")
    @ApiModelProperty(value = "电话号码", name = "phone", example = "电话号码", required = true)
    private String phone;
    @NotBlank(message = "密保问题不能为空")
    @Size(min = 2, max = 30, message = "问题字数限制在30字以内")
    @ApiModelProperty(value = "密保问题", name = "question", example = "密保问题", required = true)
    private String question;
    @NotBlank(message = "密保答案不能为空")
    @Size(min = 2, max = 30, message = "答案字数限制在30字以内")
    @ApiModelProperty(value = "密保答案", name = "answer", example = "密保答案", required = true)
    private String answer;
    //@NotNull(message = "角色不能为空")
    //@Pattern(regexp = "[01]", message = "角色格式为0|1,0管理员,1代表普通用户")
    @ApiModelProperty(value = "用户角色", name = "role",example = "0|1", required = true,hidden = true)
    private String role;
}