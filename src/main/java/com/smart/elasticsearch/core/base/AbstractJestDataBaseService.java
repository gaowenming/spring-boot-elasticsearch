package com.smart.elasticsearch.core.base;

import com.smart.elasticsearch.core.result.SmartSearchResult;
import com.smart.elasticsearch.core.result.SmartSearchResultConverter;

import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortOrder;

import java.io.IOException;
import java.util.List;

import javax.annotation.Resource;

import io.searchbox.client.JestClient;
import io.searchbox.client.JestResult;
import io.searchbox.core.Bulk;
import io.searchbox.core.Delete;
import io.searchbox.core.Get;
import io.searchbox.core.Index;
import io.searchbox.core.Search;
import io.searchbox.core.SearchResult;
import lombok.extern.slf4j.Slf4j;

/**
 * elasticsearch通用操作
 *
 * @author gaowenming
 * @create 2018-02-01 14:21
 * @desc
 **/
@Slf4j
public abstract class AbstractJestDataBaseService<T> implements JestDataBaseService<T> {

    @Resource
    private JestClient jestClient;

    @Override
    public boolean deleteItem(String index, String type, String id) {
        try {
            JestResult jestResult = jestClient.execute(new Delete.Builder(id).index(index).type(type).refresh(true).build());
            if (!jestResult.isSucceeded()) {
                log.error("deleteItem error:{}", jestResult.getErrorMessage());
            }
            return jestResult.isSucceeded();
        } catch (IOException e) {
            log.error("deleteItem error", e);
        }
        return false;
    }


    @Override
    public void batchIndex(String index, String type, List<T> list) {
        try {
            Bulk.Builder builder = new Bulk.Builder();
            for (T t : list) {
                builder.addAction(new Index.Builder(t).index(index).type(type).build());
            }
            JestResult jestResult = jestClient.execute(builder.build());
            if (!jestResult.isSucceeded()) {
                log.error("batchIndex error:{}", jestResult.getErrorMessage());
            }
        } catch (IOException e) {
            log.error("batchIndex error", e);
        }
    }

    @Override
    public void singleIndex(String index, String type, T t) {
        try {
            JestResult jestResult = jestClient.execute(new Index.Builder(t).index(index).type(type).build());
            if (!jestResult.isSucceeded()) {
                log.error("singleIndex error:{}", jestResult.getErrorMessage());
            }
        } catch (IOException e) {
            log.error("singleIndex error", e);
        }
    }

    @Override
    public T queryById(String index, String type, String id, Class<T> clazz) {
        T result = null;
        try {
            Get get = new Get.Builder(index, id).type(type).build();
            JestResult jestResult = jestClient.execute(get);
            result = jestResult.getSourceAsObject(clazz);
        } catch (IOException e) {
            log.error("queryById error", e);
        }
        return result;
    }

    @Override
    public void singleIndexWithId(String index, String type, String id, T t) {
        try {
            JestResult jestResult = jestClient.execute(new Index.Builder(t).index(index).type(type).id(id).build());
            if (!jestResult.isSucceeded()) {
                log.error("singleIndexWithId error:{}", jestResult.getErrorMessage());
            }
        } catch (IOException e) {
            log.error("singleIndexWithId error", e);
        }
    }

    @Override
    public SmartSearchResult<T> queryAll(String index, String type, int fetchSize, Class<T> clazz) {
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(QueryBuilders.matchAllQuery()).size(fetchSize).sort("id", SortOrder.DESC);
        Search search = new Search.Builder(searchSourceBuilder.toString())
                .addIndex(index).addType(type)
                .build();
        log.info("search query:{}", searchSourceBuilder);
        SearchResult result;
        SmartSearchResult<T> smartSearchResult = null;
        try {
            result = jestClient.execute(search);
            smartSearchResult = SmartSearchResultConverter.searchResultFormatter(result, clazz);
        } catch (IOException e) {
            log.error("queryAll error", e);
        }
        return smartSearchResult;
    }

}
