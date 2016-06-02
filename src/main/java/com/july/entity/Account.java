package com.july.entity;

import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Created by sherrypan on 16-6-1.
 */
@Document
public class Account extends AbstractDocument {

    private String type;

    private String identity;

    private String name;

    private String link;

    public Account() {
        super();
    }

    public Account(final String type, final String identity, final String name, final String link) {
        super();
        this.type = type;
        this.identity = identity;
        this.name = name;
        this.link = link;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getIdentity() {
        return identity;
    }

    public void setIdentity(String identity) {
        this.identity = identity;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    @Override
    public String toString() {
        return "Account [id=" + id + ", type=" + type + ", link=" + link + "]";
    }

}
