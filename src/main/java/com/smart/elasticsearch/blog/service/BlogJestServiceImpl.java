package com.smart.elasticsearch.blog.service;

import com.smart.elasticsearch.blog.domain.Blog;
import com.smart.elasticsearch.blog.param.SearchParam;
import com.smart.elasticsearch.blog.query.BlogSearchRequest;
import com.smart.elasticsearch.core.base.AbstractJestDataBaseService;
import com.smart.elasticsearch.core.result.SmartSearchResult;

import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;

import javax.annotation.Resource;

import lombok.extern.slf4j.Slf4j;

/**
 * @author gaowenming
 * @create 2018-02-01 15:13
 * @desc
 **/
@Slf4j
@Service("blogJestService")
public class BlogJestServiceImpl extends AbstractJestDataBaseService<Blog> implements BlogJestService {

    @Resource
    private BlogSearchRequest blogSearchRequest;

    @Override
    public String buildIndexMapping(String type) {
        String mapping = "";
        XContentBuilder mapBuilder = null;
        try {
            mapBuilder = XContentFactory.jsonBuilder();
            mapBuilder.startObject()
                    .startObject(type)
                    .startObject("properties")
                    .startObject("id").field("type", "long").field("store", "yes").endObject()
                    //设置分词
                    .startObject("title").field("type", "string").field("analyzer", "ik_max_word").field("store", "no").endObject()
                    .startObject("content").field("type", "string").field("analyzer", "ik_max_word").field("store", "yes").endObject()
                    //不分词
                    .startObject("author").field("type", "string").field("index", "not_analyzed").field("store", "no").endObject()
                    .startObject("tags").field("type", "string").field("index", "not_analyzed").field("store", "no").endObject()
                    .startObject("categroy").field("type", "string").field("index", "not_analyzed").field("store", "no").endObject()
                    .startObject("createTime").field("type", "long").field("index", "not_analyzed").field("store", "no").endObject()
                    .startObject("lastUpdateTime").field("type", "date").field("format", "yyyy-MM-dd HH:mm:ss").field("store", "no").endObject()
                    .endObject()
                    .endObject()
                    .endObject();
            mapping = mapBuilder.string();
        } catch (IOException e) {
            log.error("buildIndexMapping error", e);
        }
        return mapping;
    }

    @Override
    public SmartSearchResult<Blog> queryBySearchParam(SearchParam searchParam) {
        return blogSearchRequest.buildSearchRequest(searchParam);
    }
}
