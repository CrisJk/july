package com.july.entity;

import javax.persistence.Id;
import java.math.BigInteger;

/**
 * Created by sherrypan on 16-5-24.
 */
public class AbstractDocument {

    @Id
    protected BigInteger id;

    public BigInteger getId() {
        return id;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (this.id == null || obj == null || !(this.getClass().equals(obj.getClass()))) {
            return false;
        }
        AbstractDocument that = (AbstractDocument) obj;
        return this.id.equals(that.getId());
    }

    @Override
    public int hashCode() {
        return id == null ? 0 : id.hashCode();
    }

}
