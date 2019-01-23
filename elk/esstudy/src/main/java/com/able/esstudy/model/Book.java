package com.able.esstudy.model;


import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @author jipeng
 * @date 2019-01-23 14:52
 * @description
 */
@Data
public class Book {
    private Integer world_count;
    private String author;
    private String title;
    @DateTimeFormat(pattern ="yyyy-MM-dd")
    private Date publish_date;

}

