package com.smart.elasticsearch.core.result;

import java.io.Serializable;
import java.util.List;

import lombok.Builder;
import lombok.Data;

/**
 * 搜索结果
 *
 * @author gaowenming
 * @create 2018-02-03 8:48
 * @desc
 **/
@Data
@Builder
public class SmartSearchResult<T> implements Serializable {
    private static final long serialVersionUID = 2014673833701172653L;
    private long matchTotal;
    private List<T> list;

}
