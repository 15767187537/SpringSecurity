package com.xztx.authentication.code;

import com.xztx.authentication.CustomAuthenticationFailureHandler;
import com.xztx.controller.WebController;
import com.xztx.exception.ValidateCodeException;
import com.xztx.properties.SecurityProperties;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/***
 * 添加一个验证码过滤器，在验证用户名密码之前使用, 使用要判断uri是否为'/login/form'
 */
@Component("imageCodeValidateFilter")
public class ImageCodeValidateFilter extends OncePerRequestFilter {

    @Autowired
    private SecurityProperties securityProperties;

    @Autowired
    private CustomAuthenticationFailureHandler customAuthenticationFailureHandler;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        boolean b = securityProperties.getAuthentication().getLoginProcessingUrl().equals(request.getRequestURI())
                && "post".equalsIgnoreCase(request.getMethod());
        // 判断请求是否为post请求，url是否为'/login/form'
        if(b) {
            // 是post请求，url也是/login/form
            try {
                // 1、获取session中验证码
                String sessionCode = (String)request.getSession().getAttribute(WebController.SESSION_KEY);
                // 2、获取用户输入的验证码
                String userCode = request.getParameter("code");
                if(StringUtils.isBlank(userCode)) {
                    // 用户输入的验证码为空
                    throw new ValidateCodeException("验证码为空");
                }
                if(!sessionCode.equalsIgnoreCase(userCode)) {
                    throw new ValidateCodeException("验证码输入错误");
                }
            } catch (AuthenticationException e) {
                // 验证码有误，定向到登录失败的Handler中
                customAuthenticationFailureHandler.onAuthenticationFailure(request, response, e);
                return ;
            }
        }
        // 没有问题，放行
        filterChain.doFilter(request, response);
    }
}
