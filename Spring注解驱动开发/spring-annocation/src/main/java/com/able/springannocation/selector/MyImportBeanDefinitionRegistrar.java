package com.able.springannocation.selector;

import com.able.springannocation.bean.RainBow;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;

/**
 * @author jipeng
 * @date 2019-03-01 16:20
 * @description
 */
@Slf4j
public class MyImportBeanDefinitionRegistrar implements ImportBeanDefinitionRegistrar {
    /**
     *
     * @param importingClassMetadata 当前类的注解信息
     * @param registry bean定义的注册类
     */
    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {

        RootBeanDefinition beanDefinition=new RootBeanDefinition(RainBow.class);
        MutablePropertyValues propertyValues=new MutablePropertyValues();
        propertyValues.add("number",88);
        beanDefinition.setPropertyValues(propertyValues);
        registry.registerBeanDefinition("rainBow",beanDefinition);
    }
}

