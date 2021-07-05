package com.xztx.exception;


import org.springframework.security.core.AuthenticationException;

/***
 * 验证码Exception
 */
public class ValidateCodeException extends AuthenticationException {

    public ValidateCodeException(String msg) {
        super(msg);
    }

    public ValidateCodeException(String msg, Throwable ex) {
        super(msg, ex);
    }
}
