package com.xztx.authentication.session;

import com.xztx.authentication.CustomAuthenticationFailureHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.session.SessionInformationExpiredEvent;
import org.springframework.security.web.session.SessionInformationExpiredStrategy;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import java.io.IOException;

@Component
public class CustomerSessionInformationExpiredStrategy implements SessionInformationExpiredStrategy {

    @Autowired
    private CustomAuthenticationFailureHandler customAuthenticationFailureHandler;

    @Override
    public void onExpiredSessionDetected(SessionInformationExpiredEvent event) throws IOException {
        UserDetails userDetails = (UserDetails) event.getSessionInformation().getPrincipal();
        AuthenticationException authenticationException = new AuthenticationServiceException(userDetails.getUsername() + "用户在另一台设备登录，您以被迫下线");
        event.getRequest().setAttribute("toAuthentication", true);
        try {
            customAuthenticationFailureHandler.onAuthenticationFailure(event.getRequest(), event.getResponse(),authenticationException);
        } catch (ServletException e) {
            e.printStackTrace();
        }
    }
}
