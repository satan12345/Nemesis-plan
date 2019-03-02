package com.able.springannocation.bean;

import org.springframework.beans.factory.FactoryBean;

/**
 * @author jipeng
 * @date 2019-03-01 16:39
 * @description
 */
public class ColorFactoryBean implements FactoryBean<White> {
    @Override
    public White getObject() throws Exception {
        return new White();
    }

    @Override
    public Class<?> getObjectType() {
        return White.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }
}

