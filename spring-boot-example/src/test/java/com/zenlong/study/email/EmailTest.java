package com.zenlong.study.email;

import com.zenlong.study.SpringBootExampleApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * 描述:
 * 项目名:my-spring-integration
 *
 * @Author:ZENLIN
 * @Created 2019/1/7  15:50.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = SpringBootExampleApplication.class)
public class EmailTest {
    @Autowired
    private Email email;

    @Test
    public void sendSimpleMail() throws Exception {
        email.sendSimpleMail();
    }
}
