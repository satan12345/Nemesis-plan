package com.able.springboot.springbootweb.com.able.conf;

import com.able.springboot.springbootweb.com.able.web.filter.MyFilter;
import com.able.springboot.springbootweb.com.able.web.filter.MyFilter2;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author jipeng
 * @date 2019-01-15 11:20
 * @description
 */
@Configuration
public class WebConfiguration {
    @Bean
    public FilterRegistrationBean myFilter(){
        FilterRegistrationBean filterRegistrationBean=new FilterRegistrationBean();
        filterRegistrationBean.setFilter(new MyFilter());
        filterRegistrationBean.setOrder(6);
        filterRegistrationBean.setName("myfilter");
        filterRegistrationBean.addUrlPatterns("/*");
        return filterRegistrationBean;
    }
    @Bean
    public FilterRegistrationBean myFilter2(){
        FilterRegistrationBean filterRegistrationBean=new FilterRegistrationBean();
        filterRegistrationBean.setFilter(new MyFilter2());
        filterRegistrationBean.setOrder(1);
        filterRegistrationBean.setName("myfilter2");
        filterRegistrationBean.addUrlPatterns("/*");
        return filterRegistrationBean;
    }
}

