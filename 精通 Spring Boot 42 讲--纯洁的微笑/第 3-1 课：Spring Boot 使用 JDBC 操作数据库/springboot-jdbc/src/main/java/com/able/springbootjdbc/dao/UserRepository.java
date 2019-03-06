package com.able.springbootjdbc.dao;

import com.able.springbootjdbc.bean.User;

import java.util.List;

/**
 * @author jipeng
 * @date 2019-03-06 15:51
 * @description
 */
public interface UserRepository {
    int save(User user);

    int update(User user);

    int delete(long id);

    List<User> findALL();

    User findById(long id);
}
