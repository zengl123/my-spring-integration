package com.zenlong.study.data.domain.vo.response;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 描述:
 * 项目名:my-spring-integration
 *
 * @Author:ZENLIN
 * @Created 2018/9/27  15:05.
 */
@Data
@NoArgsConstructor
public class TerminalRegisterMsgRespBody {
    public static final byte SUCCESS = 0;
    public static final byte CAR_ALREADY_REGISTERED = 1;
    public static final byte CAR_NOT_FOUND = 2;
    public static final byte TERMINAL_ALREADY_REGISTERED = 3;
    public static final byte TERMINAL_NOT_FOUND = 4;
    /**
     * byte[0-1] 应答流水号(WORD) 对应的终端注册消息的流水号
     */
    private int replyFlowId;
    /***
     * byte[2] 结果(BYTE) <br>
     * 0：成功<br>
     * 1：车辆已被注册<br>
     * 2：数据库中无该车辆<br>
     **/
    private byte replyCode;
    /**
     * byte[3-x] 鉴权码(STRING) 只有在成功后才有该字段
     */
    private String replyToken;
}
