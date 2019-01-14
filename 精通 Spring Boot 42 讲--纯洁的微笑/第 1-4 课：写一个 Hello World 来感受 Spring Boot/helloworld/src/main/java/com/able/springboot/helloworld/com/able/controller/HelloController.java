package com.able.springboot.helloworld.com.able.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author jipeng
 * @date 2019-01-14 16:45
 * @description
 */
@RestController
public class HelloController {
    @RequestMapping("/hello")
    public String hello(String name,String age) {
        return "hello world,"+name+",--age="+age;
    }
}

