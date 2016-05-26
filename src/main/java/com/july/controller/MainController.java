package com.july.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * Created by sherrypan on 16-5-25.
 */
@Controller
public class MainController {

    @RequestMapping("/index")
    public ModelAndView index() {
        ModelAndView mav = new ModelAndView("index");
        return mav;
    }

}
