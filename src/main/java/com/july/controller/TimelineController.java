package com.july.controller;

import com.july.entity.Moment;
import com.july.entity.User;
import com.july.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by sherrypan on 16-5-27.
 */
@Controller
public class TimelineController {


    private final Logger logger = LoggerFactory.getLogger(getClass()) ;
    @Autowired
    UserService userService;

    @RequestMapping(value="/timeline", method = RequestMethod.GET)
    public ModelAndView showTimeline(
            @RequestParam(value="current_page",defaultValue = "1") int current_page,
            @RequestParam(value="page_size",defaultValue = "3") int page_size,
            @RequestParam(value="aim_user_id",required=false) BigInteger aim_user_id
                                     ) {

        ModelAndView mav = new ModelAndView("timeline");
        User user = null;
        User current_user = userService.getSessionUser();
        if(aim_user_id!=null) user = userService.getUserById(aim_user_id);
        else user = userService.getSessionUser();
        logger.info("enter timeline");
        logger.info(user.getEmail());
        System.out.println("******"+user.getId()+"*************") ;
        mav.addObject("aim_user",user);
        mav.addObject("current_user",current_user);

        //得到它的时间线
        List<Moment> timeline = user.getTimeline();
        System.out.println("timeline_size:"+timeline.size());
        int total_page = (timeline.size()+page_size-1)/page_size;

        //得到当前page，即current_page
        if(current_page<1) current_page=1;
        if(current_page>total_page&&total_page!=0) current_page=total_page;

        //根据分页信息得到时间线
        List<Moment> result_timeline = new ArrayList<>();
        int end = (current_page*page_size <= timeline.size()) ? current_page*page_size : timeline.size();
        System.out.println("end:***"+end);
        System.out.println("current_page:  " + current_page);
        System.out.println("page_size: "+page_size);
        System.out.println("ddd:"+(current_page-1)*page_size);
        for( int i = (current_page-1)*page_size; i < end ;i++ )
        {
            System.out.println(i+"***************");
            if(timeline.size()!=0)result_timeline.add(timeline.get(i));
        }
        mav.addObject("timeline",result_timeline);
        mav.addObject("current_page",current_page);
        mav.addObject("page_size",page_size);
        mav.addObject("total_page",total_page);
        mav.addObject("total_articles",timeline.size()); //文章数
        mav.addObject("total_followers",current_user.getFollowers().size());
        mav.addObject("total_followings",current_user.getFollowings().size());
        System.out.println(mav);
        return mav;
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
