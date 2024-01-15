package com.cn.net;

import cn.dev33.satoken.stp.StpUtil;
import com.alibaba.fastjson.JSONObject;
import com.cn.constant.ChatStatusConstant;
import com.cn.dto.DialogueParameterDto;
import com.cn.enums.DialogueEnum;
import com.cn.exception.CloseException;
import com.cn.exceptions.MemberException;
import com.cn.service.impl.ChatGptServiceFlux;
import com.cn.utils.ChatGptUtil;
import com.cn.utils.SpringContextUtil;
import com.cn.utils.UserUtils;
import jakarta.websocket.*;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * GPT流对话
 *
 * @author 时间海 @github dulaiduwang003
 * @version 1.0
 */
@Slf4j
@ServerEndpoint(value = "/gpt/{token}", subprotocols = {"protocol"})
@SuppressWarnings("all")
@Service
public class ChatGptNet {

    private static ChatGptServiceFlux chatGptServiceFlux;
    private static ChatGptUtil chatGptUtil;
    private Session session;

    private int maxSize = 10 * 1024 * 1024;

    @OnOpen
    public void onOpen(final Session session, @PathParam("token") final String token) {
        //设置传输缓存流
        if (session.getMaxTextMessageBufferSize() <= 8192) {
            session.setMaxBinaryMessageBufferSize(maxSize);
            session.setMaxTextMessageBufferSize(maxSize);
        }
        try {
            assert session.getId() != null;
            //校验用户
            assert StpUtil.getLoginIdByToken(token) != null;
        } catch (Exception e) {
            log.warn("无法获取建立连接数据,已拒绝连接");
            return;
        }
        this.session = session;
        //手动注入
        if (chatGptServiceFlux == null) {
            chatGptServiceFlux = (ChatGptServiceFlux) SpringContextUtil.getBean("chatGptServiceFlux");
            chatGptUtil = (ChatGptUtil) SpringContextUtil.getBean("chatGptUtil");
        }
    }

    /**
     * 建立数据连接
     *
     * @param message the message
     * @param token   the token
     * @param model   the model
     * @param env     the env
     */
    @OnMessage
    public void onMessage(final String parameter, @PathParam("token") final String token) {
        //转化请求
        final DialogueParameterDto dto = JSONObject.parseObject(parameter, DialogueParameterDto.class);
        try {
            //校验用户会员身份是否过期
            UserUtils.membershipHasExpiredCharacter(UserUtils.getLoginIdByToken(token));
            //建立流数据
            final ChatGptServiceFlux.FluxOutcome action = chatGptServiceFlux.action(dto, DialogueEnum.fromString(dto.getType()));
            //返回流数据
            action.getFlux().doFinally(signal -> handleWebSocketCompletion())
                    .subscribe(data -> {
                        final String dataed = chatGptUtil.dataParsing(data);
                        try {
                            this.session.getBasicRemote().sendText(dataed);
                        } catch (Exception e) {
                            //用户可能手动端口连接
                            throw new CloseException();
                        }
                    }, throwable -> {
                        //为 Close异常时 过滤
                        if (!(throwable instanceof CloseException)) {
                            sendErrorMessages(ChatStatusConstant.ERROR);
                        }
                    });
        } catch (MemberException e) {
            //会员国企
            sendErrorMessages(e.getMessage());
        } catch (Exception e) {
            sendErrorMessages(ChatStatusConstant.ERROR);
        }

    }

    /**
     * \
     * 发送错误消息
     *
     * @param msg
     */
    private void sendErrorMessages(final String msg) {
        try {
            this.session.getBasicRemote().sendText(msg);
            handleWebSocketCompletion();
        } catch (Exception e) {

        }
    }

    @OnClose
    public void handleWebSocketCompletion() {
        try {
            this.session.close();
        } catch (IOException e) {
            log.error("关闭 WebSocket 会话失败.", e);
        }
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        log.warn("GPT websocket出现异常 原因:{}", throwable.getMessage());
        //打印堆栈
        //      throwable.printStackTrace();
    }
}
