package com.smart;

import com.google.common.collect.Lists;

import com.smart.elasticsearch.blog.domain.Blog;
import com.smart.elasticsearch.blog.param.SearchParam;
import com.smart.elasticsearch.blog.service.BlogJestService;
import com.smart.elasticsearch.core.base.CommonJestIndexService;
import com.smart.elasticsearch.core.param.SortParam;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import lombok.extern.slf4j.Slf4j;

/**
 * @author gaowenming
 *
 * 2018-01-31 15:16
 **/
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@Slf4j
public class JestServiceTest {

    private static final String INDEX_NAME = "smart";
    private static final String TYPE_NAME = "blog";

    @Resource
    private BlogJestService blogJestService;


    @Resource
    private CommonJestIndexService commonJestIndexService;

    @Test
    public void testBatchIndex() {
        List<Blog> blogList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Blog blog = new Blog();
            blog.setCategroy("后端");
            blog.setContent("说到写if else，大多数程序员可能都会会心一笑，毕竟不论写啥系统，代码里大部分语句还是if else。业务系统就是对PM的需求堆if else，基础服务就是对OS资源和网络问题堆if else。越来越多的需求造成了代码的膨胀，如何来管理这坨if else，就衍生出了设计模式：通过各种手段来把if else划分到更小的粒度。当然，还没有任何一套方法论可以解决if else复杂性的问题，因为if else反映的其实是真实业务的需求，它其实代表了业务的复杂性。就像鲁迅或者马云或者巴菲特说的：“不论用什么方法，if else不会凭空消失，只会从一个地方转移到另一个地方”。但是，在面对特定的场景，特定的业务，代码的阅读性、可维护性以及扩展性是可以得到提升的");
            blog.setCreateTime(System.currentTimeMillis());
            blog.setId(i);
            blog.setTags(Lists.newArrayList("Go", "代码"));
            blog.setTitle("拯救你的Go代码");
            blogList.add(blog);
        }
        blogJestService.batchIndex(INDEX_NAME, TYPE_NAME, blogList);
    }

    @Test
    public void testSingleIndex() {
        Blog blog = new Blog();
        blog.setCategroy("前端");
        blog.setContent("场景：在访问页面的时候如果图片较大或者网速慢的情况我们会看到图片加载起来是有一个明显过程的，就是从上到下逐行呈现出来，这给用户的信号就是再等等图片还在加载中，并且图片没有加载出来的部分呈现一片空白，不利于体验，下面就看看渐进式图片怎么缓解这一情况");
        blog.setCreateTime(System.currentTimeMillis());
        blog.setId(2);
        blog.setTags(Lists.newArrayList("图片", "技巧"));
        blog.setTitle("页面体验提升小技巧—渐进式图片");
        blog.setAuthor("小鱼");
        //blog.setLastUpdateTime();
        blogJestService.singleIndex(INDEX_NAME, TYPE_NAME, blog);
    }

    @Test
    public void testSingleIndexId() {
        Blog blog = new Blog();
        blog.setCategroy("技术内容");
        blog.setContent("我的博客");
        blog.setCreateTime(System.currentTimeMillis());
        blog.setId(100);
        blog.setTags(Lists.newArrayList("爱情", "体育"));
        blog.setTitle("第一篇博客");
        blog.setLastUpdateTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        blogJestService.singleIndexWithId(INDEX_NAME, TYPE_NAME, "100", blog);
    }

    @Test
    public void testIndexExist() {
        boolean result = commonJestIndexService.indexExist(INDEX_NAME);
        log.info("indexExist:{}", result);
    }

    @Test
    public void testDeleteIndex() {
        commonJestIndexService.deleteIndex(INDEX_NAME);
    }

    @Test
    public void testCreateIndex() {
        commonJestIndexService.createIndex(INDEX_NAME);
    }

    @Test
    public void testCreateIndexMapping() {
        String mapping = blogJestService.buildIndexMapping(TYPE_NAME);
        commonJestIndexService.createIndexMapping(INDEX_NAME, TYPE_NAME, mapping);
    }

    @Test
    public void testGetIndexMapping() {
        String mapping = commonJestIndexService.getMapping(INDEX_NAME, TYPE_NAME);
        log.info(mapping);
    }

    @Test
    public void testAddIndexMapping() {
        commonJestIndexService.addFieldMapping(INDEX_NAME, TYPE_NAME, "author", "keyword", false);
    }

    @Test
    public void testAddDateIndexMapping() {
        commonJestIndexService.addDateFieldMapping(INDEX_NAME, TYPE_NAME, "lastUpdateTime", "yyyy-MM-dd HH:mm:ss||yyyy-MM-dd||epoch_millis");
    }

    @Test
    public void testDeleteItem() {
        boolean result = blogJestService.deleteItem(INDEX_NAME, TYPE_NAME, "1");
        log.info("deleteItem:{}", result);
    }


    @Test
    public void testUpdateItem() {
        //更新直接覆盖就行
        Blog blog = new Blog();
        blog.setCategroy("娱乐git");
        blog.setContent("我的博客2");
        blog.setCreateTime(System.currentTimeMillis());
        blog.setId(8);
        blog.setTags(Lists.newArrayList("科技", "汽车"));
        blog.setTitle("第一篇博客2");
        blogJestService.singleIndex(INDEX_NAME, TYPE_NAME, blog);
    }

    @Test
    public void testQueryById() {
        Blog result = blogJestService.queryById(INDEX_NAME, TYPE_NAME, "5", Blog.class);
        log.info("testQueryById:{}", result);
    }

    @Test
    public void testOptimize() {
        commonJestIndexService.optimizeIndex();
    }

    @Test
    public void testClear() {
        commonJestIndexService.clearCache();
    }

    @Test
    public void testQueryAll() {
        log.info("queryAll：{}", blogJestService.queryAll(INDEX_NAME, TYPE_NAME, 5, Blog.class));
    }

    @Test
    public void testQueryBySearchParam() {
        SearchParam searchParam = new SearchParam();
        //searchParam.setCategroy("后端");
        searchParam.setKeyWord("页面");
        //searchParam.setTags(new String[]{"Go","lua"});
        searchParam.setSortOrder(SortParam.ASC);
        searchParam.setMaxId(100);
        searchParam.setMinId(1);
        log.info("testQueryBySearchParam：{}", blogJestService.queryBySearchParam(searchParam));
    }


}
