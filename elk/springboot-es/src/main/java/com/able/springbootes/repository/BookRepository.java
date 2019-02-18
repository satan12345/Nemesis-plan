package com.able.springbootes.repository;

import com.able.springbootes.bean.Book;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * @author jipeng
 * @date 2019-02-18 8:55
 * @description
 */

public interface BookRepository extends ElasticsearchRepository<Book,Integer> {

}

