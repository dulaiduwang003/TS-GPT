package com.cn.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 对话参数 DTO
 *
 * @author 时间海 @github dulaiduwang003
 * @version 1.0
 */
@Data
@Accessors(chain = true)
public class DialogueParameterDto implements Serializable {
    //数据源
    private String messages;

    //对话类型
    private String type;

    //额外参数
    private String extra;


    /**
     * 额外携带参数 如模型坐标 是否启用文字过滤
     */
    @Data
    @Accessors(chain = true)
    public static class Extra {

        private Integer modelIndex;

        private Boolean isFiltration;

    }
}
