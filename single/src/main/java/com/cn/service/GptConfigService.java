package com.cn.service;

import com.cn.dto.SetGptExtraDto;
import com.cn.vo.GptExtraVo;


/**
 *  gpt配置类业务
 *
 * @author 时间海 @github dulaiduwang003
 * @version 1.0
 */
public interface GptConfigService {

    /**
     * 配置加载至内存
     */
    void loadGptStructure();


    /**
     * 设置gpt额外配置参数
     *
     * @param dto the dto
     */
    void setGptExtraConfig(final SetGptExtraDto dto);


    /**
     * 获取gpt额外参数
     *
     * @return the gpt extra config
     */
    GptExtraVo getGptExtraConfig();
}
