package com.zenlong.study.es.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zenlong.study.common.ServerResponse;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.admin.indices.get.GetIndexRequest;
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
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.FetchSourceContext;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Component;
import org.springframework.test.context.junit4.SpringRunner;

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
        request.local(false);
        request.humanReadable(true);
        boolean exists = highLevelClient.indices().exists(request,RequestOptions.DEFAULT);
        return exists;
    }

    /**
     * 单个新增
     *
     * @param index
     * @param type
     * @param requestBody
     * @return
     */
    public ServerResponse insert(String index, String type, JSONObject requestBody) {
        IndexRequest request = new IndexRequest(index, type);
        request.source(requestBody, XContentType.JSON);
        try {
            IndexResponse indexResponse = highLevelClient.index(request);
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
            DeleteResponse delete = highLevelClient.delete(deleteRequest);
            return ServerResponse.createBySuccess();
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
    public ServerResponse bulk() {
        //BulkResponse bulkResponse = highLevelClient.bulk(request);
        return null;
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
            GetResponse documentFields = highLevelClient.get(getRequest);
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
     * @param termMap
     * @return
     */
    public ServerResponse<List<Map>> queryByTerm(String index, String type, Map<String, String> termMap) {
        return queryByTerm(index, type, termMap, Map.class);
    }

    /**
     * 根据条件查询
     *
     * @param termMap
     * @return
     */
    public <T> ServerResponse<List<T>> queryByTerm(String index, String type, Map<String, String> termMap, Class<T> tClass) {
        if (null == termMap || termMap.isEmpty()) {
            ServerResponse.createByErrorMessage("查询条件不能为空");
        }
        SearchRequest searchRequest = new SearchRequest(index, type);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        termMap.forEach((k, v) -> {
            TermQueryBuilder termQueryBuilder = QueryBuilders.termQuery(k, v);
            boolQueryBuilder.must(termQueryBuilder);
        });
        searchSourceBuilder.query(boolQueryBuilder);
        searchRequest.source(searchSourceBuilder);
        try {
            SearchResponse search = highLevelClient.search(searchRequest, RequestOptions.DEFAULT);
            SearchHits hits = search.getHits();
            List<T> collect = Stream.of(hits.getHits())
                    .map(hit -> JSON.parseObject(hit.getSourceAsString(), tClass))
                    .collect(Collectors.toList());
            return ServerResponse.createBySuccess(collect);
        } catch (IOException e) {
            log.error(e.getMessage());
            return ServerResponse.createByErrorMessage("");
        }
    }
}
