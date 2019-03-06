package com.able.springannocation.config;

import com.able.springannocation.aop.MathCalculator;
import com.able.springannocation.bean.Boss;
import com.able.springannocation.bean.Car;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * @author jipeng
 * @date 2019-03-01 16:55
 * @description
 */
public class AOPConfigTest {
    AnnotationConfigApplicationContext applicationContext;
    @Before
    public void  init(){
        applicationContext=new AnnotationConfigApplicationContext(AOPConfig.class);


    }
    @Test
    public void test1(){
        MathCalculator ma = applicationContext.getBean(MathCalculator.class);
        ma.div(6,3);

    }


}