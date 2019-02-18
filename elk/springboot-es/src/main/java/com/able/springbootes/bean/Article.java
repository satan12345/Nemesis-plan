package com.able.springbootes.bean;

import io.searchbox.annotations.JestId;
import lombok.Data;

/**
 * @author jipeng
 * @date 2019-02-16 19:56
 * @description
 */
@Data
public class Article {
    @JestId
    private Integer id;
    private String author;
    private String title;
    /**
    *
    */
    private String content;
}

