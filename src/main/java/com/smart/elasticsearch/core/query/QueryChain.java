package com.smart.elasticsearch.core.query;

import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;

/**
 * 拼装query
 */
public class QueryChain extends ConditionChain {

    private QueryBuilder queryBuilder;

    public static QueryChain defaultQueryChain() {
        return new QueryChain();
    }

    public static QueryChain rangeQueryChain(String field) {
        QueryChain queryChain = defaultQueryChain();
        queryChain.queryBuilder = QueryBuilders.rangeQuery(field);
        return queryChain;
    }

    private void ensureBoolQueryBuilder() {
        if (queryBuilder == null) {
            queryBuilder = QueryBuilders.boolQuery().disableCoord(true);
        }
        if (!(queryBuilder instanceof BoolQueryBuilder)) {
            throw new UnsupportedOperationException();
        }
    }

    public QueryChain or(QueryChain subQueryChain) {
        if (ConditionChain.isEmpty(subQueryChain)) {
            return this;
        }
        ensureBoolQueryBuilder();
        ((BoolQueryBuilder) queryBuilder).should(subQueryChain.queryBuilder);
        return this;
    }

    public QueryChain and(QueryChain subQueryChain) {
        if (ConditionChain.isEmpty(subQueryChain)) {
            return this;
        }
        ensureBoolQueryBuilder();
        ((BoolQueryBuilder) queryBuilder).must(subQueryChain.queryBuilder);
        return this;
    }

    public QueryChain not(QueryChain subQueryChain) {
        if (ConditionChain.isEmpty(subQueryChain)) {
            return this;
        }
        ensureBoolQueryBuilder();
        ((BoolQueryBuilder) queryBuilder).mustNot(subQueryChain.queryBuilder);
        return this;
    }

    @Override
    public boolean isEmpty() {
        if (queryBuilder == null) {
            return true;
        }
        return queryBuilder instanceof BoolQueryBuilder
                && !((BoolQueryBuilder) queryBuilder).hasClauses();
    }

    public static QueryChain term(String field, Object value) {
        QueryChain queryChain = new QueryChain();
        queryChain.queryBuilder = QueryBuilders.termQuery(field, value);
        return queryChain;
    }

    public static QueryChain terms(String field, Object... values) {
        QueryChain queryChain = new QueryChain();
        queryChain.queryBuilder = QueryBuilders.termsQuery(field, values);
        return queryChain;
    }

    //对查询关键字分词
    public static QueryChain match(String field, Object value) {
        QueryChain queryChain = new QueryChain();
        queryChain.queryBuilder = QueryBuilders.matchQuery(field, value);
        return queryChain;
    }

    //不会分词
    public static QueryChain matchPhrase(String field, Object value) {
        QueryChain queryChain = new QueryChain();
        queryChain.queryBuilder = QueryBuilders.matchPhraseQuery(field, value);
        return queryChain;
    }

    //不会分词
    public static QueryChain matchPhrasePrefix(String field, Object value) {
        QueryChain queryChain = new QueryChain();
        queryChain.queryBuilder = QueryBuilders.matchPhrasePrefixQuery(field, value);
        return queryChain;
    }

    private void ensureRangeQuery() {
        if (queryBuilder == null || !(queryBuilder instanceof RangeQueryBuilder)) {
            throw new UnsupportedOperationException();
        }
    }

    //>=
    public QueryChain gte(Object value) {
        ensureRangeQuery();
        ((RangeQueryBuilder) queryBuilder).gte(value);
        return this;
    }

    //>
    public QueryChain gt(Object value) {
        ensureRangeQuery();
        ((RangeQueryBuilder) queryBuilder).gt(value);
        return this;
    }

    //<=
    public QueryChain lte(Object value) {
        ensureRangeQuery();
        ((RangeQueryBuilder) queryBuilder).lte(value);
        return this;
    }

    //<
    public QueryChain lt(Object value) {
        ensureRangeQuery();
        ((RangeQueryBuilder) queryBuilder).lt(value);
        return this;
    }

    public QueryBuilder getQueryBuilder() {
        return queryBuilder;
    }
}
