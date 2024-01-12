package com.cn.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * 文件夹名
 *
 * @author 时间海 @github dulaiduwang003
 * @version 1.0
 */
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public enum FileEnum {

    //头像
    AVATAR("avatar"),
    //绘图
    DRAWING("drawing");

    String dec;

}
