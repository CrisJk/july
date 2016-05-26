package com.july.service;

import com.july.beans.SecUserDetails;
import com.july.entity.User;
import com.july.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by sherrypan on 16-5-25.
 */
@Service
public class SecUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        List<User> users = userRepository.findByEmailAddress(username);
        if(users == null || users.size() > 1){
            throw new UsernameNotFoundException(username);
        }else{
            User user = users.get(0);
            UserDetails details = new SecUserDetails(user);
            return details;
        }
    }

}
