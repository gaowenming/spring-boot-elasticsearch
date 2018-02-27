package com.smart.elasticsearch.blog.query;

import com.smart.elasticsearch.blog.param.SearchParam;
import com.smart.elasticsearch.core.query.AbstractQueryBuilder;
import com.smart.elasticsearch.core.query.QueryChain;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

/**
 * @author gaowenming
 * @create 2018-02-03 10:26
 * @desc
 **/
@Service
public class BlogQueryBuilder extends AbstractQueryBuilder<SearchParam> {

    @Override
    public QueryChain build(SearchParam searchParam) {
        QueryChain queryChain = QueryChain.defaultQueryChain();

        if (StringUtils.isNotEmpty(searchParam.getCategroy())) {
            queryChain.and(QueryChain.term("categroy", searchParam.getCategroy()));
        }
        if (searchParam.getTags() != null && searchParam.getTags().length > 0) {
            queryChain.and(QueryChain.terms("tags", (Object[]) searchParam.getTags()));
        }
        if (StringUtils.isNotEmpty(searchParam.getKeyWord())) {
            QueryChain keyWordQueryChain = QueryChain.defaultQueryChain();
            keyWordQueryChain.or(QueryChain.match("content", searchParam.getKeyWord()));
            keyWordQueryChain.or(QueryChain.match("title", searchParam.getKeyWord()));
            queryChain.and(keyWordQueryChain);
        }
        if (searchParam.getMaxId() > 0 && searchParam.getMinId() > 0) {
            queryChain.and(QueryChain.rangeQueryChain("id").lte(searchParam.getMaxId()).gte(searchParam.getMinId()));
        } else if (searchParam.getMaxId() > 0) {
            queryChain.and(QueryChain.rangeQueryChain("id").lte(searchParam.getMaxId()));
        } else if (searchParam.getMinId() > 0) {
            queryChain.and(QueryChain.rangeQueryChain("id").gte(searchParam.getMinId()));
        }
        return queryChain;
    }
}
