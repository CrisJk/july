package com.july.entity;

import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

/**
 * Created by kuangjun on 2016/5/5.
 */
@Entity
@Table(name="users")
public class Users {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id ;

    @Column(name="username" ,unique = true)
    @NotEmpty
    private String username ;

    @Column(name="password")
    @NotNull
    private String password ;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "role_id" , referencedColumnName = "id", nullable = false)
    private Role role ;

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
