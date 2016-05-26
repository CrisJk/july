package com.july.configuration;

import org.springframework.boot.autoconfigure.web.WebMvcAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import javax.servlet.Filter;

/**
 * Created by kuangjun on 2016/5/7.
 */

@Configuration
@EnableWebMvc
public class MvcConfig extends WebMvcAutoConfiguration {

    @Bean
    public Filter characterEncodingFilter() {
        CharacterEncodingFilter characterEncodingFilter = new CharacterEncodingFilter();
        characterEncodingFilter.setEncoding("UTF-8");
        characterEncodingFilter.setForceEncoding(true);
        return characterEncodingFilter;
    }

//    @Bean
//    public SpringSecurityDialect springSecurityDialect(){
//        return new SpringSecurityDialect();
//    }
//
//    @Bean
//    public Java8TimeDialect java8TimeDialect() {
//        return new Java8TimeDialect();
//    }

//    @Bean
//    public MultipartResolver multipartResolver() {
//        CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver();
//        //multipartResolver.setMaxUploadSize(5000000);
//        return multipartResolver;
//    }

}