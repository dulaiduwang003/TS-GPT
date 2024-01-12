package com.cn.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.cn.dto.AddDallKeyDto;
import com.cn.dto.DeleteDallKeyDto;
import com.cn.dto.SetDallExtraDto;
import com.cn.dto.UpdateDallKeyDto;
import com.cn.vo.DallExtraVo;
import com.cn.vo.DallKeyPageVo;

/**
 * DALL配置 业务
 */
public interface DallConfigService {


    /**
     * Gets dall extra config.
     *
     * @return the dall extra config
     */
    DallExtraVo getDallExtraConfig();


    /**
     * Sets dall extra config.
     *
     * @param dto the dto
     */
    void setDallExtraConfig(final SetDallExtraDto dto);

    /**
     * Gets dall key page.
     *
     * @param pageNum the page num
     * @param prompt  the prompt
     * @return the dall key page
     */
    IPage<DallKeyPageVo> getDallKeyPage(int pageNum, String prompt);


    /**
     * Update dall key.
     *
     * @param dto the dto
     */
    void updateDallKey(UpdateDallKeyDto dto);


    /**
     * Delete dall key.
     *
     * @param dto the dto
     */
    void deleteDallKey(DeleteDallKeyDto dto);


    /**
     * Add dall key.
     *
     * @param dto the dto
     */
    void addDallKey(AddDallKeyDto dto);


    /**
     * Load dall structure.
     */
    void loadDallStructure();

}
