package com.zenlong.study.service.impl;

import com.zenlong.study.common.ServerResponse;
import com.zenlong.study.dao.CameraGroupMapper;
import com.zenlong.study.service.ICameraService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * 描述:
 * 项目名:my-spring-integration
 *
 * @Author:ZENLIN
 * @Created 2018/11/26  16:48.
 */
@Service
public class CameraServiceImpl implements ICameraService {
    @Value("${tdp.interface.camera.url}")
    private String url;
    @Autowired
    private CameraGroupMapper mapper;

    @Override
    public ServerResponse sync() {

        return null;
    }
}
