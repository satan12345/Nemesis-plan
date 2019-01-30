package com.able.springboot.restfulstudy.com.able.controller;

import com.able.springboot.restfulstudy.com.able.bean.Message;
import com.able.springboot.restfulstudy.com.able.dao.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author jipeng
 * @date 2019-01-30 9:23
 * @description
 */
@RestController
@RequestMapping("message")
public class MessageController {
    @Autowired
    private MessageRepository messageRepository;
    @DeleteMapping("message/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Long id ){
        try {
            messageRepository.deleteMessage(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }


    @PatchMapping("message/text")
    public ResponseEntity<Message> patch(Long id,String text){
        try {
            Message message=new Message();
            message.setId(id);
            message.setText(text);
            messageRepository.updateText(message);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
    @PutMapping("message")
    public ResponseEntity<Message> modify(Message message) {
        try {
            messageRepository.update(message);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    @GetMapping("/message/{id}")
    public ResponseEntity<Message> getById(@PathVariable("id") Long id) {
        try {
            Message message = messageRepository.findMessage(id);
            if (message == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
            return ResponseEntity.ok(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    @GetMapping("/messages")
    public ResponseEntity<List<Message>> list() {

        try {
            List<Message> all = messageRepository.findAll();
            if (CollectionUtils.isEmpty(all)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
            return ResponseEntity.ok(all);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    @PostMapping("/message")
    public ResponseEntity<Message> save(Message message) {
        try {
            Message temp = messageRepository.save(message);
            return ResponseEntity.status(HttpStatus.CREATED).body(temp);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
}

