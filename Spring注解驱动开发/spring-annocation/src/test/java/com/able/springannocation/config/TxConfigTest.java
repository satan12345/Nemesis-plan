package com.able.springannocation.config;

import com.able.springannocation.tx.UserDao;
import com.able.springannocation.tx.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

/**
 * @author jipeng
 * @date 2019-03-08 10:25
 * @description
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class TxConfigTest {

    @Resource
    UserService userService;
    @Test
    public void testSave(){
        userService.save();
    }


}