package com.able.springboot.thymeleaf.controller;

import com.able.springboot.thymeleaf.model.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author jipeng
 * @date 2019-01-16 13:09
 * @description
 */
@Controller
public class HelloController {
    @GetMapping("world")
    public ModelAndView toWorld(HttpServletRequest request, HttpSession httpSession){

        ModelAndView modelAndView=new ModelAndView("page/world");
        modelAndView.addObject("userName","旗木卡卡西");
        request.setAttribute("request","i am request");
        httpSession.setAttribute("session","i am session");
        modelAndView.addObject("date",new Date());
        modelAndView.addObject("users",getUserList());
        modelAndView.addObject("count","6");
        return modelAndView;
    }

    @GetMapping("/hello")
    public ModelAndView toHello(){
        ModelAndView modelAndView=new ModelAndView("page/hello");
        modelAndView.addObject("message","你好,宇智波鼬");
        modelAndView.addObject("userName","真实名称111");
        modelAndView.addObject("flag",true);
        modelAndView.addObject("users",getUserList());
        modelAndView.addObject("type",1);
        modelAndView.addObject("pageId",2);
        modelAndView.addObject("orderId",2);
        modelAndView.addObject("img","https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1547630111506&di=c774eb415df5cab1459c20cd28899c91&imgtype=0&src=http%3A%2F%2Fb-ssl.duitang.com%2Fuploads%2Fitem%2F201610%2F19%2F20161019190541_UA3Gf.jpeg");
        modelAndView.addObject("name","卡卡西");
        modelAndView.addObject("age",23);
        modelAndView.addObject("sex","woman");
        return modelAndView;
    }

    private List<User> getUserList(){
        List<User> list=new ArrayList<>();
        User user1=new User("大牛",12,"123456");
        User user2=new User("小牛",6,"123563");
        User user3=new User("纯洁的微笑",66,"666666");
        list.add(user1);
        list.add(user2);
        list.add(user3);
        return  list;
    }
}

