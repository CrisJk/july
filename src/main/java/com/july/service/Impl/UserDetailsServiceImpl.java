package com.july.service.Impl;

import com.july.beans.CurrentUser;
import com.july.entity.User;
import com.july.service.UserService;
import org.cyberneko.html.HTMLScanner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by kuangjun on 2016/5/7.
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    UserService userService ;

    @Override
    public UserDetails loadUserByUsername(String username)throws UsernameNotFoundException{
        User user = userService.getUserByUsername(username) ;
        if(user == null){
            throw new UsernameNotFoundException(String.format("User with name=%s was not found", username));
        }
        return  new CurrentUser(user) ;
    }


}
