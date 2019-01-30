package com.able.springboot.restfulstudy.com.able.dao;

import com.able.springboot.restfulstudy.com.able.bean.Message;

import java.util.List;

/**
 * @author jipeng
 * @date 2019-01-30 9:11
 * @description
 */
public interface MessageRepository {
    List<Message> findAll();

    Message save(Message message);

    Message update(Message message);

    Message updateText(Message message);

    Message findMessage(Long id);

    void deleteMessage(Long id);
}
