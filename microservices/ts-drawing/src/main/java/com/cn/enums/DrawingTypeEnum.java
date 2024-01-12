package com.cn.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 绘图类型
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
public enum DrawingTypeEnum {


    DALL("DALL"),

    SD("SD");


    /**
     * The Dec.
     */
    String dec;


}
