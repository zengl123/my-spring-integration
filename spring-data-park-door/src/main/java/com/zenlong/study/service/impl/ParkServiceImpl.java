package com.zenlong.study.service.impl;

import com.zenlong.study.common.ServerResponse;
import com.zenlong.study.domain.vo.ParkInfoVo;
import com.zenlong.study.service.IParkService;
import org.springframework.stereotype.Service;

/**
 * 描述:停车场接口实现
 * 项目名:my-spring-integration
 *
 * @Author:ZENLIN
 * @Created 2018/10/23  16:11.
 */
@Service
public class ParkServiceImpl implements IParkService {

    /**
     * 获取配置参数
     * @return
     */
    private boolean init(){

    }

    /**
     * 获取停车场信息列表
     *
     * @return
     */
    @Override
    public ServerResponse<ParkInfoVo> listParkInfo() {

        return null;
    }
}
