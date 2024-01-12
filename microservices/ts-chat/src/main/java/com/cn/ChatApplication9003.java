package com.cn;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * 对话服务
 *
 * @author 时间海 @github dulaiduwang003
 * @version 1.0
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients("com.cn.feign")
public class ChatApplication9003 {

    public static void main(String[] args) {
        SpringApplication.run(ChatApplication9003.class, args);
    }

}
