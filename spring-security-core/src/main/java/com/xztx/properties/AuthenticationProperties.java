package com.xztx.properties;

import lombok.Data;

@Data
public class AuthenticationProperties {

    private String loginPage;

    private String loginProcessingUrl;

    private String usernameParameter;

    private String passwordParameter;

    private String[] staticPaths;

    private String loginResponseType; // json/redirect

    private String imageCodeUrl; // 获取图形验证码地址

    private String mobilePage; // mobile/page # 发送手机验证码地址

    private String mobileCodeUrl; // /code/mobile # 前往手机登录页面

    private Integer tokenValiditySeconds = 60 * 60 * 24 * 7; // 记住我时效
}
