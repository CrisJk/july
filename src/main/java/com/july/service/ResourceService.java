package com.july.service;

import com.july.entity.Resource;
import com.july.repository.ResourceRepository;
import com.mongodb.gridfs.GridFSFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
    ResourceRepository resourceRepository;

    @Autowired
    GridFsOperations gridOperations;

    public String save(MultipartFile file) {
        InputStream inputStream = null;
        GridFSFile gridFSFile = null;
        try {
            inputStream = file.getInputStream();
            String contentType = file.getContentType();
            String filename = file.getOriginalFilename();
            logger.info("contentType = " + contentType + "; filename = " + filename + "; size = " + file.getSize());
            gridFSFile = gridOperations.store(inputStream, filename, contentType);
            logger.info("Resource " + filename + " saved successfully.");
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
            return gridFSFile.getId().toString();
        }
    }

    public void saveResource(Resource resource) {
        resourceRepository.save(resource);
    }

    public Resource getResourceByIdentity(String identity) {
        List<Resource> resourceList = resourceRepository.findByIdentity(identity);
        if (resourceList != null && resourceList.size() == 1) {
            return resourceList.get(0);
        }
        return null;
    }

}
