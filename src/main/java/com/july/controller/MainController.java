package com.july.controller;

import com.july.controller.form.UserCreateForm;
import com.july.entity.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by sherrypan on 16-5-25.
 */
@Controller
public class MainController {

    @RequestMapping("/")
    public ModelAndView index(HttpServletRequest request) {
        ModelAndView mav = new ModelAndView("index");
        String notBind = "bind";
        if(request.getSession() != null && request.getSession().getAttribute("notBind") != null) {
            notBind = (String) request.getSession().getAttribute("notBind");
            if (notBind == "notBind") {
//                UserCreateForm userCreateForm = (UserCreateForm) request.getSession().getAttribute("form");
                mav = new ModelAndView("register", "form", new UserCreateForm());
            }
            String type = (String) request.getSession().getAttribute("type");
            String identity = (String) request.getSession().getAttribute("identity");
            mav.addObject("type", type);
            mav.addObject("identity", identity);
        }
        else{
            mav = new ModelAndView("index", "form", new UserCreateForm());
        }
        mav.addObject("notBind", notBind);
        return mav;
    }

    /*@ModelAttribute("currentUser")
    public User getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() && !"anonymousUser".equals(auth.getPrincipal())) {
            return (User) auth.getPrincipal();
        }
        return null;
    }*/

}