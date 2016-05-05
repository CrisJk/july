package com.july.configuration;


import org.hibernate.dialect.MySQLDialect;

/**
 * Created by kuangjun on 2016/5/5.
 */
public class UTF8MySQLDialect extends MySQLDialect {
    @Override
    public String getTableTypeString() {
        return "DEFAULT CHARSET=utf8" ;
    }
}
