package com.smart.elasticsearch.core.query;

public interface ConditionBuider<T> {
    ConditionChain build(T searchParam);
}
