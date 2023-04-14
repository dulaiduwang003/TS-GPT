package com.cn.app.superbot.dto;

import com.cn.app.superbot.config.ServerConfig;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * The type Put server config dto.
 *
 * @author bdth
 * @email 2074055628 @qq.om
 */
@Getter
@Setter
@ToString
public class PutServerConfigDto {

    /**
     * The Choose.
     */
    public Integer choose;

    /**
     * The Proxy.
     */
    public ServerConfig.Proxy proxy;

    /**
     * The Third party.
     */

    public ServerConfig.ThirdParty thirdParty;


    /**
     * The Chat gpt 4.
     */
    public ServerConfig.ChatGpt4 chatGpt4;


    /**
     * The Sd url.
     */
    public String sdUrl;


    /**
     * The Cookie.
     */
    public String cookie;


    /**
     * The Key.
     */
    public String key;

    /**
     * Convert to server config.
     *
     * @param item the item
     * @return the server config
     */
    public static ServerConfig convertToServerConfig(PutServerConfigDto item) {
        if (item == null) {
            return null;
        }
        ServerConfig result = new ServerConfig();
        result.setChoose(item.getChoose());
        result.setProxy(item.getProxy());
        result.setChatGpt4(item.getChatGpt4());
        result.setThirdParty(item.getThirdParty());
        result.setCookie(item.getCookie());
        result.setKey(item.getKey());
        result.setSdUrl(item.getSdUrl());
        return result;
    }
}
