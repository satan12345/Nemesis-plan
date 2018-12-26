package com.able.gcstudy.chapter4;

import com.able.gcstudy.chapter2.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author jipeng
 * @date 2018-12-13 14:35
 * @description
 */
@RestController
@RequestMapping("ch4")
public class Ch4Controller {


    @RequestMapping("/arg1")
    public String arg1(@RequestParam("name")String name) {
        return "hello,"+name;
    }

    @RequestMapping("/arg2")
    public User arg2(User user) {
        return user;
    }

    @RequestMapping("/constructor")
    public User constructor(User user) {
        return user;
    }

    @RequestMapping("/same1")
    public String same(@RequestParam("name")String name) {
        return "hello,"+name;
    }
    @RequestMapping("/same2")
    public String same(@RequestParam("name")String name,@RequestParam("id")int id) {
        return "hello,"+name+","+id;
    }

    @RequestMapping("/exception")
    public String exception() {
        try {
            System.out.println("start...");
            System.out.println(1/0);
            System.out.println("end...");
        }catch(Exception e) {
            //
        }
        return "success";
    }

}

