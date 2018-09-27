package com.zenlong.study.data.utils;

import com.zenlong.study.data.constant.JT808Const;
import com.zenlong.study.data.domain.PackageData;
import com.zenlong.study.data.domain.PackageData.MsgHeader;
import com.zenlong.study.data.domain.vo.request.LocationInfoUploadMsg;
import com.zenlong.study.data.domain.vo.request.TerminalRegisterMsg;
import com.zenlong.study.data.domain.vo.request.TerminalRegisterMsg.TerminalRegInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * 描述:消息解码器
 * 项目名:my-spring-integration
 *
 * @Author:ZENLIN
 * @Created 2018/9/27  14:01.
 */
@Component
@Scope("prototype")
public class MsgDecoder {

    private static final Logger logger = LoggerFactory.getLogger(MsgDecoder.class);

    private BitOperator bitOperator;

    public MsgDecoder() {
        this.bitOperator = new BitOperator();
    }

  /*  public PackageData bytes2PackageData(byte[] data) {
        PackageData ret = new PackageData();
        MsgHeader msgHeader = this.parseMsgHeaderFromBytes(data);
        ret.setMsgHeader(msgHeader);
        int msgBodyByteStartIndex = 12;
        if (msgHeader.isHasSubPackage()) {
            msgBodyByteStartIndex = 16;
        }
        byte[] tmp = new byte[msgHeader.getMsgBodyLength()];
        System.arraycopy(data, msgBodyByteStartIndex, tmp, 0, tmp.length);
        ret.setMsgBodyBytes(tmp);
        int checkSumInPkg = data[data.length - 1];
        int calculatedCheckSum = this.bitOperator.getCheckSum4JT808(data, 0, data.length - 1);
        ret.setCheckSum(checkSumInPkg);
        if (checkSumInPkg != calculatedCheckSum) {
            logger.warn("检验码不一致,msgid:{},pkg:{},calculated:{}", msgHeader.getMsgId(), checkSumInPkg, calculatedCheckSum);
        }
        return ret;
    }
*/

    /**
     * @Description: 将byte[]解码成业务对象
     * @param data
     * @return PackageData
     */
    public PackageData bytes2PackageData(byte[] data) {
        //先把数据包反转义一下
        List<Byte> listbs = new ArrayList<Byte>();
        for (int i = 1; i < data.length - 1; i++) {
            //如果当前位是0x7d，判断后一位是否是0x02或0x01，如果是，则反转义
            if ((data[i] == (byte)0x7d) && (data[i + 1] == (byte) 0x02)) {
                listbs.add((byte) JT808Const.PKG_DELIMITER);
                i++;
            } else if ((data[i] == (byte) 0x7d) && (data[i + 1] == (byte) 0x01)) {
                listbs.add((byte) 0x7d);
                i++;
            } else {
                listbs.add(data[i]);
            }
        }
        byte[] newbs = new byte[listbs.size()];
        for (int i = 0; i < listbs.size(); i++) {
            newbs[i] = listbs.get(i);
        }
        //将反转义后的byte[]转成业务对象
        PackageData pkg = new PackageData();
        MsgHeader msgHeader = this.parseMsgHeaderFromBytes(data);
        pkg.setMsgHeader(msgHeader);
        byte[] bodybs = DigitUtil.sliceBytes(newbs, 11, 11 + msgHeader.getMsgBodyLength() - 1);
        pkg.setMsgBodyBytes(bodybs);
        return pkg;
    }


    private MsgHeader parseMsgHeaderFromBytes(byte[] data) {
        MsgHeader msgHeader = new MsgHeader();
        msgHeader.setMsgId(this.parseIntFromBytes(data, 0, 2));
        int msgBodyProps = this.parseIntFromBytes(data, 2, 2);
        msgHeader.setMsgBodyPropsField(msgBodyProps);
        msgHeader.setMsgBodyLength(msgBodyProps & 0x3ff);
        msgHeader.setEncryptionType((msgBodyProps & 0x1c00) >> 10);
        msgHeader.setHasSubPackage(((msgBodyProps & 0x2000) >> 13) == 1);
        msgHeader.setReservedBit(((msgBodyProps & 0xc000) >> 14) + "");
        msgHeader.setTerminalPhone(this.parseBcdStringFromBytes(data, 4, 6));
        msgHeader.setFlowId(this.parseIntFromBytes(data, 10, 2));
        if (msgHeader.isHasSubPackage()) {
            msgHeader.setPackageInfoField(this.parseIntFromBytes(data, 12, 4));
            msgHeader.setTotalSubPackage(this.parseIntFromBytes(data, 12, 2));
            msgHeader.setSubPackageSeq(this.parseIntFromBytes(data, 12, 2));
        }
        return msgHeader;
    }

    protected String parseStringFromBytes(byte[] data, int startIndex, int lenth) {
        return this.parseStringFromBytes(data, startIndex, lenth, null);
    }

    private String parseStringFromBytes(byte[] data, int startIndex, int lenth, String defaultVal) {
        try {
            byte[] tmp = new byte[lenth];
            System.arraycopy(data, startIndex, tmp, 0, lenth);
            return new String(tmp, JT808Const.STRING_CHARSET);
        } catch (Exception e) {
            logger.error("解析字符串出错:{}", e.getMessage());
            e.printStackTrace();
            return defaultVal;
        }
    }

    private String parseBcdStringFromBytes(byte[] data, int startIndex, int lenth) {
        return this.parseBcdStringFromBytes(data, startIndex, lenth, null);
    }

