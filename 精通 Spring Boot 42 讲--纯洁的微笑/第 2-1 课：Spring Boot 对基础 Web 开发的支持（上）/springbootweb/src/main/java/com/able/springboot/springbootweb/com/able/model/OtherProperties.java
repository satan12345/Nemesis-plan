package com.able.springboot.springbootweb.com.able.model;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * @author jipeng
 * @date 2019-01-15 14:31
 * @description
 */
@Component
@ConfigurationProperties(prefix="other")
@PropertySource("classpath:other.properties")
@Data
public class OtherProperties {
    private String title;
    private String blog;
}

