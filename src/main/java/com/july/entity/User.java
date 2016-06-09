package com.july.entity;

import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.ArrayList;
import java.util.Iterator;
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

    //粉丝 存的是emali
    private List<String> followers = new ArrayList<String>();

    //关注 存的是email
    private List<String> followings = new ArrayList<String>();

    private boolean enabled;

    @DBRef
    private Account facebookAccount;

    @DBRef
    private Account githubAccount;



    //这个属性用在进行昵称搜索时，当前用户与该用户的关注关系判断，其余位置不起作用，可随时更改
    public String is_followed;

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

    public List<Moment> getTimeline() { return timeline; }

    public void setTimeline(List<Moment> timeline) { this.timeline = timeline; }

    public List<String> getFollowers()
    {
        return followers;
    }

    public void setFollowers(List<String> followers) { this.followers = followers; }

    public List<String> getFollowings() {
        return followings;
    }

    public void setFollowings(List<String> followings) { this.followings = followings; }

    public String is_followed() { return is_followed;}

    public void setIs_followed(String is_followed) {this.is_followed = is_followed;}

    @Override
    public String toString() {
        return "User [id=" + id + ", email=" + email + ", nickname=" + nickname + ", is_followed=" + is_followed + "]";
    }
}
