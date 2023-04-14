package com.cn.app.superbot.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * The type New bing dto.
 *
 * @author bdth
 * @email 2074055628 @qq.om
 */
@Getter
@Setter
@ToString
public class NewBingDto {

    /**
     * The Parameter.
     */
    @NotBlank(message = "内容不能为空")
    private String parameter;
}
