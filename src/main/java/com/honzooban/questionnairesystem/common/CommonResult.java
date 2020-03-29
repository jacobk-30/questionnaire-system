package com.honzooban.questionnairesystem.common;

/**
 * @author honzooban
 * @version 1.0.0
 * @ClassName CommonResult.java
 * @Description 公共返回结果
 * @createTime 2019年12月08日 19:59:00
 */
public class CommonResult<T> {

    private long code;
    private String message;
    private T data;

    public CommonResult() {
    }

    public CommonResult(long code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    /**
     * 成功返回的结果
     * @return
     */
    public static <E> CommonResult<E> success() {
        return new CommonResult<E>(ResultCodeEnum.SUCCESS.getCode(), ResultCodeEnum.SUCCESS.getMessage(), null);
    }

    /**
     * 成功返回的结果
     * @param data 返回数据
     * @return
     */
    public static <E> CommonResult<E> success(E data) {
        return new CommonResult<E>(ResultCodeEnum.SUCCESS.getCode(), ResultCodeEnum.SUCCESS.getMessage(), data);
    }

    /**
     * 成功返回的结果
     * @param message 提示信息
     * @return
     */
    public static <E> CommonResult<E> success(String message) {
        return new CommonResult<E>(ResultCodeEnum.SUCCESS.getCode(), message, null);
    }

    /**
     * 成功返回结果
     * @param data 返回数据
     * @param message 提示信息
     * @return
     */
    public static <E> CommonResult<E> success(String message, E data) {
        return new CommonResult<E>(ResultCodeEnum.SUCCESS.getCode(), message, data);
    }

    /**
     * 失败返回结果
     * @param code 错误状态枚举
     * @return
     */
    public static <E> CommonResult<E> failed(ResultCodeEnum code) {
        return new CommonResult<E>(code.getCode(), code.getMessage(), null);
    }

    /**
     * 失败返回结果
     * @param code 错误状态枚举
     * @param message 提示信息
     * @return
     */
    public static <E> CommonResult<E> failed(ResultCodeEnum code, String message) {
        return new CommonResult<E>(code.getCode(), message, null);
    }

    /**
     * 失败返回结果
     * @param code 错误状态枚举
     * @param data 返回数据
     * @return
     */
    public static <E> CommonResult<E> failed(ResultCodeEnum code, E data) {
        return new CommonResult<E>(code.getCode(), code.getMessage(), data);
    }

    /**
     * 失败返回结果
     * @param code 错误状态枚举
     * @param message 提示信息
     * @param data 返回数据
     * @return
     */
    public static <E> CommonResult<E> failed(ResultCodeEnum code, String message, E data){
        return new CommonResult<E>(code.getCode(), message, data);
    }

    public long getCode() {
        return code;
    }

    public void setCode(long code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
