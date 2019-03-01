package com.able.springannocation.config;

import com.able.springannocation.bean.Person;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author jipeng
 * @date 2019-03-01 10:47
 * @description
 */
@Configuration
public class IocConfig {
    @Bean
    public Person person(){
        return new Person(1,"卡卡西");
    }
}

