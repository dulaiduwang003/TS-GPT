package com.cn.app.superbot.model;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * The type Resource model.
 *
 * @author bdth
 * @email 2074055628 @qq.om
 */
@Data
@Accessors(chain = true)
public class ResourceModel {

    /**
     * The Source.
     */
    private String source;

    /**
     * The Target.
     */
    private String target;
}
