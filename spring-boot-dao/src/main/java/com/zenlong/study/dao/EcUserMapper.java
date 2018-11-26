package com.zenlong.study.dao;

import com.zenlong.study.domain.po.EcUser;
import org.apache.ibatis.annotations.Param;
import org.mybatis.generator.api.dom.java.BaseMapper;
import org.springframework.stereotype.Component;

/**
 * @author zengling
 * @description
 * @date 2018/09/11 21:52
 */
@Component
public interface EcUserMapper extends BaseMapper<EcUser> {
    /**
     * 校验用户名
     *
     * @param userName
     * @return
     */
    int checkUserName(String userName);

    /**
     * 校验email
     *
     * @param email
     * @return
     */
    int checkEmail(String email);

    /**
     * 校验手机号码
     *
     * @param phone
     * @return
     */
    int checkPhone(String phone);

    /**
     * 检验问题答案
     *
     * @param userName
     * @param question
     * @param answer
     * @return
     */
    int checkAnswer(@Param(value = "userName") String userName, @Param(value = "question") String question, @Param(value = "answer") String answer);

    /**
     * 校验
     *
     * @param userName
     * @param password
     * @return
     */
}