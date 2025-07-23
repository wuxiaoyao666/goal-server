package com.xiaoyao.goal.exception;

import com.xiaoyao.goal.utils.ResultCode;
import lombok.Getter;

@Getter
public class BusinessException extends RuntimeException {

    private final int code;

    private final Object data;

    private final String i18nKey;

    //---------------- 核心构造器 ----------------
    public BusinessException(ResultCode resultCode) {
        this(resultCode, resultCode.getMessage(), null, null);
    }

    public BusinessException(ResultCode resultCode, String message) {
        this(resultCode, message, null, null);
    }

    public BusinessException(ResultCode resultCode, String message, Object data) {
        this(resultCode, message, data, null);
    }

    public BusinessException(ResultCode resultCode, String message, Object data, String i18nKey) {
        super(message);  // 保留原始异常机制
        this.code = resultCode.getCode();
        this.data = data;
        this.i18nKey = i18nKey;
    }
}
