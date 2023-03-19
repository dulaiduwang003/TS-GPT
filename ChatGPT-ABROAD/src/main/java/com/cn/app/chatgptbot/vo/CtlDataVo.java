
package com.cn.app.chatgptbot.vo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * The type Ctl data vo.
 *
 * @author bdth
 * @email 2074055628@qq.com
 */
@Accessors
@Data
public class CtlDataVo implements Serializable {

    /**
     * availableKeys
     */
    private Collection<String> availableKeys;

    /**
     * lapseKeys
     */
    private Collection<String> lapseKeys;

    /**
     * thirdPartyKey
     */
    private String thirdPartyKey;

    /**
     * use key
     */
    private String mainKey;

    /**
     * choose
     */
    private Boolean choose;
}
