package com.july.entity;

import com.sun.org.apache.regexp.internal.RE;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.File;
import java.util.Date;

/**
 * Created by sherrypan on 16-6-3.
 */
@Document
public class Resource extends AbstractDocument {

    private String fileName;

    //private File file;

    public Resource() {
        super();
    }

    public Resource(String fileName, File file) {
        super();
        this.fileName = fileName;
        //this.file = file;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

//    public File getFile() {
//        return file;
//    }
//
//    public void setFile(File file) {
//        this.file = file;
//    }

}
