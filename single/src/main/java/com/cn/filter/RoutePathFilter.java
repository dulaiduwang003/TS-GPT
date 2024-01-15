package com.cn.filter;

import cn.dev33.satoken.context.SaHolder;
import cn.dev33.satoken.filter.SaServletFilter;
import cn.dev33.satoken.router.SaRouter;
import cn.dev33.satoken.stp.StpUtil;
import com.cn.enums.RoleEnum;
import com.cn.msg.Result;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;

/**
 * 路由拦截器
 *
 * @author 时间海 @github dulaiduwang003
 * @version 1.0
 */
@Configuration
@SuppressWarnings("all")
public class RoutePathFilter {


    @Bean
    public SaServletFilter getSaServletFilter() {
        return new SaServletFilter()
                .addInclude("/**").addExclude("/favicon.ico")
                .setAuth(obj -> {
                    SaRouter.match("/**")
                            .notMatch(
                                    //AUTH模块
                                    "/auth/**",
                                    "/email/**",
                                    "/revelation/**",
                                    //CHAT模块
                                    "/public/**",
                                    "/gpt/**"
                            )
                            .check(r -> StpUtil.checkLogin());
                    //权限校验 AUTH
                    SaRouter.match("/product-management/**", r -> StpUtil.checkRole(RoleEnum.ADMIN.getDesc()));
                    SaRouter.match("/orders-management/**", r -> StpUtil.checkRole(RoleEnum.ADMIN.getDesc()));
                    SaRouter.match("/user-management/**", r -> StpUtil.checkRole(RoleEnum.ADMIN.getDesc()));
                    SaRouter.match("/ahi-management/**", r -> StpUtil.checkRole(RoleEnum.ADMIN.getDesc()));
                    //CHAT
                    SaRouter.match("/micro-management/**", r -> StpUtil.checkRole(RoleEnum.ADMIN.getDesc()));
                    SaRouter.match("/gpt-management/**", r -> StpUtil.checkRole(RoleEnum.ADMIN.getDesc()));
                    //DRAWING
                    SaRouter.match("dall-management/**", r -> StpUtil.checkRole(RoleEnum.ADMIN.getDesc()));
                    SaRouter.match("/drawing-management/**", r -> StpUtil.checkRole(RoleEnum.ADMIN.getDesc()));
                })
                .setError(e -> {
                    return Result.error("登录信息已失效", 401);
                })
                .setBeforeAuth(r -> {
                    SaHolder.getResponse()
                            .setHeader("Access-Control-Allow-Origin", "*")
                            .setHeader("Access-Control-Allow-Methods", "*")
                            .setHeader("Access-Control-Max-Age", "3600")
                            .setHeader("Access-Control-Allow-Headers", "*")
                            .setServer("Zeus");
                    if (SaHolder.getRequest().getMethod().equals(HttpMethod.OPTIONS.toString())) {
                        SaRouter.back();
                    }
                });
    }
}
