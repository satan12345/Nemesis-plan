package com.able.springboot.restfulstudy.com.able.controller;

import com.able.springboot.restfulstudy.com.able.bean.Message;
import com.able.springboot.restfulstudy.com.able.dao.MessageRepository;
import io.swagger.annotations.*;
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
@Api(value = "消息" ,description = "消息操作的API",produces = "application/json",protocols="http")
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

    @ApiOperation(
            value = "消息列表",
            notes = "完整的消息内容列表",
            produces="application/json, application/xml",
            consumes="application/json, application/xml",
            response = ResponseEntity.class
    )
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

    @ApiOperation(value = "添加消息",produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id",value = "消息id",paramType ="query" ),
            @ApiImplicitParam(name = "text",value = "消息正文",required = true,paramType ="query" ),
            @ApiImplicitParam(name = "summary",value = "摘要",required = true,paramType ="query" )
    }
    )
    @ApiResponses({
            @ApiResponse(code = 201,message = "创建成功"),
            @ApiResponse(code = 500,message = "服务器内部错误")
    })
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

