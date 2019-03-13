package com.able.springannocation.tx;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Random;
import java.util.UUID;

/**
 * @author jipeng
 * @date 2019-03-08 10:17
 * @description
 */
@Repository
@Slf4j
public class UserDao {
    @Autowired
    JdbcTemplate jdbcTemplate;
    @Transactional(rollbackFor = Exception.class)
    public void save(){

        String substring = UUID.randomUUID().toString().substring(0, 5);
        int age = new Random().nextInt(100);
        int affectCount = jdbcTemplate.update("insert into  users(name, password, age)value (?,?,?)",
                substring, substring, age);
        log.info("插入完成:affectCount= {}",affectCount);
        throw  new RuntimeException("抛出异常玩玩");


    }
}

