package com.able.springannocation.config;

import com.able.springannocation.bean.Person;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.annotation.PropertySource;

/**
 * @author jipeng
 * @date 2019-03-02 14:58
 * @description
 */
//@Configuration
//@PropertySource({
//        "classpath:application.properties"
//})
public class PropertyConfig {
    @Bean
    public Person person(){

        return new Person();

    }
}

