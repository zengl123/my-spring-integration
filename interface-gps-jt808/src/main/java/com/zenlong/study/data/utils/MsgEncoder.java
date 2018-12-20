package com.zenlong.study.data.utils;

import com.zenlong.study.data.constant.JT808Const;
import com.zenlong.study.data.domain.PackageData;
import com.zenlong.study.data.domain.Session;
import com.zenlong.study.data.domain.vo.request.TerminalRegisterMsg;
import com.zenlong.study.data.domain.vo.response.ServerCommonRespMsgBody;
import com.zenlong.study.data.domain.vo.response.TerminalRegisterMsgRespBody;

import java.util.Arrays;

/**
 * 描述:
 * 项目名:my-spring-integration
 *
 * @Author:ZENLIN
 * @Created 2018/9/27  15:48.
 */
public class MsgEncoder {
    private BitOperator bitOperator;
    private JT808ProtocolUtil jt808ProtocolUtil;

    public MsgEncoder() {
        this.bitOperator = new BitOperator();
        this.jt808ProtocolUtil = new JT808ProtocolUtil();
    }

    public byte[] encode4TerminalRegisterResp(TerminalRegisterMsg req, TerminalRegisterMsgRespBody respMsgBody, int flowId) throws Exception {
        // 消息体字节数组
        byte[] msgBody;
        // 鉴权码(STRING) 只有在成功后才有该字段
        if (respMsgBody.getReplyCode() == TerminalRegisterMsgRespBody.SUCCESS) {
            msgBody = this.bitOperator.concatAll(Arrays.asList(
                    bitOperator.integerTo2Bytes(respMsgBody.getReplyFlowId()),
                    new byte[] { respMsgBody.getReplyCode() },
                    respMsgBody.getReplyToken().getBytes(JT808Const.STRING_CHARSET)
            ));
        } else {
            msgBody = this.bitOperator.concatAll(Arrays.asList(
                    bitOperator.integerTo2Bytes(respMsgBody.getReplyFlowId()),
                    new byte[] { respMsgBody.getReplyCode() }
            ));
        }
        // 消息头
        int msgBodyProps = this.jt808ProtocolUtil.generateMsgBodyProps(msgBody.length, 0b000, false, 0);
        byte[] msgHeader = this.jt808ProtocolUtil.generateMsgHeader(req.getMsgHeader().getTerminalPhone(),
                JT808Const.CMD_TERMINAL_REGISTER_RESP, msgBody, msgBodyProps, flowId);
        byte[] headerAndBody = this.bitOperator.concatAll(msgHeader, msgBody);
        // 校验码
        int checkSum = this.bitOperator.getCheckSum4JT808(headerAndBody, 0, headerAndBody.length - 1);
        // 连接并且转义
        return this.doEncode(headerAndBody, checkSum);
    }


    public byte[] encode4ServerCommonRespMsg(PackageData req, ServerCommonRespMsgBody respMsgBody, int flowId)
            throws Exception {
        byte[] msgBody = this.bitOperator.concatAll(Arrays.asList(
                bitOperator.integerTo2Bytes(respMsgBody.getReplyFlowId()),
                bitOperator.integerTo2Bytes(respMsgBody.getReplyId()),
                new byte[] { respMsgBody.getReplyCode() }
        ));
        int msgBodyProps = this.jt808ProtocolUtil.generateMsgBodyProps(msgBody.length, 0b000, false, 0);
        byte[] msgHeader = this.jt808ProtocolUtil.generateMsgHeader(req.getMsgHeader().getTerminalPhone(),
                JT808Const.CMD_COMMON_RESP, msgBody, msgBodyProps, flowId);
        byte[] headerAndBody = this.bitOperator.concatAll(msgHeader, msgBody);
        // 校验码
        int checkSum = this.bitOperator.getCheckSum4JT808(headerAndBody, 0, headerAndBody.length - 1);
        // 连接并且转义
        return this.doEncode(headerAndBody, checkSum);
    }

    public byte[] encode4ParamSetting(byte[] msgBodyBytes, Session session) throws Exception {
        // 消息头
        int msgBodyProps = this.jt808ProtocolUtil.generateMsgBodyProps(msgBodyBytes.length, 0b000, false, 0);
        byte[] msgHeader = this.jt808ProtocolUtil.generateMsgHeader(session.getTerminalPhone(),
                JT808Const.CMD_TERMINAL_PARAM_SETTINGS, msgBodyBytes, msgBodyProps, session.currentFlowId());
        // 连接消息头和消息体
        byte[] headerAndBody = this.bitOperator.concatAll(msgHeader, msgBodyBytes);
        // 校验码
        int checkSum = this.bitOperator.getCheckSum4JT808(headerAndBody, 0, headerAndBody.length - 1);
        // 连接并且转义
        return this.doEncode(headerAndBody, checkSum);
    }

    private byte[] doEncode(byte[] headerAndBody, int checkSum) throws Exception {
        byte[] noEscapedBytes = this.bitOperator.concatAll(Arrays.asList(new byte[] { JT808Const.PKG_DELIMITER },
                headerAndBody,
                bitOperator.integerTo1Bytes(checkSum),
                new byte[] { JT808Const.PKG_DELIMITER }
        ));
        return jt808ProtocolUtil.doEscape4Send(noEscapedBytes, 1, noEscapedBytes.length - 2);
    }
}
