package com.able.springbootes.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author jipeng
 * @date 2019-02-22 15:31
 * @description
 */
@RestController
@Slf4j
public class MyController {
    @GetMapping("index")
    public String index(String name,Integer age){
        log.info("index");
        int a=1/0;
        return "你好 "+name+":age="+age;
    }
}

