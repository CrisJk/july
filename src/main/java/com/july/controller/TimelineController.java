package com.july.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by sherrypan on 16-5-27.
 */
@Controller
public class TimelineController {

    @RequestMapping("/timeline")
    public String showTimeline() {
        return "timeline";
    }

}
