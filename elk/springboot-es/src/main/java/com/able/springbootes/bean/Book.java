package com.able.springbootes.bean;

import lombok.Data;
import org.springframework.data.elasticsearch.annotations.Document;

/**
 * @author jipeng
 * @date 2019-02-18 8:56
 * @description
 */
@Data
@Document(indexName = "bookindex",type = "booktype")
public class Book {
    /**
     * id
     */
    private Integer id;

    /**
     * name
     */
    private String name;
    /**
     *author
     */
    private String author;
}

