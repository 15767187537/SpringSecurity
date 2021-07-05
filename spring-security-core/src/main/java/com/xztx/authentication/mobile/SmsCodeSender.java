package com.xztx.authentication.mobile;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SmsCodeSender implements SmsSend {

    @Override
    public boolean smsSend(String mobile, String content) {
        String sendContent = "发送的验证码为" + content + ", 请勿泄露";
        log.info("发送的手机号为: {}, 验证码为: {}", mobile, sendContent);
        return true;
    }
}
