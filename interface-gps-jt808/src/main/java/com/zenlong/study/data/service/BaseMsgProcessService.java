package com.zenlong.study.data.service;

import com.zenlong.study.data.constant.JT808Const;
import com.zenlong.study.data.domain.Session;
import com.zenlong.study.data.server.SessionManager;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * 描述:
 * 项目名:my-spring-integration
 *
 * @Author:ZENLIN
 * @Created 2018/9/27  13:57.
 */
public class BaseMsgProcessService {
    protected final Logger logger = LoggerFactory.getLogger(getClass());

    protected SessionManager sessionManager;

    public BaseMsgProcessService() {
        this.sessionManager = SessionManager.getInstance();
    }

    protected ByteBuf getByteBuf(byte[] arr) {
        ByteBuf byteBuf = PooledByteBufAllocator.DEFAULT.directBuffer(arr.length);
        byteBuf.writeBytes(arr);
        return byteBuf;
    }

    public void send2Client(Channel channel, byte[] arr) throws InterruptedException {
        if (channel.isOpen()) {
            ChannelFuture future = channel.writeAndFlush(Unpooled.copiedBuffer(arr)).sync();
            if (!future.isSuccess()) {
                logger.error("发送数据出错:{}", future.cause());
            }
        } else {
            logger.info("channel is closed");
        }
    }

    protected int getFlowId(Channel channel, int defaultValue) {
        Session session = this.sessionManager.findBySessionId(Session.buildId(channel));
        if (session == null) {
            return defaultValue;
        }
        return session.currentFlowId();
    }

    protected int getFlowId(Channel channel) {
        return this.getFlowId(channel, 0);
    }
}
