package com.july.controller;

import com.july.entity.Moment;
import com.july.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import com.july.entity.User;

import java.util.List;

/**
 * Created by Crow on 2016/6/3.
 */
@Controller
public class FollowController {
    @Autowired
    UserService userService;

    @RequestMapping(value="/followControlInJson",method = RequestMethod.GET,produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String followControlInJson( @RequestParam(value="type") Integer type,
                                       @RequestParam(value="aim_user_email") String aim_user_email,
                                       @RequestParam(value="current_user_email")String current_user_email)
    {
        System.out.println("FollowControl type:"+type+"************************");
        System.out.println("FollowControl aim_user_email:"+aim_user_email+"********************");
        System.out.println("FollowControl current_user_email:"+current_user_email+"********************");

        Gson gson = new Gson();
        JsonObject jo = new JsonObject();

        User current_user = userService.getUserByEmail(current_user_email);
        User aim_user = userService.getUserByEmail(aim_user_email);
        boolean success = false;
        if(type==1) //1代表关注
        {
            //current_user 关注 aim_user
            List<String> followings = current_user.getFollowings();

            followings.add(aim_user_email);
            List<Moment> aim_user_timeLine = aim_user.getTimeline();
            List<Moment> current_user_timeLine = current_user.getTimeline();
            for(int i = 0; i < aim_user_timeLine.size(); i ++ )
            {
                current_user_timeLine.add(aim_user_timeLine.get(i));
            }
            userService.update(current_user);

            //向aim_user内添加粉丝current_user
            List<String> followers = aim_user.getFollowers();
            followers.add(current_user_email);
            userService.update(aim_user);

            success = true;
            jo.addProperty("success",success);
        }
        else        //取消关注
        {
            List<String> followings = current_user.getFollowings();
            List<String> followers = aim_user.getFollowers();
            followings.remove(aim_user_email);
            followers.remove(current_user_email);
            List<Moment> aim_user_timeLine = aim_user.getTimeline();
            List<Moment> current_user_timeLine = current_user.getTimeline();
            for( int i = 0; i < aim_user_timeLine.size(); i ++ )
            {
                current_user_timeLine.remove(aim_user_timeLine.get(i));
            }
            userService.update(current_user);
            userService.update(aim_user);
            success = true;
            jo.addProperty("success",success);
            return gson.toJson(jo);
        }
        return gson.toJson(jo);
    }
}
