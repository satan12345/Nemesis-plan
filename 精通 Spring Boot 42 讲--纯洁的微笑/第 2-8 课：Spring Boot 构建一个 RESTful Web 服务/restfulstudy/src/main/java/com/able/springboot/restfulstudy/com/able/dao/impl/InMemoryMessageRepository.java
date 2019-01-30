package com.able.springboot.restfulstudy.com.able.dao.impl;

import com.able.springboot.restfulstudy.com.able.bean.Message;
import com.able.springboot.restfulstudy.com.able.dao.MessageRepository;
import com.google.common.collect.Lists;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author jipeng
 * @date 2019-01-30 9:12
 * @description
 */
@Repository("messageRepository")
public class InMemoryMessageRepository implements MessageRepository {
    private static AtomicLong counter = new AtomicLong();
    private final ConcurrentMap<Long, Message> messages = new ConcurrentHashMap<>();
    @Override
    public List<Message> findAll() {
        return Lists.newArrayList(messages.values());
    }

    @Override
    public Message save(Message message) {
        long id = counter.incrementAndGet();
        message.setId(id);
        messages.put(id,message);
        return message;
    }

    @Override
    public Message update(Message message) {
        messages.put(message.getId(),message);
        return message;
    }

    @Override
    public Message updateText(Message message) {
        Message temp = messages.get(message.getId());
        temp.setText(message.getText());
        messages.put(temp.getId(),temp);
        return message;
    }

    @Override
    public Message findMessage(Long id) {
        return messages.get(id);
    }

    @Override
    public void deleteMessage(Long id) {
        messages.remove(id);
    }
}

