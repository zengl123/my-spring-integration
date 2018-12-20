package com.zenlong.study.data.server;

/**
 * 描述:
 * 项目名:my-spring-integration
 *
 * @Author:ZENLIN
 * @Created 2018/9/27  11:28.
 */

import com.zenlong.study.data.constant.JT808Const;
import com.zenlong.study.data.domain.PackageData;
import com.zenlong.study.data.domain.PackageData.MsgHeader;
import com.zenlong.study.data.domain.Session;
import com.zenlong.study.data.domain.vo.request.LocationInfoUploadMsg;
import com.zenlong.study.data.domain.vo.request.TerminalAuthenticationMsg;
import com.zenlong.study.data.domain.vo.request.TerminalRegisterMsg;
import com.zenlong.study.data.service.TerminalMsgProcessService;
import com.zenlong.study.data.utils.MsgDecoder;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.ReferenceCountUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * ClassName: ServerHandler
 *
 * @author ZENLIN
 * @Description: 业务处理handler, 所有终端发过来的数据首先就是由这个handler处理
 */
public class ServerHandler extends ChannelInboundHandlerAdapter {

    private final static Logger logger = LoggerFactory.getLogger(ServerHandler.class);

    private final SessionManager sessionManager;
    private MsgDecoder msgDecoder;
    private TerminalMsgProcessService msgProcessService;

    public ServerHandler() {
        this.sessionManager = new SessionManager();
        this.msgDecoder = new MsgDecoder();
        this.msgProcessService = new TerminalMsgProcessService();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws InterruptedException {
        logger.info("message:{}", msg);
        try {
            ByteBuf buf = (ByteBuf) msg;
            if (buf.readableBytes() <= 0) {
                logger.info("msg is null");
                return;
            }
            byte[] bs = new byte[buf.readableBytes()];
            buf.readBytes(bs);
            //字节数据转换为针对于808消息结构的业务对象
            PackageData pkg = this.msgDecoder.bytes2PackageData(bs);
            //引用channel,以便回送数据给终端
            pkg.setChannel(ctx.channel());
            this.processPackageData(pkg);
        } catch (Exception e) {
            logger.error("channelRead()异常:{}", e.getMessage());
        } finally {
            ReferenceCountUtil.release(msg);
        }
    }

    private void processPackageData(PackageData packageData) throws Exception {
        final MsgHeader header = packageData.getMsgHeader();
        if (JT808Const.MSG_ID_TERMINAL_HEART_BEAT == header.getMsgId()) {
            // 1. 终端心跳-消息体为空 ==> 平台通用应答
            this.msgProcessService.processTerminalHeartBeatMsg(packageData);
        } else if (JT808Const.MSG_ID_TERMINAL_AUTHENTICATION == header.getMsgId()) {
            // 2. 终端鉴权 ==> 平台通用应答
            TerminalAuthenticationMsg authenticationMsg = new TerminalAuthenticationMsg(packageData);
            this.msgProcessService.processAuthMsg(authenticationMsg);
        } else if (JT808Const.MSG_ID_TERMINAL_REGISTER == header.getMsgId()) {
            //3.终端注册 ==> 终端注册应答
            TerminalRegisterMsg msg = this.msgDecoder.toTerminalRegisterMsg(packageData);
            this.msgProcessService.processRegisterMsg(msg);
        } else if (JT808Const.MSG_ID_TERMINAL_LOG_OUT == header.getMsgId()) {
            //4.终端注销(终端注销数据消息体为空) ==> 平台通用应答
            this.msgProcessService.processTerminalLogoutMsg(packageData);
        } else if (JT808Const.MSG_ID_TERMINAL_LOCATION_INFO_UPLOAD == header.getMsgId()) {
            //5.位置信息汇报 ==> 平台通用应答
            LocationInfoUploadMsg locationInfoUploadMsg = this.msgDecoder.toLocationInfoUploadMsg(packageData);
            this.msgProcessService.processLocationInfoUploadMsg(locationInfoUploadMsg);
            //拿到位置信息进行存储
            String terminalPhone = locationInfoUploadMsg.getMsgHeader().getTerminalPhone();
            float latitude = locationInfoUploadMsg.getLatitude();
            float longitude = locationInfoUploadMsg.getLongitude();
            float speed = locationInfoUploadMsg.getSpeed();
            int direction = locationInfoUploadMsg.getDirection();
            int elevation = locationInfoUploadMsg.getElevation();
            locationInfoUploadMsg.getTime();

        } else {
            //6.其他
        }
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        Session session = Session.buildSession(ctx.channel());
        sessionManager.put(session.getId(), session);
        logger.debug("终端连接:{}", session);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        final String sessionId = ctx.channel().id().asLongText();
        Session session = sessionManager.findBySessionId(sessionId);
        this.sessionManager.removeBySessionId(sessionId);
        logger.debug("终端断开连接:{}", session);
        ctx.channel().close();
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (IdleStateEvent.class.isAssignableFrom(evt.getClass())) {
            IdleStateEvent event = (IdleStateEvent) evt;
            if (event.state() == IdleState.READER_IDLE) {
                Session session = this.sessionManager.removeBySessionId(Session.buildId(ctx.channel()));
                logger.error("服务器主动断开连接:{}", session);
                ctx.close();
            }
        }
    }
}
