package com.zenlong.study.data.domain;

import io.netty.channel.Channel;
import lombok.*;
import org.joda.time.DateTime;

import java.net.SocketAddress;

/**
 * 描述:
 * 项目名:my-spring-integration
 *
 * @Author:ZENLIN
 * @Created 2018/9/27  11:35.
 */
@Data
@NoArgsConstructor
public class Session {
    private String id;
    private String terminalPhone;
    private Channel channel = null;
    private boolean isAuthenticated = false;
    /**
     * 消息流水号 word(16) 按发送顺序从 0 开始循环累加
     */
    private int currentFlowId = 0;
    /**
     * 消息流水号 word(16) 按发送顺序从 0 开始循环累加
     */
    private long lastCommunicateTimeStamp = 0L;


    public static String buildId(Channel channel) {
        return channel.id().asLongText();
    }

    public static Session buildSession(Channel channel) {
        return buildSession(channel, null);
    }

    public static Session buildSession(Channel channel, String phone) {
        Session session = new Session();
        session.setChannel(channel);
        session.setId(buildId(channel));
        session.setTerminalPhone(phone);
        session.setLastCommunicateTimeStamp(System.currentTimeMillis());
        return session;
    }


    public SocketAddress getRemoteAddr() {
        System.out.println(this.channel.remoteAddress().getClass());
        return this.channel.remoteAddress();
    }

    public boolean isAuthenticated() {
        return isAuthenticated;
    }


    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        Session other = (Session) obj;
        if (id == null) {
            if (other.id != null) {
                return false;
            }
        } else if (!id.equals(other.id)) {
            return false;
        }
        return true;
    }

    public synchronized int currentFlowId() {
        if (currentFlowId >= 0xffff) {
            currentFlowId = 0;
        }
        return currentFlowId++;
    }
}
