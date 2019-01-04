package com.zenlong.study.es.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.gson.JsonElement;
import com.zenlong.study.common.ServerResponse;
import com.zenlong.study.common.excpetion.CusException;
import com.zenlong.study.common.excpetion.ExceptionUtil;
import com.zenlong.study.common.utils.DateTimeUtil;
import com.zenlong.study.common.utils.GsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.admin.indices.get.GetIndexRequest;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.search.SearchHits;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 描述:
 * 项目名:my-spring-integration
 *
 * @Author:ZENLIN
 * @Created 2018/10/23  11:59.
 */
@Slf4j
@Component
public class ElasticsearchUtil {
    @Autowired
    private RestHighLevelClient highLevelClient;

    /**
     * 判断索引是否存在
     *
     * @param index
     * @return
     */
    public boolean isIndexExist(String index) {
        GetIndexRequest request = new GetIndexRequest();
        request.indices(index);
        request.humanReadable(true);
        boolean exists;
        try {
            exists = highLevelClient.indices().exists(request, RequestOptions.DEFAULT);
        } catch (IOException e) {
            throw new CusException("IOException:{}", e);
        }
        return exists;
    }

    /**
     * 单个新增
     *
     * @param index
     * @param type
     * @param data
     * @return
     */
    public ServerResponse insert(String index, String type, Object data) {
        Preconditions.checkArgument(!Strings.isNullOrEmpty(index), "index can not be empty");
        Preconditions.checkArgument(!Strings.isNullOrEmpty(type), "type can not be empty");
        //判断data
        JsonElement requestData = GsonUtil.createByDateFormat().toJsonTree(data);
        if (requestData.isJsonNull() || requestData.isJsonPrimitive() || requestData.isJsonArray()) {
            throw new IllegalArgumentException("请求参数必须是key/value结构数据体");
        }
        JSONObject json = JSONObject.parseObject(JSON.toJSONString(data));
        String id = json.get("id") == null ? "" : json.get("id").toString();
        String createTime = DateTimeUtil.getCurrentDatetime();
        if (!StringUtils.isEmpty(id)) {
            ServerResponse<JSONObject> byId = getById(index, type, id);
            if (byId.isSuccess()) {
                createTime = byId.getData().getString("create_time");
            }
        }
        json.put("create_time", createTime);
        json.put("modified_time", createTime);
        IndexRequest request = new IndexRequest(index, type);
        request.source(json, XContentType.JSON);
        try {
            IndexResponse indexResponse = highLevelClient.index(request, RequestOptions.DEFAULT);
            id = indexResponse.getId();
            return ServerResponse.createBySuccess(id);
        } catch (Exception e) {
            log.error("新增异常:{}", ExceptionUtil.hand(e));
            return ServerResponse.createByErrorMessage("新增失败");
        }
    }

    /**
     * 批量新增
     *
     * @return
     */
    public ServerResponse bulk(String index, String type, Object list) {
        Preconditions.checkArgument(!Strings.isNullOrEmpty(index), "index can not be empty");
        Preconditions.checkArgument(!Strings.isNullOrEmpty(type), "type can not be empty");
        //判断data
        JsonElement requestData = GsonUtil.createByDateFormat().toJsonTree(list);
        if (list == null || requestData.isJsonNull() || !requestData.isJsonArray()) {
            throw new IllegalArgumentException("请求参数必须是Array结构数据体");
        }
        BulkRequest bulkRequest = new BulkRequest();
        final String[] createTime = {DateTimeUtil.getCurrentDatetime()};
        List newList = (List) list;
        newList.forEach(t -> {
            JSONObject json = JSONObject.parseObject(JSON.toJSONString(t));
            String id = json.get("id") == null ? "" : json.get("id").toString();
            if (!StringUtils.isEmpty(id)) {
                ServerResponse<JSONObject> byId = getById(index, type, id);
                if (byId.isSuccess()) {
                    createTime[0] = byId.getData().getString("create_time");
                }
            }
            json.put("create_time", createTime[0]);
            json.put("modified_time", createTime[0]);
            IndexRequest indexRequest = new IndexRequest(index, type);
            indexRequest.source(json, XContentType.JSON);
            bulkRequest.add(indexRequest);
        });
        try {
            highLevelClient.bulk(bulkRequest, RequestOptions.DEFAULT);
            return ServerResponse.createBySuccess();
        } catch (Exception e) {
            log.error("批量新增异常:{}", ExceptionUtil.hand(e));
            return ServerResponse.createByErrorMessage("批量新增失败");
        }
    }

