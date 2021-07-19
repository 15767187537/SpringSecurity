package com.xztx.controller.web;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.SpringSecurityMessageSource;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.security.Security;

/**
 * @Auther: 梦学谷 www.mengxuegu.com
 */
@Controller
public class MainController {

    @RequestMapping({"/index", "/", ""})
    public String index() {
        return "index";// resources/templates/index.html
    }

    /***
     * 获取用户信息的第一种方式
     */
    @RequestMapping("/user/info1")
    @ResponseBody
    public UserDetails getUserInfo() {
        UserDetails userDetails = null;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object principal = authentication.getPrincipal();
        if(principal instanceof UserDetails) {
            userDetails = (UserDetails) principal;
        }
        return userDetails;
    }

    /***
     * 获取用户信息的方式2
     * @param authentication
     * @return
     */
    @RequestMapping("/user/info2")
    @ResponseBody
    public Object getUserInfo2(Authentication authentication) {
        return authentication.getPrincipal();
    }

    /***
     * 获取用户信息的方式3
     * @param userDetails
     * @return
     */
    @RequestMapping("/user/info3")
    @ResponseBody
    public Object getUserInfo3(@AuthenticationPrincipal UserDetails userDetails) {
        return userDetails;
    }

}
