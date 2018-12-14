package com.zenlong.study.impl;

import com.zenlong.study.common.ServerResponse;
import com.zenlong.study.common.utils.UuidUtil;
import com.zenlong.study.domain.BaseModel;
import org.mybatis.generator.api.dom.java.BaseMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * @Project spring-boot-all
 * @Package com.zenlin.mysql.web.service.impl
 * @ClassName BaseServiceImpl
 * @Author ZENLIN
 * @Date 2018-08-28 23:17
 * @Description TODO
 * @Version
 * @Modified By
 */
public abstract class BaseServiceImpl<M extends BaseMapper<T>, T extends BaseModel> {
    /**
     * 获取mapper
     *
     * @return
     */
    protected abstract M getMapper();

    /**
     * 增
     *
     * @param t
     * @return
     */
    public ServerResponse save(T t) {
        String id = UuidUtil.createId();
        t.setId(id);
        int row = getMapper().insert(t);
        if (row == 1) {
            return ServerResponse.createBySuccess(id, "保存成功");
        } else {
            return ServerResponse.createByErrorMessage("保存失败");
        }
    }

    /**
     * 批量新增
     *
     * @param list
     * @return
     */
    public ServerResponse saveBatch(List<T> list) {
        int row = getMapper().insertBatch(list);
        if (row >= 1) {
            return ServerResponse.createBySuccess("批量保存成功");
        } else {
            return ServerResponse.createByErrorMessage("批量保存失败");
        }
    }

    /**
     * 根据主键删除
     *
     * @param id
     * @return
     */
    public ServerResponse removeById(String id) {
        int row = getMapper().deleteById(id);
        if (row == 1) {
            return ServerResponse.createBySuccess(id, "删除成功");
        } else {
            return ServerResponse.createByErrorMessage("删除失败");
        }
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    public ServerResponse removeByIds(List<String> ids) {
        int row = getMapper().deleteByIds(ids);
        if (row >= 1) {
            return ServerResponse.createBySuccess("批量删除成功");
        } else {
            return ServerResponse.createByErrorMessage("批量删除失败");
        }
    }


    /**
     * 修改
     *
     * @param t
     * @return
     */
    public ServerResponse update(T t) {
        int update = getMapper().update(t);
        if (update == 1) {
            return ServerResponse.createBySuccess(t.getId(), "修改成功");
        } else {
            return ServerResponse.createByErrorMessage("修改失败");
        }
    }

    /**
     * 批量修改
     *
     * @param list
     * @return
     */
    public ServerResponse updateBatch(List<T> list) {
        int row = getMapper().updateBatch(list);
        if (row >= 1) {
            return ServerResponse.createBySuccess("批量修改成功");
        } else {
            return ServerResponse.createByErrorMessage("批量修改失败");
        }
    }


    /**
     * 根据主键查找
     *
     * @param id
     * @return
     */
    public ServerResponse getById(String id) {
        if (id == null || id.length() == 0) {
            return ServerResponse.createByErrorMessage("id is empty");
        } else {
            T byId = getMapper().findById(id);
            return ServerResponse.createBySuccess(byId, "查询成功");
        }
    }

    /**
     * 根据ids列表查找
     *
     * @param ids
     * @return
     */
    public ServerResponse listByIds(List<String> ids) {
        if (ids == null || ids.isEmpty()) {
            return ServerResponse.createByErrorMessage("ids is empty");
        } else {
            List<T> byIds = getMapper().findByIds(ids);
            return ServerResponse.createBySuccess(byIds, "查询成功");
        }
    }

    /**
     * 列表查询所有
     *
     * @return
     */
    public ServerResponse listAll() {
        List<T> all = getMapper().findAll();
        return ServerResponse.createBySuccess(all, "查询成功");
    }
}
