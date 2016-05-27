package com.july.controller;

import com.july.entity.Moment;
import com.july.service.MomentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.Date;

/**
 * Created by sherrypan on 16-5-25.
 */
@Controller
public class MainController {

    private static final Logger logger = LoggerFactory.getLogger(MainController.class);

    @Autowired
    MomentService momentService;

    @RequestMapping("/index")
    public ModelAndView index() {
        ModelAndView mav = new ModelAndView("index");
        return mav;
    }

    @RequestMapping("/save")
    public String saveArticle() {
        Moment moment = new Moment();
        moment.setArticle("hello kitty");
        moment.setCreatedDate(new Date());
        momentService.save(moment);
        logger.info("save moment success");
        return "index";
    }

}
