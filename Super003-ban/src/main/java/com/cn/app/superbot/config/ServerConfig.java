package com.cn.app.superbot.config;

import lombok.Data;
import lombok.ToString;


/**
 * The type Server config.
 *
 * @author bdth
 * @email 2074055628 @qq.om
 */
@Data
public class ServerConfig {
    /**
     * 1 overseas  1 domestic 3 thirdParty
     */
    public Integer choose;

    /**
     * The Proxy.
     */
    public Proxy proxy;

    /**
     * The Chat gpt 4.
     */
    public ChatGpt4 chatGpt4;

    /**
     * The Third party.
     */
    public ThirdParty thirdParty;


    /**
     * The Cookie.
     */
    public String cookie;


    /**
     * The Key.
     */
    public String key;


    /**
     * The Sd url.
     */
    public String sdUrl;

    /**
     * The type Proxy.
     */
    @Data
    @ToString
    public static class Proxy {

        /**
         * The Ip.
         */
        public String ip;

        /**
         * The Port.
         */
        public Integer port;


    }

    /**
     * The type Third party.
     */
    public static class ThirdParty {

        /**
         * The Base url.
         */
        public String baseUrl;

        /**
         * The Token.
         */
        public String token;


    }

    /**
     * The type Chat gpt 4.
     *
     * @author bdth
     * @email 2074055628 @qq.om
     */
    @Data
    public static class ChatGpt4 {

        /**
         * The Base url.
         */
        public String baseUrl;

        /**
         * The Token.
         */
        private String key;

    }

}
