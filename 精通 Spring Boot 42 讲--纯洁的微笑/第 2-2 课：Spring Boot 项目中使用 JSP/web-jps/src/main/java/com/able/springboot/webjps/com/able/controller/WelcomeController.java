package com.able.springboot.webjps.com.able.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Date;

/**
 * @author jipeng
 * @date 2019-01-15 15:02
 * @description
 */
@Controller
public class WelcomeController {
    @GetMapping("/welcome")
    public String welcome(Model model){
        model.addAttribute("time", new Date());
        model.addAttribute("message", "hello world");
        return "welcome";
    }
}

