package com.able.springannocation;

import com.able.springannocation.bean.Bule;
import com.able.springannocation.bean.ColorFactoryBean;
import com.able.springannocation.bean.Person;
import com.able.springannocation.bean.RainBow;
import com.able.springannocation.config.IocConfig;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.Map;

/**
 * @author jipeng
 * @date 2019-03-01 11:29
 * @description
 */
public class IOCTest {
    ApplicationContext applicationContext;
    @Before
    public void  init(){
        applicationContext=new AnnotationConfigApplicationContext(IocConfig.class);
        String osName = applicationContext.getEnvironment().getProperty("os.name");
        System.out.println("osName="+osName);

    }

    @Test
    public void test1(){

        String[] beanDefinitionNames = applicationContext.getBeanDefinitionNames();
        for (String name : beanDefinitionNames) {
            System.out.println(name);
        }

    }
    @Test
    public void testConditional(){
        String[] beanNamesForType = applicationContext.getBeanNamesForType(Person.class);
        for (String name : beanNamesForType) {
            System.out.println(name);
        }
        Map<String, Person> beansOfType = applicationContext.getBeansOfType(Person.class);
        System.out.println(beansOfType);

    }
    
    @Test
    public void testImport(){
        String[] beanDefinitionNames = applicationContext.getBeanDefinitionNames();
        for (String name : beanDefinitionNames) {
            System.out.println(name);
        }
        Bule bean = applicationContext.getBean(Bule.class);
        System.out.println(bean);

        RainBow rainBow=applicationContext.getBean(RainBow.class);
        System.out.println(rainBow);

        Object bean1 = applicationContext.getBean("colorFactoryBean");
        System.out.println(bean1);
        Object bean2 = applicationContext.getBean("&colorFactoryBean");
        System.out.println(bean2);
    }
}

