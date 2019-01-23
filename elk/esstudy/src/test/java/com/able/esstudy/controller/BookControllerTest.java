package com.able.esstudy.controller;

import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcBuilder;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import javax.annotation.Resource;

/**
 * @author jipeng
 * @date 2019-01-23 14:10
 * @description
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class BookControllerTest {
    @Resource
    BookController bookController;
    MockMvc mockMvc;

    @Before
    public void  init(){
         mockMvc = MockMvcBuilders.standaloneSetup(bookController).build();
    }
    @Test
    public void testQueryById() throws Exception{
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/book/1").accept(MediaType.APPLICATION_JSON_UTF8_VALUE)).andReturn();
        String contentAsString = mvcResult.getResponse().getContentAsString();
        System.out.println(mvcResult);
        log.info("查询到的结果数据为:{}",contentAsString);
    }
    @Test
    public void testSave () throws Exception{
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/book/save")
                .param("title", "火影忍者")
                .param("author", "岸本齐史")
                .param("world_count", "20000")
                .param("publish_date", "2019-01-23").accept(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        log.info("响应的状态码为:{}",response.getStatus());
        String contentAsString = response.getContentAsString();
        log.info("保存的内容id为:{}",contentAsString);
    }
}

