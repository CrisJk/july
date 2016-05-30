package com.july.controller;

import com.july.controller.form.UserCreateForm;
import com.july.controller.form.validator.UserCreateFormValidator;
import com.july.entity.User;
import com.july.registration.OnRegistrationCompleteEvent;
import com.july.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

/**
 * Created by sherrypan on 16-5-29.
 */
@Controller
public class UserController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private ApplicationEventPublisher eventPublisher;

    @Autowired
    UserService userService;

    @Autowired
    private UserCreateFormValidator userCreateFormValidator;

    @InitBinder("form")
    public void initBinder(WebDataBinder binder) {
        binder.addValidators(userCreateFormValidator);
    }

    @RequestMapping(value = "/register", method = RequestMethod.GET)
    public ModelAndView getUserCreateForm() {
        return new ModelAndView("register", "form", new UserCreateForm());
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public String handleUserCreateForm(@Valid @ModelAttribute("form") UserCreateForm form, BindingResult bindingResult, WebRequest request, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "register";
        }
        try {
            User registered = userService.create(form);
            if (registered == null) {
                bindingResult.rejectValue("email.exists", "邮箱已存在！");
            }
            try {
                String appUrl = request.getHeader("Host");
                eventPublisher.publishEvent(new OnRegistrationCompleteEvent(registered, appUrl));
            } catch (Exception me) {
                return "register";
            }
        } catch (DataIntegrityViolationException e) {
            bindingResult.reject("email.exists", "邮箱已存在！");
            return "register";
        }
        redirectAttributes.addFlashAttribute("message", "注册成功!");
        return "redirect:/";
    }

    @RequestMapping(value = "/registrationConfirm", method = RequestMethod.GET)
    public String confirmRegistration(final Model model, @RequestParam("token") final String token) {
        final String result = userService.validateVerificationToken(token);
        logger.info(result);
        if (result == null) {
            model.addAttribute("message", "Your account verified successfully");
            logger.info("validate succeeded!!!!!");
            return "redirect:/";
        }
        if (result == "expired") {
            model.addAttribute("expired", true);
            model.addAttribute("token", token);
        }
        logger.info("validate FAILED !!!!!");
        model.addAttribute("message", result);
        return "redirect:/";
    }

}
