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
public enum EnvEnum {

    WECHAT("wechat"),

    WEB("web");

    String dec;


    public static EnvEnum fromString(String value) {
        for (EnvEnum env : EnvEnum.values()) {
            if (env.getDec().equals(value)) {
                return env;
            }
        }
        throw new IllegalArgumentException("Invalid value for EnvEnum: " + value);
    }
}
