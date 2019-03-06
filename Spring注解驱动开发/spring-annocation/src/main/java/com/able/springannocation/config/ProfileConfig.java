package com.able.springannocation.config;

import com.able.springannocation.bean.Bule;
import com.able.springannocation.bean.Car;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

/**
 * @author jipeng
 * @date 2019-03-05 11:10
 * @description
 */
@Configuration
public class ProfileConfig {

    @Bean
    @Profile("default")
    public Car car(){
        return new Car();
    }

    @Bean
    @Profile("dev")
    public Bule bule(){
        return new Bule();
    }


}

