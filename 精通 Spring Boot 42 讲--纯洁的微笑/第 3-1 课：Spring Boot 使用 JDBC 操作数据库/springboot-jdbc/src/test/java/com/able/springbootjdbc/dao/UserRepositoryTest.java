package com.able.springbootjdbc.dao;

import com.able.springbootjdbc.bean.User;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.test.context.junit4.SpringRunner;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * @author jipeng
 * @date 2019-03-06 16:15
 * @description
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class UserRepositoryTest {

    @Autowired
    UserRepository userRepository;

    @Autowired
    JdbcTemplate primaryJdbcTemplate;

    @Autowired
    JdbcTemplate secondaryJdbcTemplate;

    @Test
    public void testSave(){
        User user=new User(2L,"宇智波鼬","333333",7);
        int affectCount = userRepository.save(user);
        log.info("保存影响的行数为:{}",affectCount);
    }

    @Test
    public void testFindAll(){
        List<User> all = userRepository.findALL();
        log.info("查询到的列表数据为:{}",all);
    }
    @Test
    public void testQueryById(){
        User user = userRepository.findById(2L);
        log.info("查询到的用户信息为:{}",user);
    }
    @Test
    public void testUpdate(){
        User user = userRepository.findById(2L);
        user.setPassword("23456");
        int affectCount = userRepository.update(user);
        log.info("影响的数据条数为:{}",affectCount);
    }

    @Test
    public void testDelete(){
        int affectCount = userRepository.delete(3L);
        log.info("影响的数据条数为:{}",affectCount);
    }

    @Test
    public void test(){
        String sql="select  count(*) as num from users";
        List<Integer> pCount = primaryJdbcTemplate.query(sql, new RowMapper<Integer>() {
            @Override
            public Integer mapRow(ResultSet rs, int rowNum) throws SQLException {
                return rs.getInt("num");
            }
        });
        log.info("pCount={}",pCount);
        List<Integer> sCount = secondaryJdbcTemplate.query(sql, new RowMapper<Integer>() {
            @Override
            public Integer mapRow(ResultSet rs, int rowNum) throws SQLException {
                return rs.getInt("num");
            }
        });
        log.info("sCount={}",sCount);
    }

}