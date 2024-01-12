package com.cn.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 修改指定预设词类目 DTO
 *
 * @author 时间海 @github dulaiduwang003
 * @version 1.0
 */
@Data
@Accessors(chain = true)
public class UpdateMicroCategoryDto implements Serializable {


    @NotNull(message = "删除标识不能为空")
    private Long microCategoryId;

    @NotBlank(message = "EL图标不能为空")
    private String elIcon;

    @NotBlank(message = "类别名称不能为空")
    private String categoryName;

}
