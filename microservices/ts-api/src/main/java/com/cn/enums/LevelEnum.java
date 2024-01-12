package com.cn.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * 异常级别枚举
 *
 * @author bdth @github dulaiduwang003
 * @version 1.0
 */
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public enum LevelEnum {

    LOW("LOW"), //低级
    MIDDLE("MIDDLE"), //中级
    HEIGHT("HEIGHT"); //高级

    String desc;
}
