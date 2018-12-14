package com.zenlong.study.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zenlong.study.common.ServerResponse;
import com.zenlong.study.common.utils.XmlUtil;
import com.zenlong.study.constant.CameraConstant;
import com.zenlong.study.dao.CameraGroupMapper;
import com.zenlong.study.domain.po.CameraDevice;
import com.zenlong.study.domain.po.CameraGroup;
import com.zenlong.study.entity.ThirdCameraDevice;
import com.zenlong.study.entity.ThirdCameraGroup;
import com.zenlong.study.service.ICameraService;
import com.zenlong.study.webservice.ICommonServiceStub;
import com.zenlong.study.webservice.ICommonServiceStub.GetAllResourceDetail;
import com.zenlong.study.webservice.ICommonServiceStub.GetAllResourceDetailByOrg;
import com.zenlong.study.webservice.ICommonServiceStub.GetAllResourceDetailByOrgResponse;
import com.zenlong.study.webservice.ICommonServiceStub.GetAllResourceDetailResponse;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.rmi.RemoteException;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collector;
import java.util.stream.Collectors;

/**
 * 描述:
 * 项目名:my-spring-integration
 *
 * @Author:ZENLIN
 * @Created 2018/11/26  16:48.
 */
@Slf4j
@Service
@Configuration(value = "classpath:hkcamera.yml")
public class CameraServiceImpl implements ICameraService {
    @Value("${tdp.interface.camera.url-thy}")
    private String urlThy;
    @Value("${tdp.interface.camera.url-thy}")
    private String urlSgx;
    @Value("${tdp.interface.camera.url-thy}")
    private String urlGtgz;
    @Value("${tdp.interface.node-index-code-thy}")
    private String nodeIndexCodeThy;
    @Value("${tdp.interface.node-index-code-sgx}")
    private String nodeIndexCodeSgx;
    @Value("${tdp.interface.node-index-code-gtgz}")
    private String nodeIndexCodeGtgz;
    private Integer resTypeOrg = 1000;
    private Integer resTypeDto = 10000;
    @Autowired
    private CameraGroupMapper mapper;


    @Override
    @Transactional(rollbackFor = Exception.class)
    public ServerResponse sync() {

        return null;
    }


    /*private ServerResponse syncThy() {
        Map map = new HashMap();
        List<CameraGroup> groupList;
        try {
            groupList = listCameraGroup(urlThy, resTypeOrg, nodeIndexCodeThy);
        } catch (Exception e) {
            log.error("同步桃花源监控列表异常:{}", e.getMessage());
            return null;
        }
        List<CameraDevice> deviceList;
        try {
            deviceList = listCameraDevice(urlThy, resTypeOrg, nodeIndexCodeThy, groupList);
        } catch (Exception e) {
            log.error("同步桃花源监控点异常:{}", e.getMessage());
            return ServerResponse.createByErrorMessage("同步桃花源监控点异常");
        }
    }*/

