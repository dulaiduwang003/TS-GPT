package com.cn.app.superbot.filter;

import cn.dev33.satoken.context.SaHolder;
import cn.dev33.satoken.exception.NotLoginException;
import cn.dev33.satoken.filter.SaServletFilter;
import cn.dev33.satoken.router.SaRouter;
import cn.dev33.satoken.stp.StpUtil;
import com.cn.app.superbot.constants.AuthConstants;
import com.cn.app.superbot.constants.RoleConstants;
import com.cn.app.superbot.msg.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * The type  configure.
 *
 * @author bdth
 * @email 2074055628 @qq.om
 */
@Configuration
@Slf4j
public class RoutePathFilter {


    /**
     * Gets sa servlet filter.
     *
     * @return the sa servlet filter
     */
    @Bean
    public SaServletFilter getSaServletFilter() {
        return new SaServletFilter()
                .addInclude("/**").addExclude("/favicon.ico")
                .setAuth(obj -> {
                    SaRouter
                            .match("/**")
                            .notMatch("/auth/**", "/upload/**")
                            .check(r -> StpUtil.checkLogin());
                    SaRouter.match("/v1/**", r -> StpUtil.checkRole(RoleConstants.USER));
                    SaRouter.match("/user/**", r -> StpUtil.checkRole(RoleConstants.USER));
                    SaRouter.match("/server/**", r -> StpUtil.checkRole(RoleConstants.ADMIN));
                })
                .setError(e -> {

                    if (e instanceof NotLoginException) {
                        return Result.error(AuthConstants.NOT_LOGGED_IN, 401);
                    }
                    log.error("user authentication failed");
                    return Result.error(AuthConstants.LOGIN_FAILED);


                })
                .setBeforeAuth(r -> {
                    SaHolder.getResponse()
                            .setServer("sa-server")
                            .setHeader("X-Frame-Options", "SAMEORIGIN")
                            .setHeader("X-XSS-Protection", "1; mode=block")
                            .setHeader("X-Content-Type-Options", "nosniff");
                });
    }
}
