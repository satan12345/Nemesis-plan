package com.able.springannocation.config;

import com.able.springannocation.bean.Boss;
import com.able.springannocation.bean.Car;
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
public class AutowiredConfigTest {
    AnnotationConfigApplicationContext applicationContext;
    @Before
    public void  init(){
        applicationContext=new AnnotationConfigApplicationContext(AutoWiredConfig.class);
        //String osName = applicationContext.getEnvironment().getProperty("os.name");
       // System.out.println("osName="+osName);
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

        ConfigurableEnvironment environment = applicationContext.getEnvironment();
        MutablePropertySources propertySources = environment.getPropertySources();
//        for (PropertySource<?> propertySource : propertySources) {
////            System.out.println(propertySource.getName()+":"+propertySource.getSource());
////        }
        String value = environment.getProperty("nike.name");
        System.out.println("value="+value);
    }
    @Test
    public void test2(){
        Boss bean = applicationContext.getBean(Boss.class);
        System.out.println(bean);
        Car bean1 = applicationContext.getBean(Car.class);
        System.out.println(bean1);
    }

}