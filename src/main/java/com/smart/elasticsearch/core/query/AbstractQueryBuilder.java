package com.smart.elasticsearch.core.query;

/**
 * @author gaowenming
 * @create 2018-02-03 10:22
 * @desc
 **/
public abstract class AbstractQueryBuilder<T> implements ConditionBuider<T> {
    @Override
    public abstract QueryChain build(T searchParam);
}
