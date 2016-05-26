package com.july.entity;

import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kuangjun on 2016/5/5.
 */
@Document
public class User extends AbstractDocument {

    @Field("email")
    @Indexed(unique = true)
    private EmailAddress emailAddress;

    private String password;

    private String nickname;

    //头像地址
    private String avatarAddress;

    @DBRef
    private List<Moment> moments = new ArrayList<Moment>();

    public EmailAddress getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(EmailAddress emailAddress) {
        this.emailAddress = emailAddress;
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

    @Override
    public String toString() {
        return "User [id=" + id + ", email=" + emailAddress + ", nickname=" + nickname + "]";
    }

}
