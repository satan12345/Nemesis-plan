package com.able.springbootjdbc.dao.impl;

import com.able.springbootjdbc.bean.User;
import com.able.springbootjdbc.dao.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author jipeng
 * @date 2019-03-06 15:52
 * @description
 */
@Repository
public class UserRepositoryImpl implements UserRepository {

    @Autowired
    JdbcTemplate primaryJdbcTemplate;
    @Override
    public int save(User user) {

        return primaryJdbcTemplate.update("insert into users(id, name, password, age) value (?,?,?,?)",
                user.getId(),user.getName(),user.getPassword(),user.getAge());
    }

    @Override
    public int update(User user) {
        return primaryJdbcTemplate.update("update users set name=?,password=?,age=? where id=?",
                user.getName(),user.getPassword(),user.getAge(),user.getId());
    }

    @Override
    public int delete(long id) {
        return primaryJdbcTemplate.update("delete from users where id=?",id);
    }

    @Override
    public List<User> findALL() {
        return primaryJdbcTemplate.query("select * from users", (resultSet, i) -> {
            User user=new User();
            user.setId(resultSet.getLong("id"));
            user.setAge(resultSet.getInt("age"));
            user.setName(resultSet.getString("name"));
            user.setPassword(resultSet.getString("password"));
            return user;
        });
    }

    @Override
    public User findById(long id) {
        return primaryJdbcTemplate.queryForObject("select * from users where id=?", new Object[]{id}, (resultSet, i) -> {
            User user=new User();
            user.setId(resultSet.getLong("id"));
            user.setAge(resultSet.getInt("age"));
            user.setName(resultSet.getString("name"));
            user.setPassword(resultSet.getString("password"));
            return user;
        });

    }
}

