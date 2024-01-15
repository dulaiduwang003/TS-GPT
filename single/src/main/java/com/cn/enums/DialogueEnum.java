package com.cn.enums;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * 环境枚举(影响GPT输出行为)
 *
 * @author 时间海 @github dulaiduwang003
 * @version 1.0
 */
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public enum DialogueEnum {

    //图片识别
    RECOGNITION("RECOGNITION"),
    //对话
    DIALOGUE("DIALOGUE");
//    //文章生成
//    WRITING("WRITING");

    String dec;


    /**
     * 转为 枚举类型
     *
     * @param value the value
     * @return the dialogue enum
     */
    public static DialogueEnum fromString(String value) {
        for (DialogueEnum env : DialogueEnum.values()) {
            if (env.getDec().equals(value)) {
                return env;
            }
        }
        throw new IllegalArgumentException("Invalid value for EnvEnum: " + value);
    }
}
