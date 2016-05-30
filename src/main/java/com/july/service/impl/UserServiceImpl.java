package com.july.service.impl;

import com.july.controller.form.UserCreateForm;
import com.july.entity.User;
import com.july.repository.UserRepository;
import com.july.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by kuangjun on 2016/5/7.
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public User getUserByEmail(String email) {
        List<User> users = userRepository.findByEmail(email);
        if(users != null && users.size() == 1){
            User user = users.get(0);
            return user;
        }else{
            return null;
        }
    }

    @Override
    public User create(UserCreateForm form) {
        User user = new User();
        user.setEmail(form.getEmail());
        user.setPassword(new BCryptPasswordEncoder().encode(form.getPassword()));
        user.setNickname(form.getNickname());
        return userRepository.save(user);
    }
}
