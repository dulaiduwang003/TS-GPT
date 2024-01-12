package com.cn.filter;

import cn.dev33.satoken.context.SaHolder;
import cn.dev33.satoken.reactor.filter.SaReactorFilter;
import cn.dev33.satoken.router.SaRouter;
import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.util.SaResult;
import com.cn.enums.RoleEnum;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;

/**
 * The type Interceptor configure.
 */
@Configuration
public class InterceptorConfigure {
    /**
     * Gets sa reactor filter.
     *
     * @return the sa reactor filter
     */
// 注册 Sa-Token全局过滤器
    @Bean
    public SaReactorFilter getSaReactorFilter() {
        return new SaReactorFilter()
                .addInclude("/**").addExclude("/favicon.ico")
                .setAuth(obj -> {
                    //拦截所有路由
                    SaRouter.match("/**")
                            //不需要校验的API
                            .notMatch(
                                    //AUTH模块
                                    "/auth-api/auth/**",
                                    "/auth-api/email/**",
                                    "/auth-api/revelation/**",
                                    //CHAT模块
                                    "/chat-api/public/**",
                                    "/chat-api/gpt/**"
                            )
                            .check(r -> StpUtil.checkLogin());
                    //权限校验 AUTH
                    SaRouter.match("/auth-api/product-management/**", r -> StpUtil.checkRole(RoleEnum.ADMIN.getDesc()));
                    SaRouter.match("/auth-api/orders-management/**", r -> StpUtil.checkRole(RoleEnum.ADMIN.getDesc()));
                    SaRouter.match("/auth-api/user-management/**", r -> StpUtil.checkRole(RoleEnum.ADMIN.getDesc()));
                    SaRouter.match("/auth-api/ahi-management/**", r -> StpUtil.checkRole(RoleEnum.ADMIN.getDesc()));
                    //CHAT
                    SaRouter.match("/chat-api/micro-management/**", r -> StpUtil.checkRole(RoleEnum.ADMIN.getDesc()));
                    SaRouter.match("/chat-api/gpt-management/**", r -> StpUtil.checkRole(RoleEnum.ADMIN.getDesc()));
                    //DRAWING
                    SaRouter.match("/drawing-api/dall-management/**", r -> StpUtil.checkRole(RoleEnum.ADMIN.getDesc()));
                    SaRouter.match("/drawing-api/drawing-management/**", r -> StpUtil.checkRole(RoleEnum.ADMIN.getDesc()));

                })
                // 异常处理方法：每次setAuth函数出现异常时进入
                .setError(e -> {
                    return SaResult.get(401, "登录信息已失效", null);
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
