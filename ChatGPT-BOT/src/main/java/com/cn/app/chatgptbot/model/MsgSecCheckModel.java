
package com.cn.app.chatgptbot.model;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * The type Msg sec check model.
 *
 * @author bdth
 * @email 2074055628@qq.com
 */
@Data
@Accessors(chain = true)
public class MsgSecCheckModel {

    /**
     * content
     */
    private String content;

    /**
     * version
     */
    private Integer version = 2;

    /**
     * type
     */
    private Integer scene = 2;

    /**
     * openId(weChat)
     */
    private String openid;
}
