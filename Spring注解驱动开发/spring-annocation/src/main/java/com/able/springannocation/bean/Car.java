package com.able.springannocation.bean;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

/**
 * @author jipeng
 * @date 2019-03-01 16:53
 * @description
 */
@Component
public class Car implements InitializingBean, DisposableBean {

    public Car() {
        System.out.println("car================constructor");
    }
    //对象创建并赋值之后调用
    @PostConstruct
    public void postConstrctor(){
        System.out.println("car========postConstructor========");
    }

    @PreDestroy
    public void preDestory(){
        System.out.println("car======preDestory========");
    }
    public void  init(){
        System.out.println("car===================init=======");
    }
    public void destory(){
        System.out.println("car====================destory");
    }

    @Override
    public void destroy() throws Exception {
        System.out.println("car====================destroy");
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.println("car=================afterPropertiesSet");
    }


}

