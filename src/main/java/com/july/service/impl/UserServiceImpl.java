package com.july.service.impl;

import com.july.controller.form.UserCreateForm;
import com.july.entity.User;
import com.july.entity.VerificationToken;
import com.july.repository.UserRepository;
import com.july.repository.VerificationTokenRepository;
import com.july.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.List;

/**
 * Created by kuangjun on 2016/5/7.
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private VerificationTokenRepository tokenRepository;

    public static final String TOKEN_INVALID = "invalidToken";
    public static final String TOKEN_EXPIRED = "expired";

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

    @Override
    public void createVerificationTokenForUser(User user, String token) {
        VerificationToken myToken = new VerificationToken(token, user);
        tokenRepository.save(myToken);
    }

    @Override
    public String validateVerificationToken(String token) {
        final VerificationToken verificationToken = tokenRepository.findByToken(token);
        if (verificationToken == null) {
            return TOKEN_INVALID;
        }

        final User user = verificationToken.getUser();
        final Calendar cal = Calendar.getInstance();
        if ((verificationToken.getExpiryDate().getTime() - cal.getTime().getTime()) <= 0) {
            return TOKEN_EXPIRED;
        }

        user.setEnabled(true);
        tokenRepository.delete(verificationToken);
        userRepository.save(user);
        return null;
    }

}
