package com.july.service;

import com.mongodb.gridfs.GridFSDBFile;
import com.mongodb.gridfs.GridFSFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsOperations;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * Created by sherrypan on 16-6-3.
 */
@Service
public class ResourceService {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    GridFsOperations gridOperations;

    public String save(MultipartFile file) {
        InputStream inputStream = null;
        try {
            inputStream = file.getInputStream();
            String contentType = file.getContentType();
            String filename = file.getOriginalFilename();
            logger.info("contentType = " + contentType + "; filename = " + filename + "; size = " + file.getSize());
            GridFSFile gridFSFile = gridOperations.store(inputStream, filename, contentType);
            logger.info("Resource " + filename + " saved successfully.");
            logger.info("ID = " + gridFSFile.getId() + ", hey" + ((String) gridFSFile.getId())); //????
            return ((String) gridFSFile.getId());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }
    }

    public void read() {
        List<GridFSDBFile> result = gridOperations.find(
                new Query().addCriteria(Criteria.where("filename").is("test.jpg")));
        System.out.println(result.getClass());

        for (GridFSDBFile file : result) {
            try {
                System.out.println(file.getFilename());
                System.out.println(file.getContentType());
                //save as another image
                file.writeTo("/home/sherrypan/Desktop/test1.jpg");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
