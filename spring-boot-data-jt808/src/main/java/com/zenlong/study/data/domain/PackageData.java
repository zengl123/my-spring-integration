package com.zenlong.study.data.domain;

import com.alibaba.fastjson.annotation.JSONField;
import io.netty.channel.Channel;
import lombok.Data;

/**
 * 描述:
 * 项目名:my-spring-integration
 *
 * @Author:ZENLIN
 * @Created 2018/9/27  11:33.
 */
@Data
public class PackageData {
    /**
     * 16byte 消息头
     */
    protected MsgHeader msgHeader;
    /**
     * 消息体字节数组
     */
    @JSONField(serialize = false)
    protected byte[] msgBodyBytes;
    /**
     * 校验码 1byte
     */
    protected int checkSum;
    @JSONField(serialize = false)
    protected Channel channel;

    @Data
    public static class MsgHeader {
        /**
         * 消息ID
         */
        protected int msgId;

        /**
         * 消息体属性byte[2-3]
         */
        protected int msgBodyPropsField;
        /**
         * 消息体长度
         */
        protected int msgBodyLength;
        /**
         * 数据加密方式
         */
        protected int encryptionType;
        /**
         * 是否分包,true==>有消息包封装项
         */
        protected boolean hasSubPackage;
        /**
         * 保留位[14-15]
         */
        protected String reservedBit;

        /**
         * 终端手机号
         */
        protected String terminalPhone;
        /**
         * 流水号
         */
        protected int flowId;

        /**
         * 消息包封装项byte[12-15]
         */
        protected int packageInfoField;
        /**
         * 消息包总数(word(16))
         */
        protected long totalSubPackage;
        /**
         * 包序号(word(16))这次发送的这个消息包是分包中的第几个消息包, 从 1 开始
         */
        protected long subPackageSeq;
    }
}
