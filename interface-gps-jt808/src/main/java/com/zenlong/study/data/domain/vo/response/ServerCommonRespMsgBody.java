package com.zenlong.study.data.domain.vo.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 描述:
 * 项目名:my-spring-integration
 *
 * @Author:ZENLIN
 * @Created 2018/9/27  15:03.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ServerCommonRespMsgBody {
    public static final byte SUCCESS = 0;
    public static final byte FAILURE = 1;
    public static final byte MSG_ERROR = 2;
    public static final byte UNSUPPORTED = 3;
    public static final byte WARNING_MSG_ACK = 4;
    /**
     * byte[0-1] 应答流水号 对应的终端消息的流水号
     */
    private int replyFlowId;
    /**
     * byte[2-3] 应答ID 对应的终端消息的ID
     */
    private int replyId;
    /**
     * 0：成功∕确认<br>
     * 1：失败<br>
     * 2：消息有误<br>
     * 3：不支持<br>
     * 4：报警处理确认<br>
     */
    private byte replyCode;
}