    private String parseBcdStringFromBytes(byte[] data, int startIndex, int lenth, String defaultVal) {
        try {
            byte[] tmp = new byte[lenth];
            System.arraycopy(data, startIndex, tmp, 0, lenth);
            return DigitUtil.bcdToStr(tmp);
        } catch (Exception e) {
            logger.error("解析BCD(8421码)出错:{}", e.getMessage());
            e.printStackTrace();
            return defaultVal;
        }
    }

    private int parseIntFromBytes(byte[] data, int startIndex, int length) {
        return this.parseIntFromBytes(data, startIndex, length, 0);
    }

    private int parseIntFromBytes(byte[] data, int startIndex, int length, int defaultVal) {
        try {
            // 字节数大于4,从起始索引开始向后处理4个字节,其余超出部分丢弃
            final int len = length > 4 ? 4 : length;
            byte[] tmp = new byte[len];
            System.arraycopy(data, startIndex, tmp, 0, len);
            return bitOperator.byteToInteger(tmp);
        } catch (Exception e) {
            logger.error("解析整数出错:{}", e.getMessage());
            e.printStackTrace();
            return defaultVal;
        }
    }

    public TerminalRegisterMsg toTerminalRegisterMsg(PackageData packageData) {
        TerminalRegisterMsg ret = new TerminalRegisterMsg(packageData);
        byte[] data = ret.getMsgBodyBytes();
        TerminalRegisterMsg.TerminalRegInfo body = new TerminalRegInfo();
        // 1. byte[0-1] 省域ID(WORD)
        // 设备安装车辆所在的省域，省域ID采用GB/T2260中规定的行政区划代码6位中前两位
        // 0保留，由平台取默认值
        body.setProvinceId(this.parseIntFromBytes(data, 0, 2));
        // 2. byte[2-3] 设备安装车辆所在的市域或县域,市县域ID采用GB/T2260中规定的行 政区划代码6位中后四位
        // 0保留，由平台取默认值
        body.setCityId(this.parseIntFromBytes(data, 2, 2));
        // 3. byte[4-8] 制造商ID(BYTE[5]) 5 个字节，终端制造商编码
        // byte[] tmp = new byte[5];
        body.setManufacturerId(this.parseStringFromBytes(data, 4, 5));
        // 4. byte[9-16] 终端型号(BYTE[8]) 八个字节， 此终端型号 由制造商自行定义 位数不足八位的，补空格。
        body.setTerminalType(this.parseStringFromBytes(data, 9, 8));
        // 5. byte[17-23] 终端ID(BYTE[7]) 七个字节， 由大写字母 和数字组成， 此终端 ID由制造 商自行定义
        body.setTerminalId(this.parseStringFromBytes(data, 17, 7));
        // 6. byte[24] 车牌颜色(BYTE) 车牌颜 色按照JT/T415-2006 中5.4.12 的规定
        body.setLicensePlateColor(this.parseIntFromBytes(data, 24, 1));
        // 7. byte[25-x] 车牌(STRING) 公安交 通管理部门颁 发的机动车号牌
        body.setLicensePlate(this.parseStringFromBytes(data, 25, data.length - 25));
        ret.setTerminalRegInfo(body);
        return ret;
    }


    public LocationInfoUploadMsg toLocationInfoUploadMsg(PackageData packageData) {
        LocationInfoUploadMsg ret = new LocationInfoUploadMsg(packageData);
        final byte[] data = ret.getMsgBodyBytes();
        // 1. byte[0-3] 报警标志(DWORD(32))
        ret.setWarningFlagField(this.parseIntFromBytes(data, 0, 3));
        // 2. byte[4-7] 状态(DWORD(32))
        ret.setStatusField(this.parseIntFromBytes(data, 4, 4));
        // 3. byte[8-11] 纬度(DWORD(32)) 以度为单位的纬度值乘以10^6，精确到百万分之一度
        ret.setLatitude(this.parseFloatFromBytes(data, 8, 4));
        // 4. byte[12-15] 经度(DWORD(32)) 以度为单位的经度值乘以10^6，精确到百万分之一度
        ret.setLongitude(this.parseFloatFromBytes(data, 12, 4));
        // 5. byte[16-17] 高程(WORD(16)) 海拔高度，单位为米（ m）
        ret.setElevation(this.parseIntFromBytes(data, 16, 2));
        // byte[18-19] 速度(WORD) 1/10km/h
        ret.setSpeed(this.parseFloatFromBytes(data, 18, 2));
        // byte[20-21] 方向(WORD) 0-359，正北为 0，顺时针
        ret.setDirection(this.parseIntFromBytes(data, 20, 2));
        // byte[22-x] 时间(BCD[6]) YY-MM-DD-hh-mm-ss
        // GMT+8 时间，本标准中之后涉及的时间均采用此时区
        byte[] tmp = new byte[6];
        System.arraycopy(data, 22, tmp, 0, 6);
        String time = this.parseBcdStringFromBytes(data, 22, 6);
        ret.setTime(time);
        return ret;
    }

    private float parseFloatFromBytes(byte[] data, int startIndex, int length) {
        return this.parseFloatFromBytes(data, startIndex, length, 0f);
    }

    private float parseFloatFromBytes(byte[] data, int startIndex, int length, float defaultVal) {
        try {
            // 字节数大于4,从起始索引开始向后处理4个字节,其余超出部分丢弃
            final int len = length > 4 ? 4 : length;
            byte[] tmp = new byte[len];
            System.arraycopy(data, startIndex, tmp, 0, len);
            return bitOperator.byte2Float(tmp);
        } catch (Exception e) {
            logger.error("解析浮点数出错:{}", e.getMessage());
            e.printStackTrace();
            return defaultVal;
        }
    }
}