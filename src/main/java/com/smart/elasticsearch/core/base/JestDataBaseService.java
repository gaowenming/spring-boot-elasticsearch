package com.smart.elasticsearch.core.base;

import com.smart.elasticsearch.core.result.SmartSearchResult;

import java.util.List;

/**
 * 索引中数据的操作（增删改查）
 *
 * @author gaowenming
 * @create 2018-01-31 11:43
 * @desc jestSearch
 **/
public interface JestDataBaseService<T> {

    /**
     * 单条删除
     */
    boolean deleteItem(String index, String type, String id);


    /**
     * 批量创建索引
     */
    void batchIndex(String index, String type, List<T> list);

    /**
     * 单条索引(新增/更新)
     */
    void singleIndex(String index, String type, T t);

    /**
     * 指定索引ID
     */
    void singleIndexWithId(String index, String type, String id, T t);

    /**
     * 根据id查询
     */
    T queryById(String index, String type, String id, Class<T> clazz);

    /**
     * 无过滤条件
     */
    SmartSearchResult<T> queryAll(String index, String type, int fetchSize, Class<T> clazz);

    /**
     * 设置type的mapping
     */
    String buildIndexMapping(String type);


}
