package com.july.service.Impl;

import com.july.entity.User;
import com.july.repository.Mysql.UserDao;
import com.july.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by kuangjun on 2016/5/7.
 */
@Service
@Transactional
public class UserServiceImpl implements UserService {

    @Autowired
    UserDao userDao ;

    @Override
    public User getUserByUsername(String username) {
        return userDao.findByUsername(username);
    }
}
