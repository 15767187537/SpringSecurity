package com.xztx.config;

import com.xztx.authentication.mobile.SmsCodeSender;
import com.xztx.authentication.mobile.SmsSend;
import com.xztx.authentication.session.CustomerInvalidSessionStrategy;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.web.session.InvalidSessionStrategy;

@Configuration
public class SecurityConfigBean {

    @Bean
    @ConditionalOnMissingBean(InvalidSessionStrategy.class)
    public InvalidSessionStrategy getInvalidSessionStrategy() {
        return new CustomerInvalidSessionStrategy();
    }

    @Bean
    public SmsSend smsSend() {
        return new SmsCodeSender();
    }

}
