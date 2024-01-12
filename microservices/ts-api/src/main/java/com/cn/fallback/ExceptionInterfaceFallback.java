package com.cn.fallback;

import com.cn.feign.auth.ExceptionServerInterface;
import com.cn.msg.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

/**
 * 异常信息调用异常回滚
 *
 * @author bdth @github dulaiduwang003
 * @version 1.0
 */
@Component
@Slf4j
public class ExceptionInterfaceFallback implements FallbackFactory<ExceptionServerInterface> {

    @Override
    public ExceptionServerInterface create(Throwable throwable) {
        return dto -> {
            log.error("异常提交服务调用失败 原因:{}", throwable.getMessage());
            return Result.error("异常提交服务调用失败");
        };
    }
}
