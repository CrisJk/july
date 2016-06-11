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
import org.springframework.web.bind.annotation.*;
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

import com.google.gson.Gson;
import com.google.gson.JsonObject;

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

    private boolean dealWithFiles(String type, User user, MultipartFile[] files) {
        Moment moment = new Moment(type, user);
        List<Resource> resourceList = new ArrayList<>();
        try {
            if (files != null && files.length > 0) {
                System.out.println("文件的个数共有"+ files.length +"个");
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
                System.out.println("这条动态共有" + resourceList.size() + "个资源");
                moment.setResources(resourceList);
                momentService.save(moment);
                //本用户时间线添加该动态
                saveMomentInTimelineSelf(user, moment);
                //todo:关注本用户的人的时间线添加该动态

                logger.info("Moment with type " + type + " saved successfully.");
                return true;
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @RequestMapping(value = "/qqPicture/upload", method = RequestMethod.POST)
    @ResponseBody
    public void handleQqPictureUpload(@RequestParam String type,
                                      @RequestParam(value="qqfile") MultipartFile file,
                                      HttpServletResponse response) throws IOException {
        Gson gson = new Gson();
        JsonObject json1 = new JsonObject();
        BigInteger userId = userService.getSessionUser().getId();
        User user = userService.getUserById(userId);
        //上传动态(图片)
        try {
            if (file != null) {
                String resourceIdentity = resourceService.save(file);
                String filename = file.getOriginalFilename();
                logger.info("resource Id = " + resourceIdentity);
                if (resourceIdentity != null) {
                    Resource resource = new Resource(resourceIdentity, file.getContentType(), filename);
                    resourceService.saveResource(resource);
                }
                json1.addProperty("resourceId", resourceIdentity);
                json1.addProperty("success", true);
                response.setCharacterEncoding("UTF-8");
                response.setContentType("text");
                response.getWriter().print(json1);
                response.flushBuffer();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
    }

    //发布动态（图片）
    @RequestMapping(value = "/pictures/upload", method = RequestMethod.GET)
    @ResponseBody
    public String handleFormUpload(@RequestParam("ids") List<String> ids) throws IOException {
        Gson gson = new Gson();
        JsonObject json1 = new JsonObject();
        BigInteger userId = userService.getSessionUser().getId();
        User user = userService.getUserById(userId);
        String type = "pictures";
        Moment moment = new Moment(type, user);
        List<Resource> resourceList = new ArrayList<>();
        //上传动态(图片)
        System.out.println("总共有" + ids.size() + "张图片");

        for (int i = 0; i < ids.size(); i++) {
            String identity = ids.get(i).replace("[", "");
            identity = identity.replace("]", "");
            identity = identity.replace("\"", "");
            Resource resource = resourceService.getResourceByIdentity(identity);   //new BigInteger(id, 16)
            if (resource == null) {
                logger.info("Error! can't find resource.");
            } else {
                resourceList.add(resource);
            }
        }
        moment.setResources(resourceList);
        momentService.save(moment);
        //本用户时间线添加该动态
        saveMomentInTimelineSelf(user, moment);
        //todo:关注本用户的人的时间线添加该动态
        saveMomentInTimelineFollow(user, moment);
        logger.info("Moment with type " + type + " saved successfully.");
        json1.addProperty("success", true);
        return gson.toJson(json1);
    }

    //关注本用户的人的时间线添加动态
    private void saveMomentInTimelineFollow(User user, Moment moment) {

    }

    //本用户时间线添加动态到时间线
    private void saveMomentInTimelineSelf(User user, Moment moment) {

        List<Moment> timeline = user.getTimeline();
        System.out.println("添加之前时间线个数："+timeline.size());
        timeline.add(moment);
        user.setTimeline(timeline);
        userService.update(user);
        System.out.println("添加之后时间线个数："+user.getTimeline().size());
    }

    //上传头像
    @RequestMapping(value = "/avatar/upload", method = RequestMethod.POST)
    public String handleAvatarUpload(@RequestParam String type, @RequestParam MultipartFile[] files, HttpServletRequest request) {
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
        }
        return "redirect:"+ referer;
    }

    //发布动态（音乐、视频）
    @RequestMapping(value = "/resource/upload", method = RequestMethod.POST)
    public String handleResourceUpload(@RequestParam String type, @RequestParam MultipartFile[] files, HttpServletRequest request) {
        String referer = request.getHeader("Referer");
        BigInteger userId = userService.getSessionUser().getId();
        User user = userService.getUserById(userId);
        //上传动态（音乐视频）
        if (dealWithFiles(type, user, files)) {
            return "redirect:"+ referer;
        } else {
            return "redirect:"+ referer;
        }
    }

    @Autowired
    GridFsOperations gridOperations;

    //查看资源（图片、视频、音频）
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

    @RequestMapping(value="deleteMomentInJson",method = RequestMethod.GET,produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String deleteMomentInJson(
            @RequestParam(value="moment_id") BigInteger moment_id
    )
    {
        Gson gson = new Gson();
        JsonObject jo = new JsonObject();
        momentService.deleteMomentById(moment_id);
        jo.addProperty("success",true);
        return gson.toJson(jo);
    }
}
