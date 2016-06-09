package com.july.controller;

import com.july.entity.User;
import com.july.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by sherrypan on 16-5-27.
 */
@Controller
public class TimelineController {

    @Autowired
    UserService userService;

    @RequestMapping("/timeline")
    public String showTimeline(Model model) {
        User user = userService.getSessionUser();
        model.addAttribute("user", user);
        return "timeline";
    }

    @RequestMapping("/music")
    public String showTmp() {
        return "playMusic";
    }

    @RequestMapping("/video")
    public String showVideo() {
        return "playVideo";
    }



}
