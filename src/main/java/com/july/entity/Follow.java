package com.july.entity;

import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;

import java.util.Set;

/**
 * Created by sherrypan on 16-5-25.
 */
public class Follow extends AbstractDocument {
    @DBRef
    @Indexed
    private User user;

    @DBRef
    private Set<User> followers;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Set<User> getFollowers() {
        return followers;
    }

    public void setFollowers(Set<User> followers) {
        this.followers = followers;
    }

}
