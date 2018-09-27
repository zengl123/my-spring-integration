package com.zenlong.study.data.utils;

import com.zenlong.study.data.constant.JT808Const;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * 描述:消息编码器
 * 项目名:my-spring-integration
 *
 * @Author:ZENLIN
 * @Created 2018/9/27  14:06.
 */
@Component
@Scope("prototype")
public class MsgEncoderOld {

    /**
     * 编码整个消息包
     */
    public byte[] encode4Msg(int headId, String terminalPhone, byte[] bodybs) {
        //消息头
        byte[] headbs = new byte[13];
        headbs[0] = (byte) JT808Const.PKG_DELIMITER;
        //消息头标识Id
        byte[] headidbs = DigitUtil.shortTo2Byte((short) headId);
        headbs[1] = headidbs[0];
        headbs[2] = headidbs[1];
        //消息体属性
        //先获取消息体长度，并转成2个字节16位格式
        //808规范中用10bit表示长度，所以最大不能超过1024
        byte[] bodylengthbs = DigitUtil.shortTo2Byte((short) bodybs.length);
        //808规范中用10bit表示长度，所以只取低10bit，0~9
        String bodylength = "" +
                //第一个字节最后2bit
                + (byte) ((bodylengthbs[0] >> 1) & 0x1) + (byte) ((bodylengthbs[0] >> 0) & 0x1)
                //第二个字节8bit
                + (byte) ((bodylengthbs[1] >> 7) & 0x1) + (byte) ((bodylengthbs[1] >> 6) & 0x1)
                + (byte) ((bodylengthbs[1] >> 5) & 0x1) + (byte) ((bodylengthbs[1] >> 4) & 0x1)
                + (byte) ((bodylengthbs[1] >> 3) & 0x1) + (byte) ((bodylengthbs[1] >> 2) & 0x1)
                + (byte) ((bodylengthbs[1] >> 1) & 0x1) + (byte) ((bodylengthbs[1] >> 0) & 0x1);
        //加密方式为不加密，第10、11、12三位都为0，表示消息体不加密
        String encrypt = "000";
        //暂无分包，第13bit为0
        String subpackage = "0";
        //保留，第14，15bit为0
        String retain = "00";
        //生成消息体属性
        byte[] bodyattrbs = new byte[2];
        //消息体高8位
        bodyattrbs[0] = DigitUtil.binaryStrToByte(retain + subpackage + encrypt + bodylength.substring(0, 2));
        //消息体低8位
        bodyattrbs[1] = DigitUtil.binaryStrToByte(bodylength.substring(2, 10));
        headbs[3] = bodyattrbs[0];
        headbs[4] = bodyattrbs[1];
        //手机号码
        byte[] phonebs = DigitUtil.strToBcd(terminalPhone);
        headbs[5] = phonebs[0];
        headbs[6] = phonebs[1];
        headbs[7] = phonebs[2];
        headbs[8] = phonebs[3];
        headbs[9] = phonebs[4];
        headbs[10] = phonebs[5];
        //消息流水号
        headbs[11] = 0x00;
        headbs[12] = 0x00;
        //消息尾
        byte[] tailbs = new byte[] {0x00, (byte) JT808Const.PKG_DELIMITER};
        //整个消息
        byte[] msgbs = ArrayUtil.concatAll(headbs, bodybs, tailbs);
        //设置校验码
        msgbs[msgbs.length - 2] = (byte) DigitUtil.get808PackCheckCode(msgbs);
        return msgbs;
    }
}
