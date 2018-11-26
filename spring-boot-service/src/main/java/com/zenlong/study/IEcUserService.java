package com.zenlong.study;

import com.zenlong.study.common.ServerResponse;
import com.zenlong.study.domain.po.EcUser;


/**
 * @Project my-spring-integration
 * @Package com.zenlong.study
 * @ClassName IEcUserService
 * @Author ZENLIN
 * @Date 2018-09-11 22:23
 * @Description TODO
 * @Version
 * @Modified By
 */
public interface IEcUserService {
    /**
     * 注册
     *
     * @param ecUser
     * @return
     */
    ServerResponse register(EcUser ecUser);
    /**
     * 数据校验
     *
     * @param type
     * @param value
     * @return
     */
    ServerResponse checkValid(String type, String value);


}
