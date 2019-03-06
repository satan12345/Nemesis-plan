package com.able.springannocation.aop;

import lombok.extern.slf4j.Slf4j;

/**
 * @author jipeng
 * @date 2019-03-05 13:09
 * @description
 */
@Slf4j
public class MathCalculator {

    public int div(int a,int b){
        log.info("a/b");
        return  a/b;
    }
}

