package com.july.entity;

import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Created by sherrypan on 16-6-4.
 */
@Document
public class Resource extends AbstractDocument {

    private String identity;

    private String type;

    private String name;

    public Resource() {
        super();
    }

    public Resource(String identity, String type, String name) {
        super();
        this.identity = identity;
        this.type = type;
        this.name = name;
    }

    public String getIdentity() {
        return identity;
    }

    public void setIdentity(String identity) {
        this.identity = identity;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String toString()
    {

        return "\nResource:[identity:"+identity+"\ttype:"+type+"\tname:"+name+"]\n";
    }
}
