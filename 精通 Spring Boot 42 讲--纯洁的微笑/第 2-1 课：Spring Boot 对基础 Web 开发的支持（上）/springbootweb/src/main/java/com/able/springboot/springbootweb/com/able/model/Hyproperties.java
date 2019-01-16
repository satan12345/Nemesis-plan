package com.able.springboot.springbootweb.com.able.model;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author jipeng
 * @date 2019-01-15 14:25
 * @description
 */
@Component
@ConfigurationProperties(prefix = "ying")
@Data
public class Hyproperties {

    private String title;
    private String description;
}

