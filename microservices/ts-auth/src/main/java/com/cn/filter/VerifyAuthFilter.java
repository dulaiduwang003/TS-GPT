package com.cn.filter;


import cn.dev33.satoken.context.SaHolder;
import cn.dev33.satoken.filter.SaServletFilter;
import cn.dev33.satoken.same.SaSameUtil;
import cn.dev33.satoken.util.SaResult;
import com.cn.exceptions.ExceptionMessages;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.time.LocalDateTime;

/**
 * 安全过滤器 防止不通过网关调用子服务
 *
 * @author 欧渐风 @github dulaiduwang003
 * @version 1.0
 */
@Configuration
@Slf4j
public class VerifyAuthFilter implements WebMvcConfigurer {


    @Bean
    public SaServletFilter getSaServletFilter() {
        return new SaServletFilter()
                .addInclude("/**")
                .addExclude("/favicon.ico")
                .setAuth(obj -> {
                    String token = SaHolder.getRequest().getHeader(SaSameUtil.SAME_TOKEN);
                    SaSameUtil.checkToken(token);
                })
                .setError(e -> {
                    log.warn("有非法请求试图通过访问子服务操作API(已被拦截) 时间: {}", LocalDateTime.now());
                    return SaResult.error(ExceptionMessages.UNAUTHORIZED_ACCESS);
                });
    }

}
