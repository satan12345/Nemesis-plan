package com.able.springannocation.bean;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author jipeng
 * @date 2019-03-02 16:28
 * @description
 */
@Component
public class Boss {


    Car car;

    @Autowired
    public Boss(@Autowired Car car) {
        this.car = car;
    }


    public Car getCar() {
        return car;
    }
    @Autowired
    public void setCar(Car car) {
        this.car = car;
    }

    @Override
    public String toString() {
        return "Boss{" +
                "car=" + car +
                '}';
    }
}

