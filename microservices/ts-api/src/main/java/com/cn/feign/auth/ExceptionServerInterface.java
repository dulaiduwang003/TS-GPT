package com.cn.feign.auth;

import com.cn.dto.SubmitExceptionDto;
import com.cn.fallback.ExceptionInterfaceFallback;
import com.cn.filter.FeignInterceptor;
import com.cn.msg.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * 异常信息暴露接口
 *
 * @author bdth @github dulaiduwang003
 * @version 1.0
 */
@FeignClient(
        name = "auth-server",
        configuration = FeignInterceptor.class,
        fallbackFactory = ExceptionInterfaceFallback.class
)
public interface ExceptionServerInterface {

    /**
     * 提交异常信息
     *
     * @param dto the dto
     * @return the result
     */
    @PostMapping(value = "/exception-api/submit/exception", produces = MediaType.APPLICATION_JSON_VALUE)
    Result submitException(@RequestBody @Validated SubmitExceptionDto dto);


}
