package com.able.springannocation;

import com.able.springannocation.bean.Person;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

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
    }

}
