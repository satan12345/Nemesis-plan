package com.able.springannocation.service;

import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

/**
 * @author jipeng
 * @date 2019-03-01 11:23
 * @description
 */
@Service
public class BookService {
    @EventListener(classes = {ApplicationEvent.class})
    public void listener(ApplicationEvent applicationEvent){

    }
}

