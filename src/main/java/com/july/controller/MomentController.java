package com.july.controller;

import com.july.entity.Moment;
import com.july.entity.User;
import com.july.service.MomentService;
import com.july.service.ResourceService;
import com.july.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by sherrypan on 16-6-3.
 */
@Controller
@RequestMapping("/moment")
public class MomentController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    UserService userService;

    @Autowired
    MomentService momentService;

    @Autowired
    ResourceService resourceService;

//    @RequestMapping(value = "/upload")
//    public String fileUpload() {
//        resourceService.save("");
//        return "playMusic";
//    }
//
//    @RequestMapping(value = "/read")
//    public String fileRead() {
//        resourceService.read();
//        return "index";
//    }

    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public String handleFormUpload(@RequestParam String type, @RequestParam MultipartFile[] files) {
        BigInteger userId = userService.getSessionUser().getId();
        User user = userService.getUserById(userId);
        Moment moment = new Moment(type, user);
        List<String> resourceList = new ArrayList<>();
        try {
            if (files != null && files.length > 0){
                for(int i = 0; i < files.length; i++){
                    MultipartFile file = files[i];
                    if (file == null || file.isEmpty()) continue;
                    String resourceId = resourceService.save(file);
                    System.out.println("resource Id = " + resourceId);
                    if (resourceId != null) {
                        resourceList.add(resourceId);
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
            return "redirect:/";
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
