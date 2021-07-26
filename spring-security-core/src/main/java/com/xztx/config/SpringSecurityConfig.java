package com.xztx.config;

import com.xztx.authentication.code.ImageCodeValidateFilter;
import com.xztx.authentication.mobile.MobileAuthenticationConfig;
import com.xztx.authentication.mobile.MobileValidateFilter;
import com.xztx.properties.SecurityProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.session.InvalidSessionStrategy;
import org.springframework.security.web.session.SessionInformationExpiredStrategy;

import javax.sql.DataSource;


@Configuration
@EnableWebSecurity // 开启SpringSecurity的过滤链
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {

    Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private SecurityProperties securityProperties;

    @Autowired
    private UserDetailsService customUserDetailsService;

    @Autowired
    private AuthenticationSuccessHandler customAuthenticationSuccessHandler;

    @Autowired
    private AuthenticationFailureHandler customAuthenticationFailureHandler;

    @Autowired
    private ImageCodeValidateFilter imageCodeValidateFilter;

    @Autowired
    private DataSource dataSource;

    @Autowired
    private MobileValidateFilter mobileValidateFilter;

    @Autowired
    private MobileAuthenticationConfig mobileAuthenticationConfig;

    @Autowired
    private InvalidSessionStrategy invalidSessionStrategy;

    @Autowired
    private SessionInformationExpiredStrategy sessionInformationExpiredStrategy;

    /***
     * 资源权限配置:
     * 被拦截的资源
     * @param http
     * @throws Exception
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
//        http.httpBasic() // 采用httpBasic方式进行认证
        http.addFilterBefore(mobileValidateFilter, UsernamePasswordAuthenticationFilter.class)
            .addFilterBefore(imageCodeValidateFilter, UsernamePasswordAuthenticationFilter.class) // 在验证用户名密码Filter之前使用
            .formLogin() // 采用表单方式进行认证
            .loginPage(securityProperties.getAuthentication().getLoginPage()) // 重定向到登录界面
            .loginProcessingUrl(securityProperties.getAuthentication().getLoginProcessingUrl()) // 登录表单提交的url，
            .usernameParameter(securityProperties.getAuthentication().getUsernameParameter()) // 接收表单里的账户(如果不是username，就得重新定义)
            .passwordParameter(securityProperties.getAuthentication().getPasswordParameter()) // 接收表单里的密码(如果不是password，就得重新定义)
            .successHandler(customAuthenticationSuccessHandler)
            .failureHandler(customAuthenticationFailureHandler)
            .and()
            .authorizeRequests() // 认证请求
            .mvcMatchers(securityProperties.getAuthentication().getLoginPage(),
                    securityProperties.getAuthentication().getImageCodeUrl(),
                    securityProperties.getAuthentication().getMobilePage(),
                    securityProperties.getAuthentication().getMobileCodeUrl()
                    ).permitAll()
            .anyRequest().authenticated() //所有访问该应用的http请求都要通过身份认证才可以进行访问
            .and()
            .rememberMe() // 记住功能配置
            .tokenRepository(jdbcTokenRepository()) //保存登录信息
            .tokenValiditySeconds(securityProperties.getAuthentication().getTokenValiditySeconds()) //记住我有效时长
            .and()
            .sessionManagement()
            .invalidSessionStrategy(invalidSessionStrategy)
            .maximumSessions(1) // 只允许一个账户进行登录
            .expiredSessionStrategy(sessionInformationExpiredStrategy) // 引入只允许一个用户登录具体实现
            .maxSessionsPreventsLogin(true);
        ;
        http.csrf().disable();// 关闭跨域请求拦截
        http.apply(mobileAuthenticationConfig);
    }

    /***
     * 认证管理器
     * 1、认证信息(用户名、密码)
     * @param auth
     * @throws Exception
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        String password = passwordEncoder().encode("123456");
//        logger.info("登录的加密后的密码: {}", password);
//        auth.inMemoryAuthentication()
//                .withUser("xztx") // 设置固定账号
//                .password(password) // 设置固定密码
//                .authorities("ADMIN") // 设置权限，如果不设置，会报错
//        ;
        auth.userDetailsService(customUserDetailsService);
    }

    /**
     * 记住我功能bean
     * @return
     */
    @Bean
    public JdbcTokenRepositoryImpl jdbcTokenRepository() {
        JdbcTokenRepositoryImpl jdbcTokenRepository = new JdbcTokenRepositoryImpl();
        jdbcTokenRepository.setDataSource(dataSource);
        // 是否启动项目时自动创建表，true自动创建
//        jdbcTokenRepository.setCreateTableOnStartup(true);
        return jdbcTokenRepository;
    }

    /***
     * 密码加密
     * @return
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * 静态资源过滤
     * @param web
     */
    @Override
    public void configure(WebSecurity web) {
        web.ignoring().antMatchers(securityProperties.getAuthentication().getStaticPaths());
    }
}
