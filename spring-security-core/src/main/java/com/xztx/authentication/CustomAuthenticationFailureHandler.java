package com.xztx.authentication;

import com.xztx.properties.AuthenticationProperties;
import com.xztx.properties.LoginResponseType;
import com.xztx.properties.SecurityProperties;
import com.xztx.result.ResultMessage;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component("customAuthenticationFailureHandler")
public class CustomAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    @Autowired
    private SecurityProperties securityProperties;

    /**
     *
     * @param httpServletRequest
     * @param httpServletResponse
     * @param e  报错信息
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public void onAuthenticationFailure(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException, ServletException {
        if(LoginResponseType.JSON.getCode().equals(securityProperties.getAuthentication().getLoginResponseType())) {
            ResultMessage resultMessage = ResultMessage.build(HttpStatus.UNAUTHORIZED.value(), e.getMessage());
            httpServletResponse.getWriter().write(resultMessage.toJsonString());
            httpServletResponse.setContentType("application/json;charset=UTF-8");
            httpServletResponse.getWriter().write(resultMessage.toJsonString());
        }else {
//            super.setDefaultFailureUrl(securityProperties.getAuthentication().getLoginPage() + "?error");
            String referer = httpServletRequest.getHeader("Referer");
            System.out.println("referer: " + referer);
            Object toAuthentication = httpServletRequest.getAttribute("toAuthentication");
            String url = "";
            if(toAuthentication  == null) {
                url = StringUtils.substringBefore(referer, "?");
            }else {
                url = securityProperties.getAuthentication().getLoginPage();
            }
            System.out.println("上一次访问的页面url: " + url);
            super.setDefaultFailureUrl(url + "?error");
            super.onAuthenticationFailure(httpServletRequest, httpServletResponse, e);
        }
    }
}
