package com.able.springboot.helloworld.com.able.test;

import com.able.springboot.helloworld.com.able.controller.HelloController;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

/**
 * @author jipeng
 * @date 2019-01-14 16:52
 * @description
 */
@SpringBootTest
public class HelloTest {
    MockMvc mockMvc;
    @Before
    public void init(){
        mockMvc= MockMvcBuilders.standaloneSetup(new HelloController()).build();
    }
    @Test
    public void testHello() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.get("/hello?name=小明&age=123")
                .accept(MediaType.APPLICATION_JSON_UTF8)
        ).andDo(MockMvcResultHandlers.print());

    }
    @Test
    public void testHello1 () throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.post("/hello?name=卡卡西&age=17")
                .accept(MediaType.APPLICATION_JSON_UTF8))/*.andDo(print())*/
                .andExpect(MockMvcResultMatchers.content().string(Matchers.containsString("鸣人")));
    }
    @Test
    public void hello(){
        System.out.println("hello world");
    }
}

