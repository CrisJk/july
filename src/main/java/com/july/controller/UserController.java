package com.july.controller;

import com.july.controller.form.UserCreateForm;
import com.july.controller.form.validator.UserCreateFormValidator;
import com.july.entity.Account;
import com.july.entity.User;
import com.july.registration.OnRegistrationCompleteEvent;
import com.july.service.AccountService;
import com.july.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.security.Principal;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
    AccountService accountService;

    @Autowired
    private UserCreateFormValidator userCreateFormValidator;

    @InitBinder("form")
    public void initBinder(WebDataBinder binder) {
        binder.addValidators(userCreateFormValidator);
    }

    @RequestMapping(value = "/register", method = RequestMethod.GET)
    public ModelAndView getUserCreateForm() {
        ModelAndView mav =  new ModelAndView("register", "form", new UserCreateForm());
        mav.addObject("notBind", "new user");
        return mav;
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public String handleUserCreateForm(@Valid @ModelAttribute("form") UserCreateForm form, BindingResult bindingResult, HttpServletRequest request, RedirectAttributes redirectAttributes) throws ServletException {
        if (bindingResult.hasErrors()) {
            return "register";
        }
        try {
            if (userService.getUserByEmail(form.getEmail()) != null) {
                bindingResult.rejectValue("email.exists", "邮箱已存在！");
            }
            User user = userService.create(form);
            try {
                String appUrl = request.getHeader("Host");
                if (request.getSession() != null && request.getSession().getAttribute("type") != null) {
                    String type = (String) request.getSession().getAttribute("type");
                    String identity = (String) request.getSession().getAttribute("identity");
                    if (type == "github") {
                        user.setGithubAccount(accountService.getByTypeAndIdentity(type, identity));
                        logger.info("New user created, Email Sent, Binding the github account.");
                    } else {
                        user.setFacebookAccount(accountService.getByTypeAndIdentity(type, identity));
                        logger.info("New user created, Email Sent, Binding the facebook account.");
                    }
                    userService.update(user);
                }
                eventPublisher.publishEvent(new OnRegistrationCompleteEvent(user, appUrl));
            } catch (Exception me) {
                return "register";
            }
        } catch (DataIntegrityViolationException e) {
            bindingResult.reject("email.exists", "邮箱已存在！");
            return "register";
        }
        redirectAttributes.addFlashAttribute("message", "注册成功!");
        logger.info("Register successfully");
        request.logout();
        return "waitForEmailValidate";
    }

    @RequestMapping(value = "/registrationConfirm", method = RequestMethod.GET)
    public String confirmRegistration(final Model model, @RequestParam("token") final String token) {
        final String result = userService.validateVerificationToken(token);
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

    //根据昵称列出用户
    @RequestMapping(value="/listUserByNickName", method = RequestMethod.GET)
    public ModelAndView listUserByNickName(
            @RequestParam(value="nickname",required = false,defaultValue = "新浪") String nickname
            )
    {
        ModelAndView mav = new ModelAndView("listUserByNickName");

        System.out.println("UserController: ************************已进入**********************");
        System.out.println("UserController: "+nickname+"******************************************");

        int type = 0;
        User current_user = userService.getSessionUser();
        System.out.println("UserController current_user:"+current_user.toString());

        mav.addObject("current_user",current_user);
        Pageable pageable = new PageRequest(0,1);
        Page<User> aim_page_users = userService.getUserByNickNameInPage(nickname,pageable);
        System.out.println("UserController:\n"+aim_page_users);
        //mav.addObject("aim_users",aim_users);

        if(aim_page_users != null)
        {
            type = 1;
            List<User> aim_users = aim_page_users.getContent();
            for(int i=0;i<aim_page_users.getSize();i++)
            {
                List<String> aim_user_followers = aim_users.get(i).getFollowers();
                if(aim_user_followers!=null&&aim_user_followers.contains(current_user.getEmail()))
                {
                    aim_users.get(i).setIs_followed("YES");
                }
                else aim_users.get(i).setIs_followed("NO");
            }
            mav.addObject("aim_users",aim_users);
            mav.addObject("current_page",aim_page_users.getNumber());
            mav.addObject("page_size",aim_page_users.getSize());
        }
        mav.addObject("type",type);
        System.out.println("UserController: "+mav.toString());
        return mav;
    }


    @RequestMapping({ "/user", "/me" })
    @ResponseBody
    public Map<String, String> user(Principal principal) {
        Map<String, String> map = new LinkedHashMap<>();
        map.put("name", principal.getName());
        return map;
    }

    @RequestMapping(value = "/user/detail", method = RequestMethod.GET)
    @ResponseBody
    public Map userDetail(Principal principal) {
        if (principal instanceof OAuth2Authentication) {
            return (Map) ((OAuth2Authentication) principal).getUserAuthentication().getDetails();
        } else {
            return null;
        }
    }

    @RequestMapping(value = "/bindUser", method = RequestMethod.POST)
    public String bindLocalUser(@RequestParam("b_email") String email, @RequestParam("b_password") String password, @RequestParam("identity") String identity, @RequestParam("type") String type, final RedirectAttributes redirectAttributes) {
        User user = userService.getUserByEmail(email);
        if (user == null) {
            //邮箱不存在，返回错误
            logger.error("Email is not exist, can't bind user.");
        } else if (new BCryptPasswordEncoder().matches(password, user.getPassword())) {
            Account account = accountService.getByTypeAndIdentity(type, identity);
            System.out.print("type="+type+", identity="+identity);
            if (type.equals("github")) {
                System.out.println(account.getIdentity());
                user.setGithubAccount(account);
            } else {
                user.setFacebookAccount(account);
            }
            userService.update(user);
            logger.info("Bind local user successfully.");
        } else {
            logger.error("Password error, can't bind user.");
        }
        return "index";
    }

}
