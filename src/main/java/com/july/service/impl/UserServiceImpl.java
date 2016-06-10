package com.july.service.Impl;

import com.july.controller.form.UserCreateForm;
import com.july.entity.Account;
import com.july.entity.Moment;
import com.july.entity.User;
import com.july.entity.VerificationToken;
import com.july.repository.UserRepository;
import com.july.repository.VerificationTokenRepository;
import com.july.service.AccountService;
import com.july.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.stereotype.Service;
import java.math.BigInteger;
import java.security.Principal;
import java.util.*;

/**
 * Created by kuangjun on 2016/5/7.
 */

class ComparatorMoment implements Comparator
{
    public int compare(Object obj0,Object obj1)
    {
        Moment moment0 = (Moment)obj0;
        Moment moment1 = (Moment)obj1;
        return moment1.getId().compareTo(moment0.getId());
    }
}
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    MongoOperations mongoOperations;

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
        //System.out.println("UserServiceImpl: ENTER");
        List<User> users = userRepository.findByEmail(email);
        //System.out.println("UserServiceImpl: "+users.size());
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
        List<Moment> timeline = user.getTimeline();
        ComparatorMoment comparator=new ComparatorMoment();
        Collections.sort(timeline, comparator);
        user.setTimeline(timeline);
        userRepository.save(user);
    }

    @Override
    public List<User> getUserByNickName(String nickName) {
        List<User> users = userRepository.findByNickname(nickName);
        if(users != null && users.size()!=0){
            return users;
        }else{
            return null;
        }
    }

    @Override
    public User getUserById(BigInteger id) {
        return userRepository.findOne(id);
    }

    //以分页方式，按昵称搜索
    @Override
    public Page<User> getUserByNickNameInPage(String nickname, Pageable pageable)
    {
        Page<User> users = userRepository.findByNickname( nickname, pageable);
        if(users!=null&&users.getTotalElements()!=0) return users;
        else return null;
    }

}
