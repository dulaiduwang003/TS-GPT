
package com.cn.app.chatgptbot.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * The type Gpt key dto.
 *
 * @author bdth
 * @email 2074055628@qq.com
 */
@Getter
@Setter
@ToString
public final class GptKeyDto {

    /**
     * bulkOperations Kye
     */
    @NotEmpty(message = "key不能为空")
    private List<String> keys;


}
