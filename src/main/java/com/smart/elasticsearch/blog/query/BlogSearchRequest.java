package com.smart.elasticsearch.blog.query;

import com.smart.elasticsearch.blog.domain.Blog;
import com.smart.elasticsearch.blog.param.SearchParam;
import com.smart.elasticsearch.core.param.SortParam;
import com.smart.elasticsearch.core.query.QueryChain;
import com.smart.elasticsearch.core.result.SmartSearchResult;
import com.smart.elasticsearch.core.result.SmartSearchResultConverter;

import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.stereotype.Service;

import java.io.IOException;

import javax.annotation.Resource;

import io.searchbox.client.JestClient;
import io.searchbox.core.Search;
import io.searchbox.core.SearchResult;
import lombok.extern.slf4j.Slf4j;

/**
 * @author gaowenming
 * @create 2018-02-04 9:57
 * @desc
 **/
@Slf4j
@Service
public class BlogSearchRequest {

    private static final String INDEX_NAME = "smart";
    private static final String TYPE_NAME = "blog";
    @Resource
    private JestClient jestClient;
    @Resource
    private BlogQueryBuilder blogQueryBuilder;


    /**
     * searchParam搜索
     */
    public SmartSearchResult<Blog> buildSearchRequest(SearchParam searchParam) {
        SortOrder sortOrder = SortOrder.DESC;
        if (SortParam.ASC.equals(searchParam.getSortOrder())) {
            sortOrder = SortOrder.ASC;
        }
        //拼装查询条件
        QueryChain queryChain = blogQueryBuilder.build(searchParam);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.size(searchParam.getFetchSize()).sort(searchParam.getSortField(), sortOrder);
        searchSourceBuilder.query(queryChain.getQueryBuilder());
        Search search = new Search.Builder(searchSourceBuilder.toString())
                .addIndex(INDEX_NAME).addType(TYPE_NAME)
                .build();
        log.info("search query:{}", searchSourceBuilder);
        SearchResult result;
        SmartSearchResult<Blog> smartSearchResult = null;
        try {
            result = jestClient.execute(search);
            smartSearchResult = SmartSearchResultConverter.searchResultFormatter(result, Blog.class);
        } catch (IOException e) {
            log.error("query error", e);
        }
        return smartSearchResult;
    }

}
