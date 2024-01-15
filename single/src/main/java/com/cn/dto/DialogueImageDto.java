package com.cn.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.experimental.Accessors;


@Data
@Accessors(chain = true)
public class DialogueImageDto {

    @NotBlank(message = "prompt word cannot be empty")
    private String prompt;

//    @NotBlank(message = "size cannot be empty")
//    private String size;


}
