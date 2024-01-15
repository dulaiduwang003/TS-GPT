package com.cn.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.experimental.Accessors;


/**
 * 更新用户昵称DTO
 *
 * @author 时间海 @github dulaiduwang003
 * @version 1.0
 */
@Data
@Accessors(chain = true)
public class UploadUserNickNameDto {

    @NotBlank(message = "用户昵称不能为空")
    private String nickName;

}
