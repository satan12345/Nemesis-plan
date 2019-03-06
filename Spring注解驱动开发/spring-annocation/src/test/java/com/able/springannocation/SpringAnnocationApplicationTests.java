package com.able.springannocation;

import com.able.springannocation.bean.Person;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.stream.Stream;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class SpringAnnocationApplicationTests {
    @Resource
    ApplicationContext applicationContext;

    @Test
    public void contextLoads() {
        Person bean = applicationContext.getBean(Person.class);
        log.info("bean={}",bean);
        String[] beanNamesForType = applicationContext.getBeanNamesForType(Person.class);
        Stream.of(beanNamesForType).forEach(System.out::println);

        String[] beanDefinitionNames = applicationContext.getBeanDefinitionNames();
        for (String beanDefinitionName : beanDefinitionNames) {
            log.info("name= {}",beanDefinitionName);
        }
    }
    @Test
    public void testScope(){
        Person person1 = applicationContext.getBean(Person.class);
        Person person2 = applicationContext.getBean(Person.class);
        System.out.println(person1==person2);
    }
    @Test
    public void testLasy_Loading(){
//        String[] beanDefinitionNames = applicationContext.getBeanDefinitionNames();
//        for (String name : beanDefinitionNames) {
//            System.out.println(name);
//        }
        System.out.println("======开始获取bean");
        Person bean = applicationContext.getBean(Person.class);
        Person bean1 = applicationContext.getBean(Person.class);
        System.out.println("bean1==bean2"+(bean==bean1));

    }


}
