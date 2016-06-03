package com.july.service;

import com.july.controller.form.UserCreateForm;
import com.july.entity.User;

/**
 * Created by kuangjun on 2016/5/7.
 */

public interface UserService {

    User getSessionUser();

    User getUserByEmail(String email);

    User create(UserCreateForm form);

    void createVerificationTokenForUser(User user, String token);

    String validateVerificationToken(String token);

    void addFollower(User current_user,User user);

    void removeFollower(User current_user,User user);

    void addFollowing(User current_user,User user);

    void removeFollowing(User current_user, User user);
}
