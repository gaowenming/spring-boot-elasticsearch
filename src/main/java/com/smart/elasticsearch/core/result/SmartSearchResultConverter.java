package com.smart.elasticsearch.core.result;

import com.google.common.collect.Lists;

import java.util.List;

import io.searchbox.core.SearchResult;
import lombok.extern.slf4j.Slf4j;

/**
 * @author gaowenming
 * @create 2018-02-04 10:05
 * @desc
 **/
@Slf4j
public class SmartSearchResultConverter {
    /**
     * 查询结果数据格式化
     */
    public static <T> SmartSearchResult<T> searchResultFormatter(SearchResult searchResult, Class<T> clazz) {
        List<SearchResult.Hit<T, Void>> hits;
        List<T> list = Lists.newArrayList();
        SmartSearchResult<T> smartSearchResult;
        long total = searchResult.getTotal();
        log.info("本次查询共查到：{} 个结果", searchResult.getTotal());
        hits = searchResult.getHits(clazz);
        for (SearchResult.Hit<T, Void> hit : hits) {
            T t = hit.source;
            list.add(t);
        }
        smartSearchResult = SmartSearchResult.<T>builder().list(list).matchTotal(total).build();
        return smartSearchResult;
    }
}
