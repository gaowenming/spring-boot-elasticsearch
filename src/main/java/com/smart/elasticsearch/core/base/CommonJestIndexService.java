package com.smart.elasticsearch.core.base;

import com.google.gson.JsonObject;

import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;

import javax.annotation.Resource;

import io.searchbox.client.JestClient;
import io.searchbox.client.JestResult;
import io.searchbox.client.JestResultHandler;
import io.searchbox.indices.ClearCache;
import io.searchbox.indices.CreateIndex;
import io.searchbox.indices.DeleteIndex;
import io.searchbox.indices.IndicesExists;
import io.searchbox.indices.Optimize;
import io.searchbox.indices.mapping.GetMapping;
import io.searchbox.indices.mapping.PutMapping;
import lombok.extern.slf4j.Slf4j;

/**
 * 索引操作
 *
 * @author gaowenming
 * @create 2018-02-01 17:45
 * @desc
 **/
@Service
@Slf4j
public class CommonJestIndexService {
    @Resource
    private JestClient jestClient;

    /**
     * 创建index
     */
    public void createIndex(String index) {
        try {
            JestResult jestResult = jestClient.execute(new CreateIndex.Builder(index).build());
            log.info("createIndex:{}", jestResult.isSucceeded());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * （设置数据类型和分词方式）
     *
     * 设置index的mapping
     */
    public void createIndexMapping(String index, String type, String mappingString) {
        PutMapping.Builder builder = new PutMapping.Builder(index, type, mappingString);
        try {
            JestResult jestResult = jestClient.execute(builder.build());
            log.info("createIndexMapping result:{}", jestResult.isSucceeded());
            if (!jestResult.isSucceeded()) {
                log.error("settingIndexMapping error:{}", jestResult.getErrorMessage());
            }
        } catch (IOException e) {
            log.error("settingIndexMapping error:", e);
        }
    }

    /**
     * 追加mapping字段
     *
     * @param index     index名称
     * @param type      type名称
     * @param fieldName 字段名称
     * @param fieldType 字段类型
     * @param analyze   是否分词
     */
    public void addFieldMapping(String index, String type, String fieldName, String fieldType, boolean analyze) {
        String mapping;
        XContentBuilder mapBuilder = null;
        try {
            mapBuilder = XContentFactory.jsonBuilder();
            //设置分词
            if (analyze) {
                mapBuilder.startObject()
                        .startObject(type)
                        .startObject("properties")
                        .startObject(fieldName).field("type", fieldType).field("analyzer", "ik_max_word").field("store", "yes").endObject()
                        .endObject()
                        .endObject()
                        .endObject();
            } else {
                mapBuilder.startObject()
                        .startObject(type)
                        .startObject("properties")
                        .startObject(fieldName).field("type", fieldType).field("index", "not_analyzed").field("store", "yes").endObject()
                        .endObject()
                        .endObject()
                        .endObject();
            }
            mapping = mapBuilder.string();
            PutMapping.Builder builder = new PutMapping.Builder(index, type, mapping);
            JestResult jestResult = jestClient.execute(builder.build());
            log.info("addFieldMapping result:{}", jestResult.isSucceeded());
            if (!jestResult.isSucceeded()) {
                log.error("addFieldMapping error:{}", jestResult.getErrorMessage());
            }
        } catch (IOException e) {
            log.error("addFieldMapping error", e);
        }

    }


    /**
     * 追加mapping字段
     *
     * @param index     index名称
     * @param type      type名称
     * @param fieldName 字段名称
     * @param format    日期格式
     */
    public void addDateFieldMapping(String index, String type, String fieldName, String format) {
        String mapping;
        XContentBuilder mapBuilder = null;
        try {
            mapBuilder = XContentFactory.jsonBuilder();

            mapBuilder.startObject()
                    .startObject(type)
                    .startObject("properties")
                    .startObject(fieldName).field("type", "date").field("format", format).field("store", "yes").endObject()
                    .endObject()
                    .endObject()
                    .endObject();

            mapping = mapBuilder.string();
            PutMapping.Builder builder = new PutMapping.Builder(index, type, mapping);
            JestResult jestResult = jestClient.execute(builder.build());
            log.info("addDateFieldMapping result:{}", jestResult.isSucceeded());
            if (!jestResult.isSucceeded()) {
                log.error("addDateFieldMapping error:{}", jestResult.getErrorMessage());
            }
        } catch (IOException e) {
            log.error("addDateFieldMapping error", e);
        }

    }

    /**
     * 获取index的mapping
     */
    public String getMapping(String indexName, String typeName) {
        GetMapping.Builder builder = new GetMapping.Builder();
        builder.addIndex(indexName).addType(typeName);
        try {
            JestResult result = jestClient.execute(builder.build());
            if (result != null && result.isSucceeded()) {
                return result.getSourceAsObject(JsonObject.class).toString();
            }
        } catch (Exception e) {
            log.error("getMapping error", e);
        }
        return null;
    }


    /**
     * 判断index是否存在
     */
    public boolean indexExist(String index) {
        IndicesExists indicesExists = new IndicesExists.Builder(index).build();
        try {
            JestResult jestResult = jestClient.execute(indicesExists);
            if (jestResult != null) {
                return jestResult.isSucceeded();
            }
        } catch (IOException e) {
            log.error("indexExist error", e);
        }
        return false;
    }

    /**
     * 删除index
     */
    public void deleteIndex(String index) {
        try {
            JestResult jestResult = jestClient.execute(new DeleteIndex.Builder(index).build());
            log.info("deleteIndex result:{}", jestResult.isSucceeded());
        } catch (IOException e) {
            log.error("deleteIndex error", e);
        }

    }

    /**
     * 索引优化
     */
    public void optimizeIndex() {
        Optimize optimize = new Optimize.Builder().build();
        jestClient.executeAsync(optimize, new JestResultHandler<JestResult>() {
            @Override
            public void completed(JestResult jestResult) {
                log.info("optimizeIndex result:{}", jestResult.isSucceeded());
            }

            @Override
            public void failed(Exception e) {
                log.error("optimizeIndex error", e);
            }
        });
    }

    /**
     * 清理缓存
     */
    public void clearCache() {
        try {
            ClearCache clearCache = new ClearCache.Builder().build();
            jestClient.execute(clearCache);
        } catch (IOException e) {
            log.error("clearCache error", e);
        }
    }

}
