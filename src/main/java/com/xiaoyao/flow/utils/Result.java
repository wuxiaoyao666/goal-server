package com.xiaoyao.flow.utils;

import lombok.Data;

/**
 * @author 逍遥
 */
@Data
public class Result<T> {

    /**
     * 状态码
     */
    private int code;
    /**
     * 数据
     */
    private T data;
    /**
     * 业务消息
     */
    private String message;

    public static <T> Result<T> success() {
        return success(null);
    }

    public static <T> Result<T> success(T data) {
        Result<T> result = new Result<>();
        result.setCode(ResultCode.SUCCESS.getCode());
        result.setMessage(ResultCode.SUCCESS.getMessage());
        result.setData(data);
        return result;
    }

    public static <T> Result<T> fail(String message) {
        return fail(ResultCode.SERVER_ERROR.getCode(),message);
    }

    public static <T> Result<T> fail(ResultCode resultCode) {
        return fail(resultCode.getCode(),resultCode.getMessage());
    }

    public static <T> Result<T> fail(int code, String message) {
        Result<T> result = new Result<>();
        result.setCode(code);
        result.setMessage(message);
        return result;
    }
}
