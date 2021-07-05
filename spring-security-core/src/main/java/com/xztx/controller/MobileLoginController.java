package com.xztx.controller;

import com.xztx.authentication.mobile.SmsSend;
import com.xztx.result.ResultMessage;
import org.apache.commons.lang.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@Controller
public class MobileLoginController {

    @Autowired
    private SmsSend smsSend;

    public static final String SESSION_KEY_MOBILE_CODE = "SESSION_KEY_MOBILE_CODE_";

    /***
     * 跳转到手机验证登录页面
     * @return
     */
    @RequestMapping("/mobile/page")
    public String getMobilePage() {
        return "login-mobile";
    }

    @ResponseBody
    @RequestMapping("/code/mobile")
    public ResultMessage smsCode(HttpServletRequest request) {
        // 1、生成验证码
        String code = RandomStringUtils.randomNumeric(4);
        // 2、把验证码存到session中
        request.getSession().setAttribute(SESSION_KEY_MOBILE_CODE, code);
        // 3、获取手机号
        String mobile = request.getParameter("mobile");
        // 4、发送验证码
        smsSend.smsSend(mobile, code);
        return ResultMessage.ok();
    }

}
