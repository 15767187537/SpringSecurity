package com.xztx.authentication.mobile;

import com.xztx.authentication.CustomAuthenticationFailureHandler;
import com.xztx.authentication.CustomAuthenticationSuccessHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;

@Component
public class MobileAuthenticationConfig extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {

    @Autowired
    private CustomAuthenticationFailureHandler customAuthenticationFailureHandler;

    @Autowired
    private CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler;

    @Autowired
    private UserDetailsService mobileUserDetailsService;

    @Override
    public void configure(HttpSecurity http) throws Exception {
        // 容器中已经存在的AuthenticationManager 传入 mobileAuthenticationFilter中
        MobileAuthenticationFilter mobileAuthenticationFilter = new MobileAuthenticationFilter();
        mobileAuthenticationFilter.setAuthenticationManager(http.getSharedObject(AuthenticationManager.class));
        // 把验证成功或失败的handler添加进去
        mobileAuthenticationFilter.setAuthenticationSuccessHandler(customAuthenticationSuccessHandler);
        mobileAuthenticationFilter.setAuthenticationFailureHandler(customAuthenticationFailureHandler);

        // 创建provider，并把userDetailsService添加进去
        MobileAuthenticationProvider provider = new MobileAuthenticationProvider();
        provider.setUserDetailsService(mobileUserDetailsService);

        // 将provider绑定到HTTPSecurity中，并将手机号认证过滤器绑定到用户名认证过滤器之后
        http.authenticationProvider(provider).addFilterAfter(mobileAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

    }
}
