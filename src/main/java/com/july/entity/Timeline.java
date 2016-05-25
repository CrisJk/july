//test mongodb connnect
package com.july.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class Timeline extends AbstractDocument {

    @DBRef
    @Indexed
    private User user;

    @DBRef
    private Moment moment;

    public Timeline() {}

    public Timeline(User user, Moment moment) {
        this.user = user;
        this.moment = moment;
    }

    @Override
    public String toString() {
        return String.format("Timeline[id=%s, user_id='%s', moment_id='%s']", id, user.getId(), moment.getId());
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Moment getMoment() {
        return moment;
    }

    public void setMoment(Moment moment) {
        this.moment = moment;
    }

}