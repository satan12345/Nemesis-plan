package com.able.springannocation.config;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * @author jipeng
 * @date 2019-03-01 16:55
 * @description
 */
public class BeanCycleConfigTest {
    AnnotationConfigApplicationContext applicationContext;
    @Before
    public void  init(){
        applicationContext=new AnnotationConfigApplicationContext(BeanCycleConfig.class);
        //String osName = applicationContext.getEnvironment().getProperty("os.name");
       // System.out.println("osName="+osName);
    }
    @Test
    public void test1(){
        System.out.println("容器创建完成");
        applicationContext.close();
        System.out.println("容器销毁");
    }

}