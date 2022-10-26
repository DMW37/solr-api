package com.study.solrbootapi;

import com.study.solrbootapi.domain.Phone;
import org.apache.solr.common.SolrInputDocument;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Sort;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.core.query.Criteria;
import org.springframework.data.solr.core.query.HighlightOptions;
import org.springframework.data.solr.core.query.SimpleHighlightQuery;
import org.springframework.data.solr.core.query.result.HighlightEntry;
import org.springframework.data.solr.core.query.result.HighlightPage;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
class SolrBootApiApplicationTests {

    @Resource
    private SolrTemplate solrTemplate;

    /**
     * saveDocument
     */
    @Test
    public void testAddDocument(){
        SolrInputDocument solrInputDocument = new SolrInputDocument();
        solrInputDocument.addField("id","9");
        solrInputDocument.addField("name","国产RealMe手机");
        solrInputDocument.addField("price",1234);
        solrTemplate.saveDocument("new_core",solrInputDocument);
        solrTemplate.commit("new_core");
    }

    /**
     * saveBean
     */
    @Test
    public void testSaveBean(){
        Phone phone = new Phone();
        phone.setId(10L);
        phone.setName("RealMe手机");
        phone.setPrice(132514D);
        // 如果是实体类，除了id之外，其它字段需要使用Field注解
        solrTemplate.saveBean("new_core",phone);
        solrTemplate.commit("new_core");
    }

    /**
     * 根据ID删除
     */
    @Test
    public void testDelete(){
        solrTemplate.deleteByIds("new_core","10");
        solrTemplate.commit("new_core");
    }

    /**
     * 查询
     */
    @Test
    public void testQuery(){
        SimpleHighlightQuery query = new SimpleHighlightQuery();
        Criteria criteria = new Criteria("name");
        criteria.is("手机");
        query.addCriteria(criteria);

        // 分页
        query.setOffset(0L);
        query.setRows(3);

        // 排序
        Sort sort = Sort.by(Sort.Direction.DESC, "price");
        query.addSort(sort);

        // 高亮
        HighlightOptions highlightOptions = new HighlightOptions();
        highlightOptions.addField("name");
        highlightOptions.setSimplePrefix("<span style='color:red'>");
        highlightOptions.setSimplePostfix("</span>");
        query.setHighlightOptions(highlightOptions);

        HighlightPage<Phone> highlightPage = solrTemplate.queryForHighlightPage("new_core", query, Phone.class);
        System.out.println("---------------------");
        System.out.println(highlightPage.getContent());
        System.out.println("---------------------");
        List<HighlightEntry<Phone>> highlighted = highlightPage.getHighlighted();

        List<Phone> list = new ArrayList<>();

        for (HighlightEntry<Phone> phoneHighlightEntry : highlighted) {
            List<HighlightEntry.Highlight> highlights = phoneHighlightEntry.getHighlights();
            Phone entity = phoneHighlightEntry.getEntity();
            //一个对象里面可能有多个属性是高亮
            for (HighlightEntry.Highlight highlight : highlights) {
                if (highlight.getField().getName().equals("name")){
                    entity.setName(highlight.getSnipplets().get(0));
                }
            }
            list.add(entity);
        }

        System.out.println(list);
    }

}
