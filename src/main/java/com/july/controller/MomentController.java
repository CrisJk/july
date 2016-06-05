package com.july.controller;

import com.july.entity.Moment;
import com.july.entity.Resource;
import com.july.entity.User;
import com.july.service.MomentService;
import com.july.service.ResourceService;
import com.july.service.UserService;
import com.mongodb.gridfs.GridFSDBFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsOperations;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by sherrypan on 16-6-3.
 */
@Controller
public class MomentController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    UserService userService;

    @Autowired
    MomentService momentService;

    @Autowired
    ResourceService resourceService;

    @RequestMapping(value = "/resource/upload", method = RequestMethod.POST)
    public String handleFormUpload(@RequestParam String type, @RequestParam MultipartFile[] files, HttpServletRequest request) {
        String referer = request.getHeader("Referer");
        BigInteger userId = userService.getSessionUser().getId();
        User user = userService.getUserById(userId);
        //上传头像
        if (type.equals("avatar")) {
            MultipartFile file = files[0];
            if (file != null && !file.isEmpty()) {
                String resourceId = resourceService.save(file);
                String filename = file.getOriginalFilename();
                logger.info("resource Id = " + resourceId);
                if (resourceId != null) {
                    Resource resource = new Resource(resourceId, file.getContentType(), filename);
                    resourceService.saveResource(resource);
                }
                user.setAvatarAddress(resourceId);
                userService.update(user);
                logger.info("Avatar updated successfully.");
                userService.reloadSessionUser(user);
            }
            return "redirect:"+ referer;
        } else {
            //上传动态
            Moment moment = new Moment(type, user);
            List<Resource> resourceList = new ArrayList<>();
            try {
                if (files != null && files.length > 0) {
                    for (int i = 0; i < files.length; i++) {
                        MultipartFile file = files[i];
                        if (file == null || file.isEmpty()) continue;
                        String resourceId = resourceService.save(file);
                        String filename = file.getOriginalFilename();
                        logger.info("resource Id = " + resourceId);
                        if (resourceId != null) {
                            Resource resource = new Resource(resourceId, file.getContentType(), filename);
                            resourceService.saveResource(resource);
                            resourceList.add(resource);
                        }
                    }
                    moment.setResources(resourceList);
                    momentService.save(moment);

                    //本用户时间线添加该动态
                    List<Moment> timeline = user.getTimeline();
                    timeline.add(moment);
                    user.setTimeline(timeline);
                    userService.update(user);
                    //todo:关注本用户的人的时间线添加该动态

                    logger.info("Moment saved successfully.");
                }
                return "redirect:"+ referer;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
    }

    @Autowired
    GridFsOperations gridOperations;

    @RequestMapping(value = "/resource/view/{resourceId}", method = RequestMethod.GET)
    public void viewDigest(HttpServletResponse response, @PathVariable String resourceId) throws ServletException, IOException {
        GridFSDBFile gridFSDBFiles = gridOperations.findOne(
                new Query().addCriteria(Criteria.where("_id").is(resourceId)));
        logger.info("resource Id = " + resourceId);
        ServletOutputStream stream = null;
        BufferedInputStream buf = null;
        try {
            stream = response.getOutputStream();
            String fileName = gridFSDBFiles.getFilename();
            //set response headers
            response.setContentType(gridFSDBFiles.getContentType());
            response.addHeader("Content-Disposition", "attachment; filename=" + fileName);
            response.setContentLength((int) gridFSDBFiles.getLength());
            buf = new BufferedInputStream(gridFSDBFiles.getInputStream());
            int readBytes = 0;
            //read from the file; write to the ServletOutputStream
            while ((readBytes = buf.read()) != -1)
                stream.write(readBytes);
        } catch (IOException ioe) {
            logger.error("Could not show resource " + resourceId);
            throw new ServletException(ioe.getMessage());
        } finally {
            if (stream != null)
                stream.close();
            if (buf != null)
                buf.close();
        }
    }
}
