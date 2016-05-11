package com.july.entity;

/**
 * Created by Crow on 2016/5/11.
 */
import org.springframework.data.annotation.Id;

import java.util.List;

public class Follow {
    @Id
    private int id;

    private int user_id;

    private List<String> follower_id;

    private List<String> following_id;
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public List<String> getFollower_id() {
        return follower_id;
    }

    public void setFollower_id(List<String> follower_id) {
        this.follower_id = follower_id;
    }

    public List<String> getFollowing_id() {
        return following_id;
    }

    public void setFollowing_id(List<String> following_id) {
        this.following_id = following_id;
    }
}


