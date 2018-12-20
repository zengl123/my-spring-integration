package com.zenlong.study.data.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * 描述:
 * 项目名:my-spring-integration
 *
 * @Author:ZENLIN
 * @Created 2018/9/27  17:34.
 */
@Component
public class TcpServerStartRunner implements CommandLineRunner {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private Server server;

    @Override
    public void run(String... args) throws Exception {
        if (null == server) {
            server = new Server(20048);
            //启动线程
            server.startServer();
            logger.info("TCPServer服务启动执行");
        }
    }
}
