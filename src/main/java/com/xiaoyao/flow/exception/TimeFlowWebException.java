package com.xiaoyao.flow.exception;

import cn.dev33.satoken.exception.NotLoginException;
import com.xiaoyao.flow.utils.Result;
import com.xiaoyao.flow.utils.ResultCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author 逍遥
 * 统一异常处理
 */
@Slf4j
@RestControllerAdvice(basePackages = "com.xiaoyao.flow.controller")
public class TimeFlowWebException {

    /**
     * 全局异常处理
     *
     * @param e 异常
     * @return 错误信息
     */
    @ExceptionHandler(Exception.class)
    public Result<Void> handleGlobalException(Exception e) {
        if (e instanceof NotLoginException) {
            return Result.fail(ResultCode.NOT_LOGIN);
        } else if (e instanceof BusinessException be) {
            return Result.fail(be.getCode(), be.getMessage());
        } else if (e instanceof MethodArgumentNotValidException ex) {
            StringBuilder sb = new StringBuilder();
            for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
                sb.append(fieldError.getField()).append(":").append(fieldError.getDefaultMessage()).append("\n");
            }
            return Result.fail(ResultCode.SERVER_ERROR.getCode(), sb.toString());
        } else if (e instanceof TimeFlowException ex) {
            return Result.fail(ex.getMessage());
        }
        log.error("系统异常: ", e);
        return Result.fail(ResultCode.SERVER_ERROR.getCode(), "系统繁忙，请稍后再试");
    }
}
