package com.zenlong.study.common;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;


import java.io.Serializable;

/**
 * @Project spring-boot-all
 * @Package com.zenlin.mysql.web.common
 * @ClassName ServerResponse
 * @Author ZENLIN
 * @Date 2018-08-27 21:47
 * @Description TODO
 * @Version
 * @Modified By
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ServerResponse<T> implements Serializable {
    private String message;
    private Integer status;
    private T data;

    public ServerResponse(int status) {
        this.status = status;
    }

    public ServerResponse(int status, T data) {
        this.status = status;
        this.data = data;
    }

    public ServerResponse(int status, String message) {
        this.status = status;
        this.message = message;
    }

    public ServerResponse(int status, T data, String message) {
        this.status = status;
        this.data = data;
        this.message = message;
    }

    @JsonIgnore
    public boolean isSuccess() {
        return this.status == ResponseCode.SUCCESS.getCode();
    }

    public static <T> ServerResponse<T> createBySuccess() {
        return new ServerResponse<>(ResponseCode.SUCCESS.getCode());
    }

    public static <T> ServerResponse<T> createBySuccess(T data) {
        return new ServerResponse<>(ResponseCode.SUCCESS.getCode(), data);
    }

    public static <T> ServerResponse<T> createBySuccess(T data, String message) {
        return new ServerResponse<>(ResponseCode.SUCCESS.getCode(), data, message);
    }

    public static <T> ServerResponse<T> createBySuccessMessage(String message) {
        return new ServerResponse<>(ResponseCode.SUCCESS.getCode(), message);
    }

    public <T> ServerResponse<T> createByError() {
        return new ServerResponse<>(ResponseCode.ERROR.getCode(), ResponseCode.ERROR.getDesc());
    }

    public static <T> ServerResponse<T> createByErrorMessage(String message) {
        return new ServerResponse<>(ResponseCode.ERROR.getCode(), message);
    }

    public static <T> ServerResponse<T> createByErrorCodeMessage(int errorCode, String errorMessage) {
        return new ServerResponse<>(errorCode, errorMessage);
    }
}
