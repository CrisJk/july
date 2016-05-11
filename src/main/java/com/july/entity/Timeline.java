//test mongodb connnect
package com.july.entity;

import org.springframework.data.annotation.Id;

import java.util.List;


public class Timeline {

    @Id
    private String id;

    private int user_id;
    private List<String> moment_id;

    public Timeline() {}

    public Timeline(int Data) {
        this.user_id = Data;
    }

    @Override
    public String toString() {
        String str="";
        str += user_id;
        return str;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public List<String> getMoment_id() {
        return moment_id;
    }

    public void setMoment_id(List<String> moment_id) {
        this.moment_id = moment_id;
    }
}