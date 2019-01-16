package com.able.springboot.springbootweb.com.able.controller;

import com.able.springboot.springbootweb.com.able.model.Hyproperties;
import com.able.springboot.springbootweb.com.able.model.OtherProperties;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import javax.annotation.Resource;

/**
 * @author jipeng
 * @date 2019-01-15 9:33
 * @description
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class UserControllerTest {

    @Resource
    UserController userController;

    MockMvc mockMvc;

    @Before
    public void init() {
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
    }

    @Test
    public void testQueryUser() throws Exception {
        String contentAsString = mockMvc.perform(MockMvcRequestBuilders.post("/getUser"))
                .andReturn().getResponse().getContentAsString();
        log.info("查询到的用户信息为:{}",contentAsString);
    }
    @Test
    public void testQueryUsers() throws Exception{
        String getUsers = mockMvc.perform(MockMvcRequestBuilders.post("/getUsers")
                .accept(MediaType.APPLICATION_JSON_UTF8))
                .andReturn().getResponse().getContentAsString();
        log.info("查询到的用户信息为:{}",getUsers);
    }
    @Test
    public void testSaveUser () throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.put("/saveUser")
                .param("name","")
                .param("age","666")
                .param("pass","test")
        );
    }

    @Value("${huoying.name}")
    private String name;
    
    @Test
    public void testProperty (){
        log.info("幻术最强者:{}",name);
    }
    @Resource
    Hyproperties hyproperties;
    @Test
    public void testHuoyingProperties (){
        System.out.println(hyproperties.toString());
    }

    @Resource
    OtherProperties otherProperties;
    @Test
    public void testOtherProperties (){
        System.out.println(otherProperties);
    }

}

