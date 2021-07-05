package com.xztx.config;

import com.xztx.authentication.mobile.SmsCodeSender;
import com.xztx.authentication.mobile.SmsSend;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SecurityConfigBean {

    @Bean
    public SmsSend smsSend() {
        return new SmsCodeSender();
    }

}
