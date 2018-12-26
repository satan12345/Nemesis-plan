package com.able.gcstudy.chapter5;

import com.able.gcstudy.chapter2.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author jipeng
 * @date 2018-12-14 9:47
 * @description
 */
@RestController
@RequestMapping("ch5")
public class Ch5Controller {
    @GetMapping("user")
    public User query(){
        User user=new User();
        user.setId("1");
        user.setName("宇智波鼬");
        return user;
    }
}

