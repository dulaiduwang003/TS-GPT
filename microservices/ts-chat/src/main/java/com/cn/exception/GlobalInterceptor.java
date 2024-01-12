package com.cn.exception;


import cn.dev33.satoken.exception.DisableServiceException;
import cn.dev33.satoken.exception.NotLoginException;
import cn.dev33.satoken.exception.NotRoleException;
import cn.dev33.satoken.util.SaResult;
import com.cn.msg.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;


/**
 * 全局异常处理
 *
 * @author 时间海 @github dulaiduwang003
 * @version 1.0
 */
@RestControllerAdvice
@SuppressWarnings("all")
@Slf4j
@Component
public class GlobalInterceptor {


    @ExceptionHandler(NotLoginException.class)
    public Result handlerException(NotLoginException e) {
        return Result.error("登录信息失效,请重新登录", 401);
    }

    @ExceptionHandler(NotRoleException.class)
    public Result handlerException(NotRoleException e) {
        return Result.error("非法越界", 401);
    }

    @ExceptionHandler(DisableServiceException.class)
    public Result handlerException(DisableServiceException e) {
        return Result.error("因多次违规相关规定,您已被封禁一天!", 401);
    }

    @ResponseBody
    @ExceptionHandler(value = Exception.class)
    public SaResult exceptionHandler(Exception e) {
        if (e instanceof MethodArgumentNotValidException) {
            final String message = e.getMessage();
            final List<ObjectError> allErrors = ((MethodArgumentNotValidException) e).getBindingResult().getAllErrors();
            return SaResult.error(allErrors.get(0).getDefaultMessage());
        }
        log.warn("服务出现了未被拦截异常信息 信息:{} 位置:{}", e.getMessage(), e.getClass());
        return SaResult.error(e.getMessage());
    }
}
