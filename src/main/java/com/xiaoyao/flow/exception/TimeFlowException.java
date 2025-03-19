package com.xiaoyao.flow.exception;

import com.xiaoyao.flow.utils.Result;
import com.xiaoyao.flow.utils.ResultCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

/**
 * @author 逍遥
 * 统一异常处理
 */
@Slf4j
@RestControllerAdvice(basePackages = "com.xiaoyao.flow.controller")
public class TimeFlowException {

    // 业务异常处理
    @ExceptionHandler(BusinessException.class)
    public Result<Void> handleBusinessException(BusinessException e) {
        log.error("业务异常: {}", e.getMessage());
        return Result.fail(e.getCode(), e.getMessage());
    }

    // 参数校验异常处理
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result<Map<String, String>> handleValidationException(MethodArgumentNotValidException ex) {
        StringBuilder sb = new StringBuilder();
        for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
            sb.append(fieldError.getField()).append(":").append(fieldError.getDefaultMessage()).append("\n");
        }
        return Result.fail(ResultCode.BAD_REQUEST.getCode(), sb.toString());
    }

    // 系统级异常处理
    @ExceptionHandler(Exception.class)
    public Result<Void> handleGlobalException(Exception e) {
        log.error("系统异常: ", e);
        return Result.fail(ResultCode.SERVER_ERROR.getCode(), "系统繁忙，请稍后再试");
    }
}
