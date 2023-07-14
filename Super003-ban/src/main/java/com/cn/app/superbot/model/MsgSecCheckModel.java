
package com.cn.app.superbot.model;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * The type Msg sec check model.
 *
 * @author bdth
 * @email 2074055628 @qq.om
 */
@Data
@Accessors(chain = true)
public class MsgSecCheckModel {

    private String content;


    private Integer version = 2;

    private Integer scene = 2;

    private String openid;
}
