package com.july.controller;

import com.july.entity.Moment;
import com.july.entity.User;
import com.july.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
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
            @RequestParam(value="aim_user_id",required=false) BigInteger aim_user_id,
            HttpServletRequest request)
    {
        ModelAndView mav = new ModelAndView("timeline");
        User current_user = userService.getSessionUser();
        current_user = userService.getUserById(current_user.getId());
        BigInteger userId = null;
        if(aim_user_id!=null) userId = aim_user_id;
        else userId = userService.getSessionUser().getId();

        User user = userService.getUserById(userId);
        //得到它的时间线
        List<Moment> timeline = user.getTimeline();

        int total_page = (timeline.size()+page_size-1)/page_size;

        //得到当前page，即current_page
        if(current_page<1) current_page=1;
        if(current_page>total_page&&total_page!=0) current_page=total_page;

        //根据分页信息得到时间线
        List<Moment> result_timeline = new ArrayList<>();
        int end = (current_page*page_size <= timeline.size()) ? current_page*page_size : timeline.size();
        for( int i = (current_page-1)*page_size; i < end ;i++ )
        {
            if(timeline.size()!=0)result_timeline.add(timeline.get(i));
        }
        mav.addObject("timeline",result_timeline);
        mav.addObject("current_page",current_page);
        mav.addObject("page_size",page_size);
        mav.addObject("total_page",total_page);
        mav.addObject("total_articles",timeline.size()); //文章数
        System.out.println("current_user:\t"+current_user);
        List<String> followerss = current_user.getFollowers();
        System.out.println("*********************enter");
        for (String dd:
             followerss) {
            System.out.println(dd);
        }
        System.out.println("*********************exit");
        System.out.println(current_user.getFollowers().size());
        mav.addObject("total_followers",current_user.getFollowers().size());
        mav.addObject("total_followings",current_user.getFollowings().size());
        mav.addObject("aim_user",user);
        mav.addObject("current_user",current_user);
        mav.addObject("context", request.getContextPath());
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
