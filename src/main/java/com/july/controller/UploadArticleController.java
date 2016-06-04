package com.july.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created by kuangjun on 2016/6/4.
 */

@Controller
public class UploadArticleController {
    @RequestMapping(value="saveArticle",method=RequestMethod.GET)
    String saveArticle()
    {
        return "saveArticle" ;
    }
}