    /**
     * 根据id删除文档数据
     *
     * @param index
     * @param type
     * @param id
     * @return
     */
    public ServerResponse deletedById(String index, String type, String id) {
        DeleteRequest deleteRequest = new DeleteRequest(index, type, id);
        try {
            DeleteResponse delete = highLevelClient.delete(deleteRequest, RequestOptions.DEFAULT);
            return ServerResponse.createBySuccess(delete.getId());
        } catch (Exception e) {
            log.error("根据文档id删除文档异常:{}", ExceptionUtil.hand(e));
            return ServerResponse.createByErrorMessage("根据文档id删除文档失败");
        }
    }

    /**
     * 根据id查询
     *
     * @param index
     * @param type
     * @param id
     * @return
     */
    public ServerResponse<JSONObject> getById(String index, String type, String id) {
        GetRequest getRequest = new GetRequest(index, type, id);
        try {
            GetResponse documentFields = highLevelClient.get(getRequest, RequestOptions.DEFAULT);
            JSONObject source = (JSONObject) documentFields.getSource();
            return ServerResponse.createBySuccess(source);
        } catch (Exception e) {
            log.error("根据文档id查询文档信息异常:{}", ExceptionUtil.hand(e));
            return ServerResponse.createByErrorMessage("根据文档id查询文档信息失败");
        }
    }

    /**
     * 根据条件查询
     *
     * @param searchRequest
     * @return
     */
    public ServerResponse<List<Map>> queryByTerm(SearchRequest searchRequest) {
        log.debug("sql:{}", searchRequest);
        return queryByTerm(searchRequest, Map.class);
    }

    /**
     * 根据条件查询
     *
     * @param searchRequest
     * @param tClass
     * @param <T>
     * @return
     */
    public <T> ServerResponse<List<T>> queryByTerm(SearchRequest searchRequest, Class<T> tClass) {
        log.debug("sql:{}", searchRequest);
        SearchResponse search;
        try {
            search = highLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        } catch (Exception e) {
            log.error("根据条件查询文档信息异常:{}", ExceptionUtil.hand(e));
            return ServerResponse.createByErrorMessage("根据条件查询文档信息失败");
        }
        SearchHits hits = search.getHits();
        List<T> collect = Stream.of(hits.getHits())
                .map(hit -> {
                    String id = hit.getId();
                    String sourceAsString = hit.getSourceAsString();
                    JSONObject object = JSON.parseObject(sourceAsString);
                    object.put("id", id);
                    return JSON.toJavaObject(object, tClass);
                }).collect(Collectors.toList());
        return ServerResponse.createBySuccess(collect);
    }

    /**
     * @param searchRequest
     * @param tClass
     * @param <T>
     * @return
     */
    public <T> ServerResponse<List<T>> queryByTermNoId(SearchRequest searchRequest, Class<T> tClass) {
        log.debug("sql:{}", searchRequest);
        SearchResponse search;
        try {
            search = highLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            log.error("根据条件查询文档信息异常:{}", ExceptionUtil.hand(e));
            return ServerResponse.createByErrorMessage("根据条件查询文档信息失败");
        }
        SearchHits hits = search.getHits();
        List<T> collect = Stream.of(hits.getHits()).map(hit -> (JSON.parseObject(hit.getSourceAsString(), tClass))).collect(Collectors.toList());
        return ServerResponse.createBySuccess(collect);
    }
}
