package com.july.controller.form;

import com.july.entity.Account;
import com.july.entity.User;

import java.util.List;

/**
 * Created by sherrypan on 16-5-29.
 */
public class UserCreateForm {

    private String email = "";

    private String password = "";

    private String passwordRepeated = "";

    private String nickname = "";

    private Account githubAccount = null;

    private Account facebookAcount = null;

    private List<String> followers = null;

    private List<String> followings = null;

    public List<String> getFollowers(){return followers;}

    public void setFollowers(List<String> followers){this.followers=followers;}

    public List<String> getFollowings(){return followings;}

    public void setFollowings(List<String> followings){this.followings=followings;}

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getPasswordRepeated() {
        return passwordRepeated;
    }

    public void setPasswordRepeated(String passwordRepeated) {
        this.passwordRepeated = passwordRepeated;
    }

    public Account getGithubAccount() {
        return githubAccount;
    }

    public void setGithubAccount(Account githubAccount) {
        this.githubAccount = githubAccount;
    }

    public Account getFacebookAcount() {
        return facebookAcount;
    }

    public void setFacebookAcount(Account facebookAcount) {
        this.facebookAcount = facebookAcount;
    }

}
