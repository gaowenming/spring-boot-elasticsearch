package com.smart.elasticsearch.blog.service;

import com.smart.elasticsearch.blog.domain.Blog;
import com.smart.elasticsearch.blog.param.SearchParam;
import com.smart.elasticsearch.core.base.JestDataBaseService;
import com.smart.elasticsearch.core.result.SmartSearchResult;

/**
 * @author gaowenming
 * @create 2018-01-31 11:43
 * @desc jestSearch
 **/
public interface BlogJestService extends JestDataBaseService<Blog> {

    /**
     * 通用查询
     */
    SmartSearchResult<Blog> queryBySearchParam(SearchParam searchParam);
}
