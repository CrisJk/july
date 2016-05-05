package com.july.repository.Mysql;

import com.july.entity.Users;
import org.apache.catalina.User;
import org.springframework.data.repository.CrudRepository;

import javax.transaction.Transactional;

/**
 * Created by kuangjun on 2016/5/5.
 */
@Transactional
public interface  UserDao extends CrudRepository<Users,Long> {
    public User findByUsername(String username) ;
}
