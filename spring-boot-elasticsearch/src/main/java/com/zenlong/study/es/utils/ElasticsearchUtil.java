package com.zenlong.study.es.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zenlong.study.common.ServerResponse;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.admin.indices.get.GetIndexRequest;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
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
    public boolean isIndexExist(String index) throws Exception {
        GetIndexRequest request = new GetIndexRequest();
        request.indices(index);
        request.humanReadable(true);
        boolean exists = highLevelClient.indices().exists(request, RequestOptions.DEFAULT);
        return exists;
    }

    /**
     * 单个新增
     *
     * @param index
     * @param type
     * @param t
     * @return
     */
    public <T> ServerResponse insert(String index, String type, T t) {
        IndexRequest request = new IndexRequest(index, type);
        request.source(String.valueOf(t), XContentType.JSON);
        try {
            IndexResponse indexResponse = highLevelClient.index(request, RequestOptions.DEFAULT);
            String id = indexResponse.getId();
            return ServerResponse.createBySuccess(id);
        } catch (IOException e) {
            log.error(e.getMessage());
            return ServerResponse.createByErrorMessage(e.getMessage());
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
        } catch (IOException e) {
            log.error(e.getMessage());
            return ServerResponse.createByErrorMessage(e.getMessage());
        }
    }

    /**
     * 批量新增
     *
     * @return
     */
    public <T> ServerResponse bulk(String index, String type, List<T> list) {
        BulkRequest bulkRequest = new BulkRequest();
        list.stream().forEach(t -> {
            IndexRequest indexRequest = new IndexRequest(index, type);
            indexRequest.source(String.valueOf(t), XContentType.JSON);
            bulkRequest.add(indexRequest);
        });
        try {
            BulkResponse bulk = highLevelClient.bulk(bulkRequest, RequestOptions.DEFAULT);
            return ServerResponse.createBySuccess(bulk.hasFailures());
        } catch (IOException e) {
            log.error(e.getMessage());
            return ServerResponse.createByErrorMessage(e.getMessage());
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
    public ServerResponse getById(String index, String type, String id) {
        GetRequest getRequest = new GetRequest(index, type, id);
        try {
            GetResponse documentFields = highLevelClient.get(getRequest, RequestOptions.DEFAULT);
            Map<String, Object> source = documentFields.getSource();
            return ServerResponse.createBySuccess(source);
        } catch (IOException e) {
            log.error(e.getMessage());
            return ServerResponse.createByErrorMessage(e.getMessage());
        }
    }

    /**
     * 根据条件查询
     *
     * @param searchRequest
     * @return
     */
    public ServerResponse<List<Map>> queryByTerm(SearchRequest searchRequest) throws IOException {
        return queryByTerm(searchRequest, Map.class);
    }

    public <T> ServerResponse<List<T>> queryByTerm(SearchRequest searchRequest, Class<T> tClass) throws IOException {
        SearchResponse search = highLevelClient.search(searchRequest, RequestOptions.DEFAULT);
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

    public <T> ServerResponse<List<T>> queryByTermNoId(SearchRequest searchRequest, Class<T> tClass) throws IOException {
        SearchResponse search = highLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        SearchHits hits = search.getHits();
        List<T> collect = Stream.of(hits.getHits()).map(hit -> (JSON.parseObject(hit.getSourceAsString(), tClass))).collect(Collectors.toList());
        return ServerResponse.createBySuccess(collect);
    }
}
