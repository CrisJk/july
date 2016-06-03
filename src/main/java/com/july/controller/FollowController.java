package com.july.controller;

import com.july.service.UserService;
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
    UserService userService;
    @RequestMapping(value="/followControl",method = RequestMethod.GET,produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String followControlInJson( @RequestParam(value="type") Integer type,
                                       @RequestParam(value="operate_email") String operate_email )
    {
        Gson gson = new Gson();
        JsonObject jo = new JsonObject();
        User current_user = userService.getSessionUser();
        User operate_user = userService.getUserByEmail(operate_email);
        if(type==1) //1代表关注
        {
            userService.addFollowing(current_user,operate_user);
            userService.addFollower(operate_user,current_user);
        }
        else        //取消关注
        {
            userService.removeFollowing(current_user,operate_user);
            userService.removeFollower(operate_user,current_user);
        }
        return "error";
    }
}
