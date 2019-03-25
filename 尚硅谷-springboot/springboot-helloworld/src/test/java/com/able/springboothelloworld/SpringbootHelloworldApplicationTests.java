package com.able.springboothelloworld;

import com.able.springboothelloworld.com.able.model.Person;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class SpringbootHelloworldApplicationTests {

    @Autowired
    Person person;
    @Test
    public void contextLoads() {
        BigDecimal bigDecimal=new BigDecimal(Integer.valueOf(0));
        System.out.println(bigDecimal.equals(BigDecimal.ZERO));

    }
    public static void main(String[] args){

    }

}




