package com.able.springannocation.config;

import com.able.springannocation.bean.Boss;
import com.able.springannocation.bean.Car;
import com.google.common.collect.Lists;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MutablePropertySources;

/**
 * @author jipeng
 * @date 2019-03-01 16:55
 * @description
 */
public class ProfileConfigTest {
    AnnotationConfigApplicationContext applicationContext;
    @Before
    public void  init(){
       // applicationContext=new AnnotationConfigApplicationContext(ProfileConfig.class);
        //创建 applicationContext
        applicationContext=new AnnotationConfigApplicationContext();
        //设置需要激活的环境
        applicationContext.getEnvironment().setActiveProfiles("dev");
        //注册主配置类
        applicationContext.register(ProfileConfig.class);
        //刷新启动容器
        applicationContext.refresh();
    }
    @Test
    public void test1(){
        String[] beanDefinitionNames = applicationContext.getBeanDefinitionNames();
        for (String name : beanDefinitionNames) {
            Object bean = applicationContext.getBean(name);
            System.out.println(bean);
            System.out.println(name);
            System.out.println("========================");
        }

    }
    @Test
    public void test2(){
        Boss bean = applicationContext.getBean(Boss.class);
        System.out.println(bean);
        Car bean1 = applicationContext.getBean(Car.class);
        System.out.println(bean1);
    }

}