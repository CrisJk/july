package com.july.service;

import com.july.controller.form.UserCreateForm;
import com.july.entity.User;

import java.security.Principal;

/**
 * Created by kuangjun on 2016/5/7.
 */

public interface UserService {

    User getUserByEmail(String email);

    User create(UserCreateForm form);

    void createVerificationTokenForUser(User user, String token);

    String validateVerificationToken(String token);

<<<<<<< HEAD
    User getOAuthUser(Principal principal, String type);

    //重新登录
    void reloadSessionUser(User user);

    //获得当前登陆用户
    User getSessionUser();

    User getOAuthUserByAccount(String type, String ob);

    void update(User user);

=======
    void addFollower(User current_user,User user);

    void removeFollower(User current_user,User user);

    void addFollowing(User current_user,User user);

    void removeFollowing(User current_user, User user);
>>>>>>> b6aecf57e7d565d3fdb4653e53c72a6f117c15f9
}
