package com.able.springboot.restfulstudy.com.able.controller;

import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.context.WebApplicationContext;

import javax.annotation.Resource;

/**
 * @author jipeng
 * @date 2019-01-30 9:56
 * @description
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class MessageControllerTest {
    @Resource
    WebApplicationContext webApplicationContext;

    MockMvc mockMvc;

    @Before
    public void init() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        batchSave();
    }
    @Test
    public void testDeleteById ()throws Exception{
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.delete("/message/message/1")
                .accept(MediaType.APPLICATION_JSON_UTF8_VALUE)).andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        log.info("返回的状态码为:{}",response.getStatus());
    }
    @Test
    public void testUpdateText() throws Exception{
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                .patch("/message/message/text")
                .param("id", "5")
                .param("text", "更新后的文本")
                .accept(MediaType.APPLICATION_JSON_UTF8_VALUE)).andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        log.info("返回的状态码为:{}",response.getStatus());
    }
    @Test
    public void testModify() throws Exception{
        MultiValueMap<String,String> param=new LinkedMultiValueMap<>();
        param.add("id","10");
        param.add("text","更新后的text");
        param.add("summary","更新后的summary");
        MockHttpServletResponse response = mockMvc.perform(
                MockMvcRequestBuilders.put("/message/message")
                .params(param).accept(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andReturn().getResponse();
        log.info("查询到响应的状态码为:{}",response.getStatus());
    }

    @Test
    public void testQueryById () throws Exception{
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/message/message/1").accept(MediaType.APPLICATION_JSON_UTF8_VALUE)).andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        log.info("响应的状态码为:{}",response.getStatus());
        log.info("查询到的数据为:{}",response.getContentAsString());
    }
    @Test
    public void testList() throws Exception {
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/message/messages").
                accept(MediaType.APPLICATION_JSON_UTF8_VALUE)).andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        log.info("响应状态码为:{}",response.getStatus());
        log.info("返回的内容为:{}",response.getContentAsString());
        
    }

    
    @Test
    public void testCreate() throws Exception{
        MultiValueMap<String,String> multiValueMap=new LinkedMultiValueMap();
        multiValueMap.add("text","木叶");
        multiValueMap.add("summary","火影忍者");
        MvcResult mvcResult = mockMvc.perform(
                MockMvcRequestBuilders.post("/message/message")
                        .params(multiValueMap)
                        .accept(MediaType.APPLICATION_JSON_UTF8_VALUE)).andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        log.info("响应的状态码为:{}",response.getStatus());
        log.info("响应的内容为:{}",response.getContentAsString());
    }


    private void batchSave() throws Exception{
        for (int i = 0; i < 10; i++) {
            MultiValueMap<String,String> multiValueMap=new LinkedMultiValueMap();
            multiValueMap.add("text","木叶"+i);
            multiValueMap.add("summary","火影忍者"+i);
mockMvc.perform(
                    MockMvcRequestBuilders.post("/message/message")
                            .params(multiValueMap)
                            .accept(MediaType.APPLICATION_JSON_UTF8_VALUE));
        }
    }
}

