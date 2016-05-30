package com.july.service;

import com.july.controller.form.UserCreateForm;
import com.july.entity.User;

/**
 * Created by kuangjun on 2016/5/7.
 */

public interface UserService {

    User getUserByEmail(String email);

    User create(UserCreateForm form);

    void createVerificationTokenForUser(User user, String token);

    String validateVerificationToken(String token);

}
