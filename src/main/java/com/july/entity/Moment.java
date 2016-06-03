package com.july.entity;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.mongodb.core.index.TextIndexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.*;

/**
 * Created by sherrypan on 16-5-25.
 */
@Document
public class Moment extends AbstractDocument {

    //资源路径
    private List<String> path = new ArrayList<String>();

    //文章内容
    @TextIndexed
    private String article;

    //创建者
    private User creater;

    //创建时间
    @CreatedDate
    private Date createdDate;

    //获得的赞数
    private int like;

    //内容类型
    private String type;// music picture video

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

    public List<String> getPath() {
        return path;
    }

    public void setPath(List<String> path) {
        this.path = path;
    }

    public User getCreater() {
        return creater;
    }

    public void setCreater(User creater) {
        this.creater = creater;
    }

    public int getLike() {
        return like;
    }

    public void setLike(int like) {
        this.like = like;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

}
