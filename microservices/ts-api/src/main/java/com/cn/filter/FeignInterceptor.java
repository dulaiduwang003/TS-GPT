package com.cn.filter;

import cn.dev33.satoken.same.SaSameUtil;
import cn.dev33.satoken.stp.StpUtil;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.stereotype.Component;

/**
 * 下游服务 feign调用安全配置
 *
 * @author bdth @github dulaiduwang003
 * @version 1.0
 */
@Component
public class FeignInterceptor implements RequestInterceptor {

    @Override
    public void apply(RequestTemplate requestTemplate) {
        requestTemplate.header(SaSameUtil.SAME_TOKEN, SaSameUtil.getToken());
        requestTemplate.header(StpUtil.getTokenName(), StpUtil.getTokenValue());
    }
}
