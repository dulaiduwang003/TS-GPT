package com.cn.supers;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.socket.config.annotation.EnableWebSocket;

/**
 * The type Supers application.
 *
 * @author bdth
 * @email 2074055628 @qq.om
 */
@SpringBootApplication
@EnableWebSocket
public class SupersApplication {

    public static void main(String[] args) {
        SpringApplication.run(SupersApplication.class, args);
    }

}
