package com.xztx.authentication.mobile;

import com.xztx.authentication.CustomAuthenticationFailureHandler;
import com.xztx.controller.MobileLoginController;
import com.xztx.exception.ValidateCodeException;
import com.xztx.service.UserCustomerService;
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

/**
 * 手机验证码过滤器
 */
@Component
public class MobileValidateFilter extends OncePerRequestFilter {

    @Autowired
    private UserCustomerService userCustomerService;

    @Autowired
    private CustomAuthenticationFailureHandler customAuthenticationFailureHandler;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // 1、判断请求是否为'/login/form'
        String uri = request.getRequestURI();
        String method = request.getMethod();
        if("/mobile/form".equals(uri) && "post".equalsIgnoreCase(method)) {
            // 是登录接口, 判断手机号是否已经注册, 暂时先不测
            String mobile = request.getParameter("mobile");
            try {
                // 判断手机验证码是否正确
                String sessionCode = (String) request.getSession().getAttribute(MobileLoginController.SESSION_KEY_MOBILE_CODE);
                String paramCode = request.getParameter("code");
                if(StringUtils.isBlank(paramCode)) {
                    // 用户输入的验证码为空
                    throw new ValidateCodeException("验证码为空");
                }
                if(!sessionCode.equalsIgnoreCase(paramCode)) {
                    // 用户输入验证码错误
                    throw new ValidateCodeException("验证码输入错误");
                }
            } catch (AuthenticationException e) {
                // 调用失败过滤器
                e.printStackTrace();
                customAuthenticationFailureHandler.onAuthenticationFailure(request,response, e);
                return ;
            }

        }
        // 放过，通过过滤器
        filterChain.doFilter(request, response);
    }
}
