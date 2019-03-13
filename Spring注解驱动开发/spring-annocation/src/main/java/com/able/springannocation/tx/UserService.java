package com.able.springannocation.tx;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author jipeng
 * @date 2019-03-08 10:52
 * @description
 */
@Service
public class UserService {
    @Autowired
    UserDao userDao;
    public void  save(){
        userDao.save();

    }
}

