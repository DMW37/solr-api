package com.study.test;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.junit.Test;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @author: 邓明维
 * @date: 2022/10/26
 * @description:
 */
public class TestSolr {

    /**
     * 添加或修改数据 | 全量修改
     *
     * @throws SolrServerException
     * @throws IOException
     */
    @Test
    public void testAddAndUpdate() throws SolrServerException, IOException {
        // 1.创建客户端连接 // http://IP/solr/核心名称
        // 2.添加时创建Solr文档输入对象
        // 3.提交
        // 4.释放资源
        HttpSolrClient httpSolrClient = null;
        String url = "http://localhost:8983/solr/new_core";
        httpSolrClient = new HttpSolrClient.Builder(url).build();
        SolrInputDocument document = new SolrInputDocument();
        document.addField("id", "7");
        document.addField("name", "666OPPO");
        document.addField("price", 6666);
        httpSolrClient.add(document);
        httpSolrClient.commit();
        httpSolrClient.close();
    }

    /**
     * 删除数据
     * @throws SolrServerException
     * @throws IOException
     */
    @Test
    public void testDelete() throws SolrServerException, IOException {
        String url = "http://localhost:8983/solr/new_core";
        HttpSolrClient httpSolrClient = new HttpSolrClient.Builder(url).build();
        httpSolrClient.deleteById("8");
        httpSolrClient.commit();
        httpSolrClient.close();
    }

    /**
     * 查询数据
     */
    @Test
    public void testQuery() throws SolrServerException, IOException {
        // 查询数据
        String url = "http://localhost:8983/solr/new_core";
        HttpSolrClient httpSolrClient = new HttpSolrClient.Builder(url).build();

        // 查询对象
        SolrQuery solrQuery = new SolrQuery();
        // 匹配查询 *:* 高亮不能显示
        solrQuery.setQuery("name:oppo");

        // 排序 asc 升序
        solrQuery.setSort("price", SolrQuery.ORDER.asc);
        // 分页 start 表示偏移量,开始条
        solrQuery.setStart(0);
        solrQuery.setRows(5);

        // 高亮
        // 开启高亮
        solrQuery.setHighlight(true);
        // 添加高亮字段
        solrQuery.addHighlightField("name");
        // 设置匹配后的高亮样式
        solrQuery.setHighlightSimplePre("<span style='color:red'>");
        solrQuery.setHighlightSimplePost("</span>");
        QueryResponse response  = httpSolrClient.query(solrQuery);

        // 获取documentList对象
        SolrDocumentList results = response.getResults();
        System.out.println("总条数:"+results.getNumFound());
        // 获取高亮数据集合
        Map<String, Map<String, List<String>>> highlighting = response.getHighlighting();
        for (SolrDocument document : results) {
            System.out.println("id:"+document.get("id"));
            Map<String, List<String>> map = highlighting.get(document.get("id"));
            List<String> nameHighList = map.get("name");
            if (nameHighList!=null&&nameHighList.size()>0){
                System.out.println("已高亮显示:"+nameHighList.get(0));
            }else {
                System.out.println("name:"+document.get("name"));
            }
            System.out.println("price:"+document.get("price"));
            System.out.println("-----------------------------");
        }
        httpSolrClient.close();
    }
}
