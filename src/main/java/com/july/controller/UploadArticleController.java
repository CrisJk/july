package com.july.controller;

import com.july.entity.Moment;
import com.july.entity.User;
import com.july.service.MomentService;
import com.july.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.math.BigInteger;
import java.util.List;

/**
 * Created by kuangjun on 2016/6/4.
 */

@Controller
public class UploadArticleController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    MomentService momentService ;
    @Autowired
    UserService userService ;

    @RequestMapping(value="/saveArticle",method=RequestMethod.GET)
    String saveArticle()
    {
        return "saveArticle" ;
    }
    @RequestMapping(value="/saveArticle",method = RequestMethod.POST)
    String uploadArticle(@RequestParam String article)
    {
        logger.info("Enter");
        BigInteger userId = userService.getSessionUser().getId();
        User user = userService.getUserById(userId) ;
        Moment moment = new Moment("article",user) ;
        moment.setArticle(article);
        try{
            momentService.save(moment);
            //本用户时间线添加该动态
            List<Moment> timeline = user.getTimeline();
            timeline.add(moment);
            user.setTimeline(timeline);
            userService.update(user);
            //todo:关注本用户的人的时间线添加该动态
            logger.info("Moment save successfully!");
            return "redirect:/" ;
        }
        catch (Exception ex) {
            ex.printStackTrace();
            return null ;
        }

    }
}
