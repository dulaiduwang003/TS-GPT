package com.cn.app.superbot.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * The type Exchange code.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName(value = "exchange_code")
@Accessors(chain = true)
public class ExchangeCode {

    /**
     * The Exchange code id.
     */
    @TableId(type = IdType.AUTO)
    private Long exchangeCodeId;
    /**
     * The Code.
     */
    private String code;
    /**
     * The Frequency.
     */
    private Long frequency;
    /**
     * The Del.
     */
    private Long del;


}
