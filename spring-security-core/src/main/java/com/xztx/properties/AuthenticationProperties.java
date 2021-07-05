package com.xztx.properties;

public class AuthenticationProperties {

    private String loginPage;

    private String loginProcessingUrl;

    private String usernameParameter;

    private String passwordParameter;

    private String[] staticPaths;

    private String loginResponseType; // json/redirect

    public String getLoginResponseType() {
        return loginResponseType;
    }

    public void setLoginResponseType(String loginResponseType) {
        this.loginResponseType = loginResponseType;
    }

    public String getLoginPage() {
        return loginPage;
    }

    public void setLoginPage(String loginPage) {
        this.loginPage = loginPage;
    }

    public String getLoginProcessingUrl() {
        return loginProcessingUrl;
    }

    public void setLoginProcessingUrl(String loginProcessingUrl) {
        this.loginProcessingUrl = loginProcessingUrl;
    }

    public String getUsernameParameter() {
        return usernameParameter;
    }

    public void setUsernameParameter(String usernameParameter) {
        this.usernameParameter = usernameParameter;
    }

    public String getPasswordParameter() {
        return passwordParameter;
    }

    public void setPasswordParameter(String passwordParameter) {
        this.passwordParameter = passwordParameter;
    }

    public String[] getStaticPaths() {
        return staticPaths;
    }

    public void setStaticPaths(String[] staticPaths) {
        this.staticPaths = staticPaths;
    }
}
