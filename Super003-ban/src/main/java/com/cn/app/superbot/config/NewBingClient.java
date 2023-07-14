package com.cn.app.superbot.config;

import jakarta.websocket.ClientEndpoint;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;

import java.util.concurrent.CountDownLatch;

/**
 * The type New bing client.
 *
 * @author bdth
 * @email 2074055628 @qq.om
 */
@ClientEndpoint
@SuppressWarnings("all")
public class NewBingClient {

    /**
     * The Latch.
     */
    private CountDownLatch latch;
    /**
     * The Session.
     */
    private Session session;

    /**
     * On web socket connect.
     *
     * @param session the session
     */
    @OnOpen
    public void onWebSocketConnect(Session session) {
        this.session = session;
        latch.countDown();
    }

    /**
     * On web socket message.
     *
     * @param message the message
     */
    @OnMessage
    public void onWebSocketMessage(String message) {

    }


    /**
     * Send message.
     *
     * @param message the message
     * @throws Exception the exception
     */
    public void sendMessage(String message) throws Exception {
        if (session != null && session.isOpen()) {
            session.getBasicRemote().sendText(message);
        }
    }

    /**
     * Close.
     *
     * @throws Exception the exception
     */
    public void close() throws Exception {
        if (session != null && session.isOpen()) {
            session.close();
        }
    }
}
