
package com.cn.app.chatgptbot.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * The type Open config.
 *
 * @author bdth
 * @email 2074055628@qq.com
 */
@Configuration
@ConfigurationProperties(prefix = "open")
@Data
public class OpenConfig {

    /**
     * key pool
     */
    private List<String> keys;

    /**
     * choose key
     */
    private Boolean choose;

    /**
     * other key
     */
    private String thirdPartyKey;

    /**
     * whether it is stream or not
     */
    private boolean isStream;

    /**
     * whether it is random or not
     */
    private boolean isRandom;

}
