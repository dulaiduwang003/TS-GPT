package com.cn;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * 网关 统一鉴权
 *
 * @author 时间海 @github dulaiduwang003
 * @version 1.0
 */
@SpringBootApplication
@EnableDiscoveryClient
public class GateWayApplication9000 {

    /**
     * The entry point of application.
     *
     * @param args the input arguments
     */
    public static void main(String[] args) {
        SpringApplication.run(GateWayApplication9000.class, args);
    }
}
