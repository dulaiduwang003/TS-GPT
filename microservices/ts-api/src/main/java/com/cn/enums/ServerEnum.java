package com.cn.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;


/**
 * 服务名称枚举
 *
 * @author bdth @github dulaiduwang003
 * @version 1.0
 */
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public enum ServerEnum {


    AUTH("AUTH"),//鉴权模块
    CHAT("CHAT"),//聊天模块
    DRAWING("DRAWING");//绘图模块

    String desc;
}
