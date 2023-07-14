package com.cn.app.superbot.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


/**
 * The type We chat login dto.
 *
 * @author bdth
 * @email 2074055628 @qq.om
 */
@Getter
@Setter
@ToString
public class WeChatLoginDto {


    /**
     * The Code.
     *
     * @author bdth
     * @email 2074055628 @qq.om
     */
    @NotBlank(message = "CODE不能为空")
    private String code;

}