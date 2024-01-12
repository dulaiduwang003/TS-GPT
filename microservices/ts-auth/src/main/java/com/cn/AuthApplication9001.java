package com.cn;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * 登录 订单 业务处理
 *
 * @author 明明不是下雨天 @github dulaiduwang003
 * @version 1.0
 */
@EnableDiscoveryClient
@SpringBootApplication
@EnableFeignClients("com.cn.feign")
public class AuthApplication9001 {

    public static void main(String[] args) {

        SpringApplication.run(AuthApplication9001.class, args);
    }
}
