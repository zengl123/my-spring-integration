package com.zenlong.study.data.domain.vo.request;

import com.zenlong.study.data.domain.PackageData;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 描述:
 * 项目名:my-spring-integration
 *
 * @Author:ZENLIN
 * @Created 2018/9/27  15:12.
 */
@Data
@NoArgsConstructor
public class TerminalRegisterMsg extends PackageData {

    private TerminalRegInfo terminalRegInfo;

    public TerminalRegisterMsg(PackageData packageData) {
        this();
        this.channel = packageData.getChannel();
        this.checkSum = packageData.getCheckSum();
        this.msgBodyBytes = packageData.getMsgBodyBytes();
        this.msgHeader = packageData.getMsgHeader();
    }

    @Data
    @NoArgsConstructor
    public static class TerminalRegInfo {
        /**
         * 省域ID(WORD),设备安装车辆所在的省域，省域ID采用GB/T2260中规定的行政区划代码6位中前两位 0保留，由平台取默认值
         */
        private int provinceId;
        /**
         * 市县域ID(WORD) 设备安装车辆所在的市域或县域,市县域ID采用GB/T2260中规定的行 政区划代码6位中后四位 0保留，由平台取默认值
         */
        private int cityId;
        /**
         * 制造商ID(BYTE[5]) 5 个字节，终端制造商编码
         */
        private String manufacturerId;
        /**
         * 终端型号(BYTE[8]) 八个字节， 此终端型号 由制造商自行定义 位数不足八位的，补空格。
         */
        private String terminalType;
        /**
         * 终端ID(BYTE[7]) 七个字节， 由大写字母 和数字组成， 此终端 ID由制造 商自行定义
         */
        private String terminalId;
        /**
         * 车牌颜色(BYTE) 车牌颜色，按照 JT/T415-2006 的 5.4.12 未上牌时，取值为0<br>
         * 0===未上车牌<br>
         * 1===蓝色<br>
         * 2===黄色<br>
         * 3===黑色<br>
         * 4===白色<br>
         * 9===其他
         */
        private int licensePlateColor;
        /**
         * 车牌(STRING) 公安交 通管理部门颁 发的机动车号牌
         */
        private String licensePlate;
    }
}
