package com.able.springannocation.ext;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.stereotype.Component;

/**
 * @author jipeng
 * @date 2019-03-08 14:15
 * @description
 */
@Slf4j
@Component
public class MyBeanFactoryPostProcessor implements BeanFactoryPostProcessor {
    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        System.out.println("myBeanFactoryPostProcssor");
        int count = beanFactory.getBeanDefinitionCount();
        log.info("定义的bean的数量为:{}",count);
        String[] beanDefinitionNames = beanFactory.getBeanDefinitionNames();
        for (String beanDefinitionName : beanDefinitionNames) {
            log.info("bean的名字为:{}",beanDefinitionName);
        }
    }
}

