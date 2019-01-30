package com.able.springboot.restfulstudy.com.able.bean;

import lombok.Data;

/**
 * @author jipeng
 * @date 2019-01-30 9:10
 * @description
 */
@Data
public class Message {
    private Long id;
    private String text;
    private String summary;
}

