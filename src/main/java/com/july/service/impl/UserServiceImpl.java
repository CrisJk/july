package com.july.service.Impl;

import com.july.controller.form.UserCreateForm;
import com.july.entity.Account;
import com.july.entity.User;
import com.july.entity.VerificationToken;
import com.july.repository.UserRepository;
import com.july.repository.VerificationTokenRepository;
import com.july.service.AccountService;
import com.july.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.security.Principal;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

/**
 * Created by kuangjun on 2016/5/7.
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private AccountService accountService;

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

    @Override
<<<<<<< HEAD
    public User getOAuthUser(Principal principal, String type) {
        UserDetails userDetails = (UserDetails) ((OAuth2Authentication) principal).getUserAuthentication().getDetails();
        User user;
        Map mp = (Map) ((OAuth2Authentication) principal).getDetails();
        if (type == "github") {
            user = this.getUserByGithubAccount((String)mp.get("login"));
        } else {
            user = this.getUserByFacebookAccount((String)mp.get("id"));
        }
        return user;
    }

    private User getUserByFacebookAccount(String fid) {
        Account account = accountService.getByTypeAndIdentity("facebook", fid);
        if (account == null) {
            return null;
        } else {
            List<User> users = userRepository.findByFacebookAccount(account);
            System.out.println(account.getIdentity());
            if (users != null && users.size() == 1 ) {
                return users.get(0);
            } else {
                return null;
            }
        }
    }

    private User getUserByGithubAccount(String gid) {
        Account account = accountService.getByTypeAndIdentity("github", gid);
        if (account == null) {
            return null;
        } else {
            List<User> users = userRepository.findByGithubAccount(account);
            if (users != null && users.size() == 1 ) {
                return users.get(0);
            } else {
                return null;
            }
        }
    }

    public void reloadSessionUser(User user) {
        Authentication authentication = new UsernamePasswordAuthenticationToken(user, user.getPassword(), null);
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    @Override
    public User getSessionUser() {
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    @Override
    public User getOAuthUserByAccount(String type, String ob) {
        if (type == "github") {
            return getUserByGithubAccount(ob);
        } else {
            return getUserByFacebookAccount(ob);
        }
    }

    @Override
    public void update(User user) {
        userRepository.save(user);
    }

<<<<<<< HEAD
=======
    public void addFollower(User current_user, User user) {
        current_user.addFollower(user);
        userRepository.save(current_user);
    }

    @Override
    public void removeFollower(User current_user, User user) {
        current_user.removeFollower(user);
        userRepository.save(current_user);
    }

    @Override
    public void addFollowing(User current_user, User user) {
        current_user.addFollowing(user);
        userRepository.save(current_user);
    }

    @Override
    public void removeFollowing(User current_user, User user) {
        current_user.removeFollowing(user);
        userRepository.save(current_user);
    }
>>>>>>> b6aecf57e7d565d3fdb4653e53c72a6f117c15f9
=======
    @Override
    public User getUserById(BigInteger id) {
        return userRepository.findOne(id);
    }

>>>>>>> 42697a0b763260ad9f8f68b2a36a9b2c72b3eeed
}
