package com.cn.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.cn.dto.*;
import com.cn.vo.MicroAppVo;
import com.cn.vo.MicroCategoryPageVo;
import com.cn.vo.MicroCategoryVo;
import com.cn.vo.MicroPageVo;

import java.util.List;

/**
 * 预设词业务
 *
 * @author 时间海 @github dulaiduwang003
 * @version 1.0
 */
public interface MicroService {


    /**
     * 分页获取预设词数据
     *
     * @param pageNum the page num
     * @return the micro application list
     */
    IPage<MicroAppVo> getMicroAppPage(final int pageNum);


    /**
     * 搜索预设词数据
     *
     * @param prompt  the prompt
     * @param pageNum the page num
     * @return the list
     */
    IPage<MicroAppVo.Vo> searchMicroApp(final String prompt, final int pageNum);


    /**
     * 获取预设词分页数据
     *
     * @param pageNum the page num
     * @param prompt  the prompt
     * @return the micro page
     */
    IPage<MicroPageVo> getMicroAppPage(final int pageNum, final String prompt);


    /**
     * 获取预设词类目
     *
     * @param pageNum the page num
     * @param prompt  the prompt
     * @return the micro category
     */
    IPage<MicroCategoryPageVo> getMicroCategory(final int pageNum, final String prompt);


    /**
     * 删除预设此类目
     *
     * @param dto the dto
     */
    void deleteMicroCategory(final DeleteMicroCategoryDto dto);


    /**
     * 修改预设词类目
     *
     * @param dto the dto
     */
    void updateMicroCategory(final UpdateMicroCategoryDto dto);


    /**
     * 新增预设词类目
     *
     * @param dto the dto
     */
    void addMicroCategory(AddMicroCategoryDto dto);

    /**
     * 新增预设词
     *
     * @param dto the dto
     */
    void addMicroApp(final AddMicroAppDto dto);


    /**
     * 获取预设词列表
     *
     * @return the micro category list
     */
    List<MicroCategoryVo> getMicroCategoryList();


    /**
     * 修改预设词
     *
     * @param dto the dto
     */
    void updateMicroApp(final UpdateMicroAppDto dto);


    /**
     * 删除预设词
     *
     * @param dto the dto
     */
    void deleteMicroApp(final DeleteMicroAppDto dto);


    /**
     * 使用预设词模板
     *
     * @param dto the dto
     */
    void useMicroApp(final UseMicroTemplateDto dto);


}
