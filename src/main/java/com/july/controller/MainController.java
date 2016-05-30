package com.july.controller;

import com.july.entity.Moment;
import com.july.service.MomentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by sherrypan on 16-5-25.
 */
@Controller
public class MainController {

    @RequestMapping("/")
    public ModelAndView index() {
        ModelAndView mav = new ModelAndView("index");
        return mav;
    }

    @RequestMapping({ "/user", "/me" })
    @ResponseBody
    public Map<String, String> user(Principal principal) {
        Map<String, String> map = new LinkedHashMap<>();
        map.put("name", principal.getName());
        return map;
    }

}
