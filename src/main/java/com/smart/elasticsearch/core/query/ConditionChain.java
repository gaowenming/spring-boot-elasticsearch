package com.smart.elasticsearch.core.query;

public abstract class ConditionChain {
    public static boolean isEmpty(ConditionChain conditionChain) {
        return conditionChain == null || conditionChain.isEmpty();
    }

    public abstract boolean isEmpty();

}
