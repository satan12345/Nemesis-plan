package com.able.springannocation.ext;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

/**
 * @author jipeng
 * @date 2019-03-08 16:04
 * @description
 */
@Slf4j
@Component
public class MyApplicationListener implements ApplicationListener<ApplicationEvent> {
    @Override
    public void onApplicationEvent(ApplicationEvent event) {
        //当容器中发布此时间  方法触发
        log.info("收到事件:{}",event);
    }
}

