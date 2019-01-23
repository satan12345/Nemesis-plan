package com.able.esstudy.controller;

import com.able.esstudy.model.Book;
import com.able.esstudy.utils.JsonUtil;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author jipeng
 * @date 2019-01-23 14:00
 * @description
 */
@RestController
@RequestMapping("book")
public class BookController {
    @Resource
    TransportClient client;
    @GetMapping("{id}")
    public ResponseEntity queryById(@PathVariable("id") String id){

        GetResponse response = client.prepareGet("book", "novel", id).get();
        if (!response.isExists()) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity(response.getSource(), HttpStatus.OK);
    }
    @PostMapping("save")
    public ResponseEntity save(Book book){
        String jsonString = JsonUtil.obj2String(book);
        IndexResponse response = client.prepareIndex("book", "novel", "13").setSource(jsonString).get();

        return ResponseEntity.ok(response.getId());
    }

}

