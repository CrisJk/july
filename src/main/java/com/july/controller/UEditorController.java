//package com.july.controller;
//
//import com.baidu.ueditor.MyActionEnter;
//import com.july.JulyApplication;
//import org.json.JSONException;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.ApplicationContext;
//import org.springframework.context.support.ClassPathXmlApplicationContext;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.ResponseBody;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//
///**
// * Created by kuangjun on 2016/6/4.
// */
//@Controller
//public class UEditorController {
//    private static final Logger logger = LoggerFactory.getLogger(JulyApplication.class) ;
//    @Value("classpath:static/config/config.json")
//    private String configJSONPath;
//
//    @RequestMapping("/controller")
//    public @ResponseBody
//    String controller(HttpServletRequest request, HttpServletResponse response) throws IOException, JSONException {
//        request.setCharacterEncoding("utf-8");
//        response.setHeader("Content-Type", "text/html");
//        @SuppressWarnings("resource")
//        ApplicationContext appContext = new ClassPathXmlApplicationContext();
//        String baseState = new MyActionEnter(request, appContext.getResource(configJSONPath).getInputStream()).exec();
//        // response.getWriter().write(baseState);
//        return baseState;
//    }
//
//}
