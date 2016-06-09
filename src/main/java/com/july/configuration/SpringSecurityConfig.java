package com.july.configuration;

import com.july.controller.form.UserCreateForm;
import com.july.entity.Account;
import com.july.entity.User;
import com.july.service.AccountService;
import com.july.service.SecUserDetailsService;
import com.july.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.resource.ResourceServerProperties;
import org.springframework.boot.autoconfigure.security.oauth2.resource.UserInfoTokenServices;
import org.springframework.boot.context.embedded.FilterRegistrationBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.filter.OAuth2ClientAuthenticationProcessingFilter;
import org.springframework.security.oauth2.client.filter.OAuth2ClientContextFilter;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;
import org.springframework.security.oauth2.client.token.grant.code.AuthorizationCodeResourceDetails;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.filter.CompositeFilter;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by kuangjun on 2016/5/7.
 */
@EnableWebSecurity
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private SecUserDetailsService userDetailsService;

    @Autowired
    private UserService userService;

    @Autowired
    private AccountService accountService;

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .userDetailsService(userDetailsService)
                .passwordEncoder(new BCryptPasswordEncoder());
    }

    @Autowired
    OAuth2ClientContext oauth2ClientContext;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .antMatcher("/**")
            .authorizeRequests()
                .antMatchers("/", "/index", "/login/**", "/register","/css/**",  "/js/**","/images/**","/registrationConfirm","/waitForEmailValidate","/webjars/**").permitAll()
                .anyRequest().authenticated()
            .and().exceptionHandling().authenticationEntryPoint(new LoginUrlAuthenticationEntryPoint("/"))
            .and().formLogin().loginPage("/").usernameParameter("username").passwordParameter("password").defaultSuccessUrl("/timeline").permitAll()
            .and().logout().logoutSuccessUrl("/").permitAll()
            .and().addFilterBefore(ssoFilter(), BasicAuthenticationFilter.class)
                .addFilterAfter(checkUserFilter(), FilterSecurityInterceptor.class);
    }

    @Bean
    public FilterRegistrationBean oauth2ClientFilterRegistration(
            OAuth2ClientContextFilter filter) {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(filter);
        registration.setOrder(-100);
        return registration;
    }

    @Bean
    @ConfigurationProperties("github")
    ClientResources github() {
        return new ClientResources();
    }

    @Bean
    @ConfigurationProperties("facebook")
    ClientResources facebook() {
        return new ClientResources();
    }

    private Filter ssoFilter() {
        CompositeFilter filter = new CompositeFilter();
        List<Filter> filters = new ArrayList<>();
        filters.add(ssoFilter(facebook(), "/login/facebook"));
        filters.add(ssoFilter(github(), "/login/github"));
        filter.setFilters(filters);
        return filter;
    }

    private Filter ssoFilter(ClientResources client, String path) {
        OAuth2ClientAuthenticationProcessingFilter oAuth2ClientAuthenticationFilter =
                new OAuth2ClientAuthenticationProcessingFilter(path);
        OAuth2RestTemplate oAuth2RestTemplate = new OAuth2RestTemplate(client.getClient(),
                oauth2ClientContext);
        oAuth2ClientAuthenticationFilter.setRestTemplate(oAuth2RestTemplate);
        UserInfoTokenServices tokenServices = new UserInfoTokenServices(
                client.getResource().getUserInfoUri(), client.getClient().getClientId());
        tokenServices.setRestTemplate(oAuth2RestTemplate);
        oAuth2ClientAuthenticationFilter.setTokenServices(tokenServices);
        return oAuth2ClientAuthenticationFilter;
    }

    private Filter checkUserFilter() {
        return new OncePerRequestFilter() {
            @Override
            protected void doFilterInternal(HttpServletRequest request,
                                            HttpServletResponse response, FilterChain filterChain)
                    throws ServletException, IOException {
                Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
                if (authentication instanceof OAuth2Authentication) {
                    //第三方登陆用户
                    Map mp = (Map) ((OAuth2Authentication) authentication).getUserAuthentication().getDetails();
                    logger.info("get information from github or facebook " + mp.toString());
                    String type;
                    Object ob = mp.get("login");
                    if (ob != null) {
                        type = "github";
                        ob = mp.get("id");
                    } else {
                        type = "facebook";
                        ob = mp.get("id");
                    }
                    logger.info("login with " + type + " account. Id=" + ob);
                    User user = userService.getOAuthUserByAccount(type, ob.toString());
                    if (user == null) {
                        //第一次登陆该第三方帐号
                        Account account = accountService.saveOrUpdateAccount(type, mp);
                        logger.info("First time to login with this account, generate a new local user.");
                        //重新注册邮箱，创建一个本地新用户
                        UserCreateForm userCreateForm = new UserCreateForm();
                        if (type == "github") {
                            userCreateForm.setGithubAccount(account);
                            logger.info("Set Github Account in UserCreateForm.");
                        } else {
                            userCreateForm.setFacebookAcount(account);
                            logger.info("Set Facebook Account in UserCreateForm.");
                        }
                        //重定向到register页，参数为userCreateForm
                        request.getSession().setAttribute("form", userCreateForm);
                        request.getSession().setAttribute("notBind", "notBind");
                        request.getSession().setAttribute("type", type);
                        request.getSession().setAttribute("identity", account.getIdentity());
                    }
                    else {
                        //更新user的Account
                        Account account = accountService.saveOrUpdateAccount(type, mp);
                        logger.info("Update the account information.");
                        //切换到本地用户user
                        userService.reloadSessionUser(user);
                        User currentUser = userService.getSessionUser();
                        //返回主页
                        logger.info("Current user is " + currentUser.toString());
                        request.getSession().setAttribute("notBind", "bind");
                    }
                } else if (authentication instanceof Authentication) {
                    //邮箱登陆
                    logger.info("login with email address.");
                    //返回主页
                }
                filterChain.doFilter(request, response);
            }
        };
    }

}

class ClientResources {
    private OAuth2ProtectedResourceDetails client = new AuthorizationCodeResourceDetails();
    private ResourceServerProperties resource = new ResourceServerProperties();

    public OAuth2ProtectedResourceDetails getClient() {
        return client;
    }

    public ResourceServerProperties getResource() {
        return resource;
    }
}