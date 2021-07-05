package com.xztx.result;

import com.alibaba.fastjson.JSON;
import lombok.Data;

/**
 * 自定义响应结构
 */
@Data
public class ResultMessage {

    // 响应业务状态
    private Integer code;

    // 响应消息
    private String message;

    // 响应中的数据
    private Object data;
	
	public ResultMessage() {
    }
	public ResultMessage(Object data) {
        this.code = 200;
        this.message = "OK";
        this.data = data;
    }
    public ResultMessage(String message, Object data) {
        this.code = 200;
        this.message = message;
        this.data = data;
    }

    public ResultMessage(Integer code, String message, Object data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public static ResultMessage ok() {
        return new ResultMessage(null);
    }
    public static ResultMessage ok(String message) {
        return new ResultMessage(message, null);
    }
    public static ResultMessage ok(Object data) {
        return new ResultMessage(data);
    }
    public static ResultMessage ok(String message, Object data) {
        return new ResultMessage(message, data);
    }

    public static ResultMessage build(Integer code, String message) {
        return new ResultMessage(code, message, null);
    }

    public static ResultMessage build(Integer code, String message, Object data) {
        return new ResultMessage(code, message, data);
    }

    public String toJsonString() {
        return JSON.toJSONString(this);
    }


    /**
     * JSON字符串转成 ResultMessage 对象
     * @param json
     * @return
     */
    public static ResultMessage format(String json) {
        try {
            return JSON.parseObject(json, ResultMessage.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
