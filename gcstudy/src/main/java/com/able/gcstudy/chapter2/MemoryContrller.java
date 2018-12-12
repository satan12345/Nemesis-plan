package com.able.gcstudy.chapter2;

import com.google.common.collect.Lists;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

/**
 * @author jipeng
 * @date 2018-12-11 18:14
 * @description
 */
@RestController
public class MemoryContrller {
    List<byte[]> lists= Lists.newArrayList();
    List<Class<?>> classList=Lists.newArrayList();
    List<User> userList=Lists.newArrayList();
    /**
     * -Xmx32M
     * -Mms32M
     * java.lang.OutOfMemoryError: Java heap space
     */
    @GetMapping("/heap")
    public void heap(){

        while (true) {
            lists.add(new byte[1024*1024]);
        }
    }
    @GetMapping("/heap1")
    public void heap1(){
        while (true) {
            userList.add(new User(UUID.randomUUID().toString(), UUID.randomUUID().toString()));
        }
    }



    /**
     * -XX:MetaspaceSize=32M
     * -XX:MaxMetaspaceSize=32M
     * java.lang.OutOfMemoryError: Metaspace
     */
    @GetMapping("/noheap")
    public void noheap(){
        while (true) {

            classList.addAll(Metaspace.createClasses());
        }
    }

}

