package com.july.entity;

import javax.persistence.*;
import javax.persistence.Id;

/**
 * Created by kuangjun on 2016/5/5.
 */
@Entity
@Table(name="users")
public class Users {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Integer id ;

    @Column(name="username" ,unique = true)
    String username ;

    @Column(name="password")
    String password ;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
