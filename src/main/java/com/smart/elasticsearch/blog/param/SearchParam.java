package com.smart.elasticsearch.blog.param;

import com.smart.elasticsearch.core.param.SortParam;

import java.io.Serializable;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author gaowenming
 * @create 2018-02-02 10:57
 * @desc 搜索公共参数
 **/
@EqualsAndHashCode(callSuper = true)
@Data
public class SearchParam extends SortParam implements Serializable {
    private static final long serialVersionUID = -2009509747353786691L;
    //最小ID
    private long minId = 0L;
    //最大ID
    private long maxId = 0L;
    //条数
    private int fetchSize = 10;
    //标签
    private String[] tags;
    //分类
    private String categroy;
    //关键字(title和content)
    private String keyWord;

}
