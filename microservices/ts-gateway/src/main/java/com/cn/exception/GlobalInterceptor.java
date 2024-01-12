package com.cn.exception;

import cn.dev33.satoken.util.SaResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.support.WebExchangeBindException;

import java.util.List;

/**
 * The type Global interceptor.
 */
@RestControllerAdvice
@SuppressWarnings("all")
public class GlobalInterceptor {


    /**
     * Web exchange bind exception sa result.
     *
     * @param e the e
     * @return the sa result
     */
    @ResponseBody
    @ExceptionHandler(value = Exception.class)
    public SaResult WebExchangeBindException(Exception e) {
        if (e instanceof WebExchangeBindException) {
            final List<ObjectError> allErrors = ((WebExchangeBindException) e).getBindingResult().getAllErrors();
            return SaResult.error((allErrors.get(0).getDefaultMessage()));
        }
        e.printStackTrace();
        return SaResult.error(e.getMessage());
    }

}
