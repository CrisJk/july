package com.july.entity;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.mongodb.core.index.TextIndexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by sherrypan on 16-5-25.
 */
@Document
public class Moment extends AbstractDocument {

    private Set<String> picturePath = new HashSet<String>();

    @Field("music")
    private String musicPath;

    @TextIndexed
    private String article;

    @Field("vedio")
    private String vedioPath;

    @CreatedDate
    private Date createdDate;

    public Set<String> getPicturePath() {
        return picturePath;
    }

    public void setPicturePath(Set<String> picturePath) {
        this.picturePath = picturePath;
    }

    public String getMusicPath() {
        return musicPath;
    }

    public void setMusicPath(String musicPath) {
        this.musicPath = musicPath;
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

    public String getVedioPath() {
        return vedioPath;
    }

    public void setVedioPath(String vedioPath) {
        this.vedioPath = vedioPath;
    }
}
