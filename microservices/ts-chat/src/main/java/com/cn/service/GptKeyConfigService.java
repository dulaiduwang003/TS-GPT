package com.cn.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.cn.dto.AddGptKeyDto;
import com.cn.dto.DeleteGptKeyDto;
import com.cn.dto.UpdateGptKeyDto;
import com.cn.vo.GptKeyPageVo;


/**
 * gpt密钥配置业务
 *
 * @author 时间海 @github dulaiduwang003
 * @version 1.0
 */
public interface GptKeyConfigService {


    /**
     * 分页获取gpt密钥
     *
     * @param pageNum the page num
     * @param prompt  the prompt
     * @return the gpt key page
     */
    IPage<GptKeyPageVo> getGptKeyPage(final int pageNum, final String prompt);


    /**
     * 更新某一个gpt密钥
     *
     * @param dto the dto
     */
    void updateGptKey(final UpdateGptKeyDto dto);


    /**
     * 删除gpt密钥
     *
     * @param dto the dto
     */
    void deleteGptKey(final DeleteGptKeyDto dto);


    /**
     * 新增gpt密钥
     *
     * @param dto the dto
     */
    void addGptKey(final AddGptKeyDto dto);


}
