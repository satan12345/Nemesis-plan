package com.able.springannocation.ext;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.BeanPostProcessor;

import javax.servlet.ServletContainerInitializer;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.HandlesTypes;
import java.util.Set;

/**
 * @author jipeng
 * @date 2019-03-16 16:45
 * @description
 */
//容器启动的时候会将@HandlesTypes指定的这个类型下面的子类 子接口传递过来
@HandlesTypes(BeanPostProcessor.class)
@Slf4j
public class MyServletContainerInntializer implements ServletContainerInitializer {
    /**
     *
     * @param set 感兴趣类型的所有子类型
     * @param servletContext 代表当前web应用的ServletContext 一个web应用 一个servletContext
     *                       1 使用servletContext注册web组件
     * @throws ServletException
     */
    @Override
    public void onStartup(Set<Class<?>> set, ServletContext servletContext) throws ServletException {
        log.info("感兴趣的类型为:{}",set);
//        servletContext.addFilter();
//        servletContext.addServlet().addMapping();
//        servletContext.addFilter().addMappingForUrlPatterns();
    }
}

