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

    //粉丝
    @DBRef
    private List<User> followers = new ArrayList<User>();

    //关注
    @DBRef
    private List<User> followings = new ArrayList<User>();

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

    public List<Moment> getTimeline() { return timeline; }

    public void setTimeline(List<Moment> timeline) { this.timeline = timeline; }

    public List<User> getFollowers() { return followers; }

    public void setFollowers(List<User> followers) { this.followers = followers; }

    public List<User> getFollowings() { return followings; }

    public void setFollowings(List<User> followings) { this.followings = followings; }

    public void addFollower( User user )
    {
        followers.add(user);
    }

    public void addFollowing( User user )
    {
        followings.add(user);
        List<Moment> tmpTimeLine = user.getTimeline();
        for( int i = 0; i < tmpTimeLine.size(); i ++ )
        {
            timeline.add(tmpTimeLine.get(i));
        }
    }

    public void removeFollower( User user )
    {
        followers.remove(user);
    }

    public void removeFollowing( User user )
    {
        followings.remove(user);
        List<Moment> tmpTimeLine = user.getTimeline();
        for( int i = 0; i < tmpTimeLine.size(); i ++ )
        {
            timeline.remove(tmpTimeLine.get(i));
        }
    }
    @Override
    public String toString() {
        return "User [id=" + id + ", email=" + email + ", nickname=" + nickname + "]";
    }


}
