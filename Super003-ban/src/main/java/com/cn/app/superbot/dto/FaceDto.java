
package com.cn.app.superbot.dto;


import com.cn.app.superbot.model.FaceModel;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;


/**
 * The type Face dto.
 *
 * @author bdth
 * @email 2074055628 @qq.om
 */
@Data
public class FaceDto {

    @NotBlank(message = "消息数据不能为空")
    private String prompt;


    /**
     * Convert to face model.
     *
     * @param item the item
     * @return the face model
     */
    public static FaceModel convertToFaceModel(FaceDto item) {
        if (item == null) {
            return null;
        }
        FaceModel result = new FaceModel();
        result.setPrompt(item.getPrompt());
        return result;
    }
}
