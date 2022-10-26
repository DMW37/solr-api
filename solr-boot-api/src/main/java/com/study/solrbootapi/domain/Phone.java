package com.study.solrbootapi.domain;

import org.apache.solr.client.solrj.beans.Field;

/**
 * @author: 邓明维
 * @date: 2022/10/26
 * @description: solr对应的手机实体对象
 */
public class Phone {
    private Long id;
    // 如果是实体类，除了id之外，其它字段需要使用Field注解
    @Field
    private String name;
    @Field
    private Double price;

    @Override
    public String toString() {
        return "Phone{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", price=" + price +
                '}';
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }
}
