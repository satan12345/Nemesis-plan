package com.able.springannocation.config;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * @author jipeng
 * @date 2019-03-08 14:20
 * @description
 */
public class ExtConfigTest {
    ApplicationContext applicationContext;

    @Before
    public void init(){
         applicationContext=new AnnotationConfigApplicationContext(ExtConfig.class);
    }
    @Test
    public void test1(){
        //发布事件
        applicationContext.publishEvent(new ApplicationEvent("我发布的事件") {
        });
        ((AnnotationConfigApplicationContext)applicationContext).close();
    }

}