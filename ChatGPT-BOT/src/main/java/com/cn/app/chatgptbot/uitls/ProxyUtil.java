/**
 * @author 明明不是下雨天
 */
package com.cn.app.chatgptbot.uitls;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Component;
import reactor.netty.http.client.HttpClient;
import reactor.netty.transport.ProxyProvider;

import java.net.*;


/**
 * The type Proxy util.
 *
 * @author bdth
 * @email 2074055628@qq.com
 */
@Slf4j
@Component
public class ProxyUtil {

    /**
     * broker
     */
    @Value(value = "${broker.ip}")
    private String ip;

    /**
     * port
     */
    @Value(value = "${broker.port}")
    private Integer port;



    /**
     * Gets proxy.
     *
     * @return the proxy
     */
    public ReactorClientHttpConnector getProxy() {
        final HttpClient httpClient = HttpClient.create()
                .proxy(proxy -> proxy
                        .type(ProxyProvider.Proxy.HTTP)
                        .address(new InetSocketAddress(ip, port)));

        return new ReactorClientHttpConnector(httpClient);
    }
}
