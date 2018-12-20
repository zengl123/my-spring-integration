package com.zenlong.study.data.domain.vo.request;

import com.zenlong.study.data.domain.PackageData;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 描述:
 * 项目名:my-spring-integration
 *
 * @Author:ZENLIN
 * @Created 2018/9/27  15:07.
 */
@Data
@NoArgsConstructor
public class LocationInfoUploadMsg extends PackageData {
    /**
     * 告警信息byte[0-3]
     */
    private int warningFlagField;
    /**
     * byte[4-7] 状态(DWORD(32))
     */
    private int statusField;
    /**
     * byte[8-11] 纬度(DWORD(32))
     */
    private float latitude;
    /**
     * byte[12-15] 经度(DWORD(32))
     */
    private float longitude;
    /**
     * byte[16-17] 高程(WORD(16)) 海拔高度，单位为米（m）
     */
    private int elevation;
    /**
     * byte[18-19] 速度(WORD) 1/10km/h
     */
    private float speed;
    /**
     * byte[20-21] 方向(WORD) 0-359，正北为 0，顺时针
     */
    private int direction;
    /**
     * byte[22-x] 时间(BCD[6]) YY-MM-DD-hh-mm-ss GMT+8 时间，本标准中之后涉及的时间均采用此时区
     */
    private String time;

    public LocationInfoUploadMsg(PackageData packageData) {
        this();
        this.channel = packageData.getChannel();
        this.checkSum = packageData.getCheckSum();
        this.msgBodyBytes = packageData.getMsgBodyBytes();
        this.msgHeader = packageData.getMsgHeader();
    }
}

