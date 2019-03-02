package com.able.springannocation.conditional;

import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotatedTypeMetadata;

/**
 * @author jipeng
 * @date 2019-03-01 15:34
 * @description
 */
public class LinuxCondition implements Condition {
    /**
     *
     * @param context 判断条件上下文环境
     * @param metadata 注解信息
     * @return
     */
    @Override
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
        //获取当前的系统环境
        Environment environment = context.getEnvironment();
        String property = environment.getProperty("os.name");
        System.out.println("LinuxCondition="+property);
        //获取beanRegistry bean定义的注册类
        BeanDefinitionRegistry registry = context.getRegistry();
        //获取BeanFactory
        ConfigurableListableBeanFactory beanFactory = context.getBeanFactory();
        //获取类加载器
        ClassLoader classLoader = context.getClassLoader();
        if (property.contains("lin")) {
            return true;
        }
        return false;
    }
}

