package com.honzooban.questionnairesystem.common;

/**
 * @author honzooban
 * @version 1.0.0
 * @ClassName ResultCode.java
 * @Description 状态码枚举类
 * @createTime 2019年12月08日 19:51:00
 */
public enum ResultCodeEnum {

    /**
     * 常用的状态码
     */
    SUCCESS(200, "操作成功"),
    FAILED(500, "操作失败,请重试"),
    VALIDATE_FAILED(400, "参数检验失败"),
    UNAUTHORIZED(401, "暂未登录或token已经过期"),
    FORBIDDEN(403, "没有相关权限"),
    NOT_FOUND(404, "找不到数据");

    private Integer code;
    private String message;

    ResultCodeEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
