package com.xztx.authentication;

import com.alibaba.fastjson.JSON;
import com.xztx.properties.LoginResponseType;
import com.xztx.properties.SecurityProperties;
import com.xztx.result.ResultMessage;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component("customAuthenticationSuccessHandler")
@Slf4j
public class CustomAuthenticationSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    @Autowired
    private SecurityProperties securityProperties;


    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest,
                   HttpServletResponse httpServletResponse,
                   Authentication authentication) throws IOException, ServletException {

        if(LoginResponseType.JSON.getCode().equals(securityProperties.getAuthentication().getLoginResponseType())) {
            log.info("Json格式！！！！！！！！！！！！");
            ResultMessage resultMessage = ResultMessage.ok("认证成功");
            httpServletResponse.setContentType("application/json;charset=UTF-8");
            httpServletResponse.getWriter().write(resultMessage.toJsonString());
        }else {
            log.info("authentication: " + JSON.toJSONString(authentication));
            super.onAuthenticationSuccess(httpServletRequest, httpServletResponse, authentication);
        }
    }
}
