package com.july.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * Created by kuangjun on 2016/5/7.
 */
@Entity
@Table(name="role")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id ;

    @Column(name="role")
    @NotNull
    private String role ;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
