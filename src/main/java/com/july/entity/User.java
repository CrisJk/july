package com.july.entity;

import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by kuangjun on 2016/5/5.
 */
@Document
public class User extends AbstractDocument {

    @Indexed
    private String email;

    private String password;

    private String nickname;

    //头像地址
    private String avatarAddress;

    @DBRef
    private List<Moment> timeline = new ArrayList<Moment>();

    //粉丝
    @DBRef
    private List<User> followers = new ArrayList<User>();

    //关注
    @DBRef
    private List<User> folllowings = new ArrayList<User>();

    private boolean enabled;

    @DBRef
    private Account facebookAccount;

    @DBRef
    private Account githubAccount;

    public User() {
        super();
        this.enabled = false;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(final boolean enabled) {
        this.enabled = enabled;
    }

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

    public String getAvatarAddress() {
        return avatarAddress;
    }

    public void setAvatarAddress(String avatarAddress) {
        this.avatarAddress = avatarAddress;
    }

    public Account getFacebookAccount() {
        return facebookAccount;
    }

    public void setFacebookAccount(Account facebookAccount) {
        this.facebookAccount = facebookAccount;
    }

    public Account getGithubAccount() {
        return githubAccount;
    }

    public void setGithubAccount(Account githubAccount) {
        this.githubAccount = githubAccount;
    }

    public List<Moment> getTimeline() {
        return timeline;
    }

    public void setTimeline(List<Moment> timeline) {
        this.timeline = timeline;
    }

    @Override
    public String toString() {
        return "User [id=" + id + ", email=" + email + ", nickname=" + nickname + "]";
    }

}
