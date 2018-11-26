package com.zenlong.study.impl;

import com.zenlong.study.IEcUserService;
import com.zenlong.study.common.ServerResponse;
import com.zenlong.study.common.constant.Constant;
import com.zenlong.study.common.security.Md5Util;
import com.zenlong.study.dao.EcUserMapper;
import com.zenlong.study.domain.po.EcUser;
import org.apache.commons.lang3.StringUtils;
import org.mybatis.generator.api.dom.java.BaseMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * @Description
 * @Project my-spring-integration
 * @Package com.zenlong.study.impl
 * @ClassName EcUserServiceImpl
 * @Author 曾灵
 * @QQ|Email 3195690389|17363645521@163.com
 * @Date 2018-09-20 23:32
 * @Version 1.0
 * @Modified By
 */
@Service
public class EcUserServiceImpl extends BaseServiceImpl implements IEcUserService {

    @Autowired
    private EcUserMapper mapper;

    @Override
    protected BaseMapper getMapper() {
        return mapper;
    }


    @Override
    public ServerResponse register(EcUser ecUser) {
        ServerResponse checkValid = checkValid(Constant.USER_NAME, ecUser.getUserName());
        if (!checkValid.isSuccess()) {
            return checkValid;
        }
        checkValid = checkValid(Constant.EMAIL, ecUser.getEmail());
        if (!checkValid.isSuccess()) {
            return checkValid;
        }
        checkValid = checkValid(Constant.PHONE, ecUser.getPhone());
        if (!checkValid.isSuccess()) {
            return checkValid;
        }
        ecUser.setRole(Constant.Role.ROLE_CUSTOMER);
        //对密码进行加密
        ecUser.setPassword(Md5Util.Md5EncodeUtf8(ecUser.getPassword()));
        ServerResponse save = super.save(ecUser);
        if (save.isSuccess()) {
            return ServerResponse.createBySuccess("注册成功");
        } else {
            return ServerResponse.createByErrorMessage("注册失败");
        }
    }

    @Override
    public ServerResponse checkValid(String type, String value) {
        if (StringUtils.isNotBlank(type)) {
            switch (type) {
                case Constant.USER_NAME:
                    int checkUserName = mapper.checkUserName(value);
                    if (checkUserName > 0) {
                        return ServerResponse.createByErrorMessage("用户名已存在");
                    } else {
                        return ServerResponse.createBySuccess();
                    }
                case Constant.EMAIL:
                    int checkEmail = mapper.checkEmail(value);
                    if (checkEmail > 0) {
                        return ServerResponse.createByErrorMessage("邮箱已存在");
                    } else {
                        return ServerResponse.createBySuccess();
                    }
                case Constant.PHONE:
                    int checkPhone = mapper.checkPhone(value);
                    if (checkPhone > 0) {
                        return ServerResponse.createByErrorMessage("手机号码已存在");
                    } else {
                        return ServerResponse.createBySuccess();
                    }
                default:
                    return ServerResponse.createByErrorMessage("校验类型不存在");
            }
        } else {
            return ServerResponse.createByErrorMessage("参数错误,校验类型不能为空");
        }
    }
}
