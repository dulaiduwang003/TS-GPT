package com.cn.supers.ws;

import cn.dev33.satoken.stp.StpUtil;
import com.alibaba.fastjson.JSONObject;
import com.cn.supers.model.ChatGPT$4Model;
import jakarta.websocket.OnClose;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import java.util.concurrent.ConcurrentHashMap;


/**
 * The type Web socket.
 */
@Slf4j
@Component
@ServerEndpoint("/super/api/{token}")
@SuppressWarnings("all")
public class WebSocket {


    /**
     * The Session.
     */
    private Session session;

    /**
     * The Webclient.
     */
    private final WebClient.Builder webclient = WebClient.builder();


    /**
     * The constant webSocketSet.
     */
    private static final ConcurrentHashMap<String, WebSocket> webSocketSet = new ConcurrentHashMap<>();


    /**
     * On open.
     *
     * @param session the session
     */
    @OnOpen
    public void OnOpen(Session session, @PathParam("token") String token) {
        /*
        certification's required here
         */
        this.session = session;
        webSocketSet.put(session.getId(), this);

    }


    /**
     * On close.
     */
    @OnClose
    public void OnClose() {
        webSocketSet.remove(this.session.getId());
        log.info("current number of people：={}", webSocketSet.size());
    }


    /**
     * On message.
     *
     * @param message the message
     */
    @OnMessage
    public void OnMessage(final String messages) {
        final ChatGPT$4Model model = JSONObject.parseObject(messages, ChatGPT$4Model.class);
        Flux<String> chatFlux = webclient.build()
                .post()
                .body(BodyInserters.fromValue(model))
                .retrieve()
                .bodyToFlux(String.class);
        chatFlux.subscribe(data -> {
            AppointSending(session.getId(), data);
        });

    }

    /**
     * Appoint sending.
     *
     * @param id      the id
     * @param message the message
     */
    public void AppointSending(final String id, final String message) {
        try {
            webSocketSet.get(id).session.getBasicRemote().sendText(message);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }
}