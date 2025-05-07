package com.xiaoyao.flow.utils;

import lombok.Getter;

/**
 * @author 逍遥
 */
@Getter
public enum ResultCode {
    SUCCESS(200, "操作成功"),
    BAD_REQUEST(400, "参数错误"),
    USERNAME_PASSWORD_INVALID_EXCEPTION(402, "用户名或密码错误"),
    SERVER_ERROR(500, "服务异常"),
    NOT_LOGIN(401, "登录已过期，请重新登录"),
    SAVE_FAIL(10001, "保存失败");

    private final int code;
    private final String message;

    ResultCode(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
