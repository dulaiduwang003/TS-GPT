package com.cn.filter;

import cn.dev33.satoken.same.SaSameUtil;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * The type Forward auth filter.
 */
@Component
public class ForwardAuthFilter implements GlobalFilter {

    /**
     * Filter mono.
     *
     * @param exchange the exchange
     * @param chain    the chain
     * @return the mono
     */
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest newRequest = exchange
                .getRequest()
                .mutate()
                .header(SaSameUtil.SAME_TOKEN, SaSameUtil.getToken())
                .build();
        return chain.filter(exchange.mutate().request(newRequest).build());
    }
}
