package com.july.controller;

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
        System.out.println("type:"+type+"************************");
       // System.out.println("aim_user_email:"+aim_user_email+"********************");
       // System.out.println("current_user_email:"+current_user_email+"********************");
        Gson gson = new Gson();
        JsonObject jo = new JsonObject();
        //User current_user = userService.getUserByNickName("163邮箱");
        User current_user = userService.getUserByEmail(current_user_email);
        User aim_user = userService.getUserByEmail(aim_user_email);
        boolean success = false;
        if(type==1) //1代表关注
        {
            userService.addFollowing(current_user,aim_user);
            userService.addFollower(aim_user,current_user);
            success = true;
            jo.addProperty("success",success);
        }
        else        //取消关注
        {
            userService.removeFollowing(current_user,aim_user);
            userService.removeFollower(aim_user,current_user);
            success = true;
            jo.addProperty("success",success);
            return gson.toJson(jo);
        }
        return gson.toJson(jo);
    }
}
