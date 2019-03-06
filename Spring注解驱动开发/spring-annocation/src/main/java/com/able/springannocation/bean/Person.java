package com.able.springannocation.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;

/**
 * @author jipeng
 * @date 2019-03-01 10:46
 * @description
 */
@Data
public class Person {
    /**
     * 使用@Value 赋值
     * 1 基本数值
     * 2 可以使用Spel:#{}
     * 3 可以使用${},使用配置文件中的值
     *
     */
    @Value("#{20-2}")
    private Integer id;

    /**
    *
    */
    @Value("张三")
    private String name;
    @Value("${nike.name}")
    private String nikeName;

    public Person() {

    }

    public Person(Integer id, String name) {
        this.id = id;
        this.name = name;
        //System.out.println("person创建");
    }
}

