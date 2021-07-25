package com.xztx.authentication.session;

import com.xztx.result.ResultMessage;
import org.springframework.http.HttpStatus;
import org.springframework.security.web.session.InvalidSessionStrategy;
import org.springframework.stereotype.Component;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CustomerInvalidSessionStrategy implements InvalidSessionStrategy {
    /***
     * session失效后返回Json串，前端接收到在进行重定向
     * @param request
     * @param response
     * @throws IOException
     */
    @Override
    public void onInvalidSessionDetected(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // 删除cookie里的JSESSIONID
        cancelCookie(request, response);
        ResultMessage resultMessage = new ResultMessage().build(HttpStatus.UNAUTHORIZED.value(), "登录超时，请重新登录");
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().write(resultMessage.toJsonString());
    }

    protected void cancelCookie(HttpServletRequest request, HttpServletResponse response) {
        Cookie cookie = new Cookie("JSESSIONID", (String)null);
        cookie.setMaxAge(0);
        cookie.setPath(this.getCookiePath(request));
        response.addCookie(cookie);
    }

    private String getCookiePath(HttpServletRequest request) {
        String contextPath = request.getContextPath();
        return contextPath.length() > 0 ? contextPath : "/";
    }
}
