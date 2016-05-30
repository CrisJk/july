package com.july.registration;

import com.july.entity.User;
import com.july.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.core.env.Environment;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * Created by sherrypan on 16-5-30.
 */
@Component
public class RegistrationListener implements ApplicationListener<OnRegistrationCompleteEvent> {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    UserService userService;

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private Environment env;
    
    @Override
    public void onApplicationEvent(final OnRegistrationCompleteEvent event) {
        logger.info("confirm registration event...");
        this.confirmRegistration(event);
    }

    private void confirmRegistration(final OnRegistrationCompleteEvent event) {
        final User user = event.getUser();
        final String token = UUID.randomUUID().toString();
        userService.createVerificationTokenForUser(user, token);
        logger.info("construct registration email...");
        final SimpleMailMessage email = constructEmailMessage(event, user, token);
        logger.info(email.toString());
        mailSender.send(email);
        logger.info("send email successfully...");
    }

    private SimpleMailMessage constructEmailMessage(OnRegistrationCompleteEvent event, User user, String token) {
        final String recipientAddress = user.getEmail();
        final String subject = "Registration Confirmation";
        final String confirmationUrl = event.getAppUrl() + "/registrationConfirm?token=" + token;
        final String message = "您好，请完成邮箱验证。\n" +
                "\n" + "为保证您能正常使用「July」，请在注册账户后尽快验证您的邮箱。（未进行邮箱验证的账户将会在一天内被系统删除，所有保存的文件也将丢失。）打开以下链接验证邮箱：";
        final SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(recipientAddress);
        email.setSubject(subject);
        email.setText(message + " \r\n" + confirmationUrl);
        email.setFrom(env.getProperty("support.email"));
        return email;
    }

}
