package com.cn.app.superbot.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * The type Card vo.
 */
@Data
public class CodeVo implements Serializable {

    /**
     * The Code.
     */
    private String code;

    /**
     * The Frequency.
     */
    private String frequency;
}
