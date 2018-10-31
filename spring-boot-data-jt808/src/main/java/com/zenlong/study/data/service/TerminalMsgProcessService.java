package com.zenlong.study.data.service;

import com.alibaba.fastjson.JSON;
import com.zenlong.study.data.domain.PackageData;
import com.zenlong.study.data.domain.PackageData.MsgHeader;
import com.zenlong.study.data.domain.Session;
import com.zenlong.study.data.domain.vo.request.LocationInfoUploadMsg;
import com.zenlong.study.data.domain.vo.request.TerminalAuthenticationMsg;
import com.zenlong.study.data.domain.vo.request.TerminalRegisterMsg;
import com.zenlong.study.data.domain.vo.response.ServerCommonRespMsgBody;
import com.zenlong.study.data.domain.vo.response.TerminalRegisterMsgRespBody;
import com.zenlong.study.data.server.SessionManager;
import com.zenlong.study.data.utils.MsgEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 描述:
 * 项目名:my-spring-integration
 *
 * @Author:ZENLIN
 * @Created 2018/9/27  15:35.
 */
public class TerminalMsgProcessService extends BaseMsgProcessService {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private MsgEncoder msgEncoder;
    private SessionManager sessionManager;

    public TerminalMsgProcessService() {
        this.msgEncoder = new MsgEncoder();
        this.sessionManager = SessionManager.getInstance();
    }

    /**
     * 终端注册
     *
     * @param msg
     * @throws Exception
     */
    public void processRegisterMsg(TerminalRegisterMsg msg) throws Exception {
        logger.info("终端注册:{}", JSON.toJSONString(msg, true));
        final String sessionId = Session.buildId(msg.getChannel());
        Session session = sessionManager.findBySessionId(sessionId);
        if (session == null) {
            session = Session.buildSession(msg.getChannel(), msg.getMsgHeader().getTerminalPhone());
        }
        session.setAuthenticated(true);
        session.setTerminalPhone(msg.getMsgHeader().getTerminalPhone());
        sessionManager.put(session.getId(), session);
        TerminalRegisterMsgRespBody respMsgBody = new TerminalRegisterMsgRespBody();
        respMsgBody.setReplyCode(TerminalRegisterMsgRespBody.SUCCESS);
        respMsgBody.setReplyFlowId(msg.getMsgHeader().getFlowId());
        respMsgBody.setReplyToken("123");
        int flowId = super.getFlowId(msg.getChannel());
        byte[] bs = this.msgEncoder.encode4TerminalRegisterResp(msg, respMsgBody, flowId);
        super.send2Client(msg.getChannel(), bs);
    }

    /**
     * 鉴权
     *
     * @param msg
     * @throws Exception
     */
    public void processAuthMsg(TerminalAuthenticationMsg msg) throws Exception {
        logger.info("终端鉴权:{}", JSON.toJSONString(msg, true));
        final String sessionId = Session.buildId(msg.getChannel());
        Session session = sessionManager.findBySessionId(sessionId);
        if (session == null) {
            session = Session.buildSession(msg.getChannel(), msg.getMsgHeader().getTerminalPhone());
        }
        session.setAuthenticated(true);
        session.setTerminalPhone(msg.getMsgHeader().getTerminalPhone());
        sessionManager.put(session.getId(), session);
        ServerCommonRespMsgBody respMsgBody = new ServerCommonRespMsgBody();
        respMsgBody.setReplyCode(ServerCommonRespMsgBody.SUCCESS);
        respMsgBody.setReplyFlowId(msg.getMsgHeader().getFlowId());
        respMsgBody.setReplyId(msg.getMsgHeader().getMsgId());
        int flowId = super.getFlowId(msg.getChannel());
        byte[] bs = this.msgEncoder.encode4ServerCommonRespMsg(msg, respMsgBody, flowId);
        super.send2Client(msg.getChannel(), bs);
    }

    /**
     * 心跳信息
     *
     * @param req
     * @throws Exception
     */
    public void processTerminalHeartBeatMsg(PackageData req) throws Exception {
        logger.info("心跳信息:{}", JSON.toJSONString(req, true));
        final MsgHeader reqHeader = req.getMsgHeader();
        ServerCommonRespMsgBody respMsgBody = new ServerCommonRespMsgBody(reqHeader.getFlowId(), reqHeader.getMsgId(),
                ServerCommonRespMsgBody.SUCCESS);
        int flowId = super.getFlowId(req.getChannel());
        byte[] bs = this.msgEncoder.encode4ServerCommonRespMsg(req, respMsgBody, flowId);
        super.send2Client(req.getChannel(), bs);
    }

    /**
     * 终端注销
     *
     * @param req
     * @throws Exception
     */
    public void processTerminalLogoutMsg(PackageData req) throws Exception {
        logger.info("终端注销:{}", JSON.toJSONString(req, true));
        final MsgHeader reqHeader = req.getMsgHeader();
        ServerCommonRespMsgBody respMsgBody = new ServerCommonRespMsgBody(reqHeader.getFlowId(), reqHeader.getMsgId(),
                ServerCommonRespMsgBody.SUCCESS);
        int flowId = super.getFlowId(req.getChannel());
        byte[] bs = this.msgEncoder.encode4ServerCommonRespMsg(req, respMsgBody, flowId);
        super.send2Client(req.getChannel(), bs);
    }

    /**
     * 位置信息
     *
     * @param req
     * @throws Exception
     */
    public void processLocationInfoUploadMsg(LocationInfoUploadMsg req) throws Exception {
        logger.info("位置信息:{}", JSON.toJSONString(req, true));
        final MsgHeader reqHeader = req.getMsgHeader();
        ServerCommonRespMsgBody respMsgBody = new ServerCommonRespMsgBody(reqHeader.getFlowId(), reqHeader.getMsgId(),
                ServerCommonRespMsgBody.SUCCESS);
        int flowId = super.getFlowId(req.getChannel());
        byte[] bs = this.msgEncoder.encode4ServerCommonRespMsg(req, respMsgBody, flowId);
        super.send2Client(req.getChannel(), bs);
    }
}
