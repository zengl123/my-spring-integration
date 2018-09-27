package com.zenlong.study.data.domain.vo.request;

import com.zenlong.study.data.constant.JT808Const;
import com.zenlong.study.data.domain.PackageData;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 描述:终端鉴权消息
 * 项目名:my-spring-integration
 *
 * @Author:ZENLIN
 * @Created 2018/9/27  15:11.
 */
@Data
@NoArgsConstructor
public class TerminalAuthenticationMsg extends PackageData {
    private String authCode;

    public TerminalAuthenticationMsg(PackageData packageData) {
        this();
        this.channel = packageData.getChannel();
        this.checkSum = packageData.getCheckSum();
        this.msgBodyBytes = packageData.getMsgBodyBytes();
        this.msgHeader = packageData.getMsgHeader();
        this.authCode = new String(packageData.getMsgBodyBytes(), JT808Const.STRING_CHARSET);
    }
}