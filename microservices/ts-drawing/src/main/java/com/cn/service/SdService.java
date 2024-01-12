package com.cn.service;

import com.cn.dto.SdTaskDto;
import com.cn.vo.SdParamVo;

/**
 * The interface Sd service.
 */
public interface SdService {


    /**
     * Add sd drawing task string.
     *
     * @param dto the dto
     * @return the string
     */
    String addSdDrawingTask(final SdTaskDto dto);


    /**
     * Gets sd param.
     *
     * @return the sd param
     */
    SdParamVo getSdParam();

}