    private List<CameraGroup> listCameraGroup(String url, String nodeIndexCode) throws Exception {
        ICommonServiceStub is = new ICommonServiceStub(url);
        //获取全部组织资源
        GetAllResourceDetail allResourceDetail = new GetAllResourceDetail();
        //资源类型： 1000资源组织;3000用户组织;4000应用;30000编码设备;110000解码设备;100000视频综合平台;50000监视屏组;
        allResourceDetail.setResType(resTypeOrg);
        //服务的INDEX_CODE（预留参数，调用方的indexCode，可填任意String类型值）
        allResourceDetail.setNodeIndexCode(nodeIndexCode);
        GetAllResourceDetailResponse detailResponse = is.getAllResourceDetail(allResourceDetail);
        String aReturn = detailResponse.get_return();
        log.info("组织列表响应:{}", aReturn);
        JSONObject xml2json = XmlUtil.xml2json(aReturn);
        JSONObject table = xml2json.getJSONObject("table");
        JSONObject head = table.getJSONObject("head");
        JSONObject result = head.getJSONObject("result");
        Integer resultCode = result.getInteger("result_code");
        if (CameraConstant.interfaceResponseSuccessCode.equals(resultCode)) {
            Integer size = result.getInteger("size");
            JSONArray row = new JSONArray();
            JSONObject rows = table.getJSONObject("rows");
            if (size == 0) {
                log.info("监控平台组织未添加内容");
                return new ArrayList<>();
            } else if (size == 1) {
                row.add(rows.getJSONObject("row"));
            } else {
                row = rows.getJSONArray("row");
            }
            return row.stream().map(object -> JSON.parseObject(JSON.toJSONString(object), ThirdCameraGroup.class)).map(thirdCameraGroup -> {
                String cIndexCode = thirdCameraGroup.getGroupNo();
                String orgName = thirdCameraGroup.getGroupName();
                String parentId = thirdCameraGroup.getParentNo();
                //状态(0-正常,负i_id值-删除 )
                Integer iStatus = Integer.parseInt(thirdCameraGroup.getStatus());
                CameraGroup cameraGroup = new CameraGroup();
                cameraGroup.setUuid(thirdCameraGroup.getUuid());
                cameraGroup.setGroupNo(cIndexCode);
                cameraGroup.setGroupName(orgName);
                cameraGroup.setParentNo(parentId);
                cameraGroup.setSeriesNo("HK7600");
                cameraGroup.setStatus(iStatus);
                return cameraGroup;
            }).collect(Collectors.toList());
        } else {
            String message = result.getString("message");
            log.info("message:{}", message);
            return new ArrayList<>();
        }
    }


    private Map listCameraDevice(String url, String nodeIndexCode, List<CameraGroup> listCameraGroup) throws Exception {
        List<CameraGroup> listCameraGroupNew = new ArrayList<>();
        ICommonServiceStub is = new ICommonServiceStub(url);
        List<CameraDevice> cameraDevices = listCameraGroup.stream().map(cameraGroup -> {
            GetAllResourceDetailByOrg gr = new GetAllResourceDetailByOrg();
            String groupNo = cameraGroup.getGroupNo();
            gr.setOrgCode(groupNo);
            gr.setResType(resTypeDto);
            gr.setNodeIndexCode(nodeIndexCode);
            GetAllResourceDetailByOrgResponse allResourceDetailByOrg;
            try {
                allResourceDetailByOrg = is.getAllResourceDetailByOrg(gr);
            } catch (RemoteException e) {
                log.error(e.getMessage());
                return null;
            }
            String aReturn = allResourceDetailByOrg.get_return();
            JSONObject xml2json = XmlUtil.xml2json(aReturn);
            JSONObject table = xml2json.getJSONObject("table");
            JSONObject head = table.getJSONObject("head");
            JSONObject result = head.getJSONObject("result");
            Integer size = result.getInteger("size");
            JSONArray row = new JSONArray();
            JSONObject rows = table.getJSONObject("rows");
            if (size == 0) {
                return null;
            } else if (size == 1) {
                row.add(rows.getJSONObject("row"));
            } else {
                row = rows.getJSONArray("row");
            }
            listCameraGroupNew.add(cameraGroup);
            return row.stream().map(object -> JSON.parseObject(JSON.toJSONString(object), ThirdCameraDevice.class)).map((ThirdCameraDevice json) -> {
                CameraDevice cameraDevice = new CameraDevice();
                BeanUtils.copyProperties(json, cameraDevice);
                return cameraDevice;
            }).collect(Collectors.toList());
        }).collect(Collectors.toList()).stream().filter(o -> o != null).findAny().get();
        Map<String, Collection> map = new HashMap<>(2);
        map.put("group", listCameraGroupNew);
        map.put("device", cameraDevices);
        return map;
    }


    @Test
    public void test() throws Exception {
        String url = "http://10.5.3.1/cms/services/ICommonService?wsdl";
        String node = "001058";
        List<CameraGroup> groupList = listCameraGroup(url, node);
        Map map = listCameraDevice(url, node, groupList);
        System.out.println("map = " + JSON.toJSON(map));
    }
}
