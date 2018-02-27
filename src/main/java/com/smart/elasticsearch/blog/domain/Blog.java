package com.smart.elasticsearch.blog.domain;

import java.io.Serializable;
import java.util.List;

import io.searchbox.annotations.JestId;
import lombok.Data;

/**
 * @author gaowenming
 * @create 2018-01-31 15:21
 * @desc
 **/
@Data
public class Blog implements Serializable {

    private static final long serialVersionUID = -929428991889962963L;
    @JestId
    private long id;
    private String title;
    private long createTime;
    private String content;
    private List<String> tags;
    private String categroy;
    private String author;
    private String lastUpdateTime;
}
