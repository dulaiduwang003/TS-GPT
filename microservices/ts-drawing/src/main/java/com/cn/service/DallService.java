package com.cn.service;

import com.cn.dto.DallTaskDto;
import com.cn.dto.DialogueImageDto;
import com.cn.vo.DialogueImageVo;

/**
 * The interface Dall service.
 */
public interface DallService {


    /**
     * Dialog generation img dialogue image vo.
     *
     * @param dto the dto
     * @return the dialogue image vo
     */
    DialogueImageVo dialogGenerationImg(final DialogueImageDto dto);


    /**
     * Add dall task string.
     *
     * @param dto the dto
     * @return the string
     */
    String addDallTask(final DallTaskDto dto);
}
