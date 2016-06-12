package com.july.controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.july.entity.Moment;
import com.july.entity.User;
import com.july.service.MomentService;
import com.july.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
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
    @Autowired
    MomentService momentService ;

    @Autowired
    MomentService momentService;

    @RequestMapping(value="/timeline", method = RequestMethod.GET)
    public ModelAndView showTimeline(
            @RequestParam(value="current_page",defaultValue = "1") int current_page,
            @RequestParam(value="page_size",defaultValue = "10") int page_size,
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
        List<Moment> timelines = user.getTimeline();
        List<Moment> timeline = new ArrayList<>();
        if(current_user.getId().compareTo(user.getId())==0)
        {
            for (int i = 0; i < timelines.size(); i++) {
                if (timelines.get(i).isStatus() == true) {
                    timeline.add(timelines.get(i));
                }
            }
        }
        else
        {
            for( int i = 0 ; i < timelines.size(); i ++ )
            {
                if(timelines.get(i).isStatus()==true && timelines.get(i).getCreater().getId().compareTo(user.getId())==0)
                {
                    timeline.add(timelines.get(i));
                }
            }
        }
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

        int total_articles = 0;
        List<Moment> current_user_timeline = current_user.getTimeline();
        for( int i = 0 ; i < current_user_timeline.size(); i ++ )
        {
            Moment tmpMoment = current_user_timeline.get(i);
            if(tmpMoment.getCreater().getId().compareTo(current_user.getId())==0&&tmpMoment.isStatus()) total_articles++;
        }
        mav.addObject("total_articles",total_articles); //文章数

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

    @RequestMapping(value = "/fullTextSearch", method = RequestMethod.GET)
    public ModelAndView showFullTextSearch(@RequestParam(value="content") String content,
                                           @RequestParam(value="page_size",defaultValue = "5") int page_size,
                                           @RequestParam(value="current_page",defaultValue="1") int current_page) {

        ModelAndView mav = new ModelAndView("search");
        mav.addObject("content", content);
        /*计算页数*/
        if (current_page < 1) current_page = 1;
        int total_pages;
        int total_size;

        //计算搜索总页数
        total_size = momentService.findMomentCountByContentInPage(content);
        if (total_size > 0) {
            total_pages = (total_size + page_size) / page_size;
        } else {
            total_pages = 1;
        }
        if (current_page > total_pages && total_pages != 0) current_page = total_pages;

        Pageable pageable = new PageRequest(current_page - 1, page_size);
        Page<Moment> momentPage = momentService.findMomentByContentInPage(content, pageable);
        List<Moment> momentList = new ArrayList<>();
        if (momentPage != null) {
            momentList = momentPage.getContent();
        }

        for (int i = 0; i < momentList.size(); i++) {
            System.out.println(momentList.get(i).getArticle() + "  moment的样子");
        }

        mav.addObject("total_pages", total_pages);//总页数
        mav.addObject("current_page", current_page);//当前页
        mav.addObject("page_size", page_size);//每页显示的数量
        mav.addObject("moments", momentList);
        User user = userService.getSessionUser();
        mav.addObject("current_user", user);
        return mav;
    }


    @RequestMapping(value="/addLike",method = RequestMethod.GET,produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String likeMoment( @RequestParam(value="id") BigInteger id,
                              @RequestParam(value="like") int like)
    {
        System.out.println("moement id:"+id+"********************");
        System.out.println("moment like:"+like+"********************");


        Gson gson = new Gson();
        JsonObject jo = new JsonObject();

        Moment moment = momentService.getMomentById(id) ;
        boolean success = false;
        moment.setLike(++like);
        momentService.save(moment) ;
        System.out.println("moment like :"+like+"********************");
        success = true;
        jo.addProperty("success",success) ;
        jo.addProperty("like",like) ;
        return gson.toJson(jo);
    }

}
