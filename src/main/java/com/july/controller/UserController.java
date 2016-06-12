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
        String referer = request.getHeader("Referer");
        if (bindingResult.hasErrors()) {
            return "redirect:"+ referer;
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
                return "redirect:"+ referer;
            }
        } catch (DataIntegrityViolationException e) {
            bindingResult.reject("email.exists", "邮箱已存在！");
            return "redirect:"+ referer;
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
    @RequestMapping(value={"/listUserByNickname"}, method = RequestMethod.GET)
    public ModelAndView listUserByNickname(
            @RequestParam(value="nickname") String nickname,
            @RequestParam(value="page_size",defaultValue = "5") int page_size,
            @RequestParam(value="current_page",defaultValue="1") int current_page) {
        ModelAndView mav = new ModelAndView("listUsers");
        mav.addObject("nickname",nickname);
        /*计算页数*/
        if(current_page<1) current_page = 1;
        int total_pages;
        List<User> users = userService.getUserByNickName(nickname);
        if (users != null) {
            total_pages = (users.size() + page_size) / page_size;
        } else {
            total_pages = 1;
        }
        if(current_page > total_pages && total_pages!= 0 ) current_page = total_pages;

        int num = 0;
        User current_user = userService.getSessionUser();
        System.out.println("UserController current_user:"+current_user.toString());

        mav.addObject("current_user",current_user);
        Pageable pageable = new PageRequest(current_page-1,page_size);
        Page<User> aim_page_users = userService.getUserByNickNameInPage(nickname,pageable);
        //mav.addObject("aim_users",aim_users);

        if(aim_page_users != null)
        {
            //System.out.println("UserController:\n"+aim_page_users);
            num = 1;
            List<User> aim_users = aim_page_users.getContent();
            for(int i=0;i<aim_users.size();i++)
            {
                //System.out.println("TEST:**************");
                List<String> aim_user_followers = aim_users.get(i).getFollowers();
                /*for( int k = 0; k < aim_user_followers.size(); k ++ )
                {
                    System.out.println(aim_user_followers.get(i));
                }*/
                //System.out.println(aim_users.get(i));
                if(aim_user_followers!=null&&aim_user_followers.contains(current_user.getEmail()))
                {
                    aim_users.get(i).setIs_followed("YES");
                }
                else aim_users.get(i).setIs_followed("NO");
            }
            mav.addObject("aim_users",aim_users);
            mav.addObject("total_pages",aim_page_users.getTotalPages());//总页数
            mav.addObject("current_page",aim_page_users.getNumber());//当前页
            mav.addObject("page_size",aim_page_users.getSize());//每页显示的数量
        }
        mav.addObject("num",num);
        mav.addObject("type","nickname");
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

            return "redirect:/timeline";
        } else {
            logger.error("Password error, can't bind user.");
        }
        return "index";
    }

}
