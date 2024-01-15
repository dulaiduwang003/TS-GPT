package com.cn.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 新增预设词类目 DTO
 *
 * @author 时间海 @github dulaiduwang003
 * @version 1.0
 */
@Data
@Accessors(chain = true)
public class AddMicroCategoryDto implements Serializable {


    @NotBlank(message = "EL图标不能为空")
    private String elIcon;

    @NotBlank(message = "类别名称不能为空")
    private String categoryName;

}
