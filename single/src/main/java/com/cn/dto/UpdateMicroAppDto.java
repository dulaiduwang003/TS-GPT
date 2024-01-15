package com.cn.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 修改指定预设此 DTO
 *
 * @author 时间海 @github dulaiduwang003
 * @version 1.0
 */
@Data
@Accessors(chain = true)
public class UpdateMicroAppDto implements Serializable {

    @NotNull(message = "修改标识不能为空")
    private Long microAppId;

    @NotBlank(message = "emoji图标不能为空")
    private String icon;

    @NotBlank(message = "标题不能为空")
    private String title;

    @NotBlank(message = "提示词介绍不能为空")
    private String introduce;

    @NotBlank(message = "中文提问模板不能为空")
    private String chineseIssue;

    @NotBlank(message = "英文提示模板不能为空")
    private String englishIssue;

    @NotBlank(message = "中文回答模板不能为空")
    private String chineseAnswer;

    @NotBlank(message = "英文回答模板不能为空")
    private String englishAnswer;

    @NotNull(message = "所属类别不能为空")
    private Long microCategoryId;

}
