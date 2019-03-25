package com.able.springboothelloworld.com.able.model;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author jipeng
 * @date 2019-03-14 14:29
 * @description
 */
@Data
@Component
@ConfigurationProperties(prefix = "person")
public class Person {
    List<String> names;
    List<Integer> ages;

    String lastName;
}



