package com.july.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * Created by Crow on 2016/5/11.
 */
@Entity
@Table(name="moment")
public class Moment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id ;

    @Column(name="path1")
    private String path1;

    @Column(name="path2")
    private String path2;

    @Column(name="path3")
    private String path3;

    @Column(name="url")
    private String url;

    @Column(name="article")
    private String article;

    @NotNull
    @ManyToOne
    @JoinColumn(name="user_id",referencedColumnName = "id",nullable=false)
    private User user;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPath1() {
        return path1;
    }

    public void setPath1(String path1) {
        this.path1 = path1;
    }

    public String getPath2() {
        return path2;
    }

    public void setPath2(String path2) {
        this.path2 = path2;
    }

    public String getPath3() {
        return path3;
    }

    public void setPath3(String path3) {
        this.path3 = path3;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getArticle() {
        return article;
    }

    public void setArticle(String article) {
        this.article = article;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
