package com.cn.service;

import com.cn.vo.ModelVo;

import java.util.List;

/**
 * 模型业务
 *
 * @author 时间海 @github dulaiduwang003
 * @version 1.0
 */
public interface ModelService {


    /**
     * 获取所有模型列表
     *
     * @return the list of models
     */
    List<ModelVo> getModelList();


}
