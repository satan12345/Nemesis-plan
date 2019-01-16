package com.able.springboot.springbootweb.com.able.controller;

import com.able.springboot.springbootweb.com.able.model.User;
import org.springframework.boot.context.properties.bind.BindResult;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

/**
 * @author jipeng
 * @date 2019-01-15 9:06
 * @description
 */
@RestController
public class UserController {
    @GetMapping(value = "/getUser")
    public User queryUser(){
        User user=new User("卡卡西",22,"123456");
        return user;
    }
    @RequestMapping("/getUsers")
    public List<User> getUsers() {
        List<User> users=new ArrayList<>();
        User user1=new User();
        user1.setName("neo");
        user1.setAge(30);
        user1.setPass("neo123");
        users.add(user1);
        User user2=new User();
        user2.setName("小明");
        user2.setAge(12);
        user2.setPass("123456");
        users.add(user2);
        return users;
    }
    @PutMapping("/saveUser")
    public void saveUser(@Valid User user, BindingResult result){
        System.out.println(user);
        if (result.hasErrors()) {
            result.getAllErrors().forEach(x-> {
                System.out.println("x.getCode()=="+x.getCode());
                System.out.println("x.getDefaultMessage:"+x.getDefaultMessage());
            });
        }
    }
}

