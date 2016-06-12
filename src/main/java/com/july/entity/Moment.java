package com.july.entity;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.mongodb.core.index.TextIndexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.TextScore;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by sherrypan on 16-5-25.
 */
@Document
public class Moment extends AbstractDocument {

    //资源路径
    @DBRef
    private List<Resource> resources = new ArrayList<Resource>();

    //文章内容
    @TextIndexed
    private String article;

    //创建者
    @DBRef
    private User creater;

    //创建时间
    @CreatedDate
    private Date createdDate;

    //获得的赞数
    private Integer like;

    //内容类型
    private String type;// music picture video

    //是否显示 true代表显示 false代表不显示（已被删除）
    private boolean status;

    public Moment() {
        super();
        status = true;
        like=0;
    }

    public Moment(String type, User creater) {
        super();
        this.type = type;
        this.creater = creater;
        this.createdDate = Date.from(Instant.now());
        like = 0;
        status = true;
    }

    public String getArticle() {
        return article;
    }

    public void setArticle(String article) {
        this.article = article;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public List<Resource> getResources() {
        return resources;
    }

    public void setResources(List<Resource> resources) {
        this.resources = resources;
    }

    public User getCreater() {
        return creater;
    }

    public void setCreater(User creater) {
        this.creater = creater;
    }

    public Integer getLike() {
        return like;
    }

    public void setLike(Integer like) {
        this.like = like;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String toString()
    {
        return "Moment:[User:"+creater.toString()+"\n]";
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}
