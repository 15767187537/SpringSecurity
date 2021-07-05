package com.xztx.properties;

public enum LoginResponseType {

    JSON("json", 1),

    REDIRECT("redirect", 2),
    ;

    String code;

    Integer type;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    LoginResponseType(String code, Integer type){
        this.code = code;
        this.type = type;
    }
}
