package com.cn.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cn.dto.AddGptModelDto;
import com.cn.dto.DeleteGptModelDto;
import com.cn.dto.UpdateGptModelDto;
import com.cn.entity.TsGptModel;
import com.cn.mapper.TsGptModelMapper;
import com.cn.service.GptModelConfigService;
import com.cn.utils.StringUtils;
import com.cn.vo.GptModelPageVo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * gpt模型 业务
 *
 * @author 时间海 @github dulaiduwang003
 * @version 1.0
 */
@Service
@RequiredArgsConstructor
public class GptModelConfigServiceImpl implements GptModelConfigService {

    private final TsGptModelMapper tsGptModelMapper;

    @Override
    public IPage<GptModelPageVo> getGptModelPage(final int pageNum, final String prompt) {
        return tsGptModelMapper.selectPage(new Page<>(pageNum, 15), new QueryWrapper<TsGptModel>()
                .lambda()
                .eq(StringUtils.notEmpty(prompt), TsGptModel::getModelName, prompt)
        ).convert(s -> new GptModelPageVo()
                .setIsSeniorModel(s.getIsSeniorModel() == 1)
                .setGptModelId(s.getGptModelId())
                .setModelName(s.getModelName())
                .setTemperature(s.getTemperature())
                .setMaxTokens(s.getMaxTokens())
                .setTopP(s.getTopP())
                .setUpdateTime(s.getUpdateTime())
                .setCreatedTime(s.getCreatedTime()));

    }

    @Override
    public void addGptModel(final AddGptModelDto dto) {
        tsGptModelMapper.insert(new TsGptModel()
                .setModelName(dto.getModelName())
                .setMaxTokens(dto.getMaxTokens())
                .setTopP(dto.getTopP())
                .setTemperature(dto.getTemperature())
                .setIsSeniorModel(dto.getIsSeniorModel() ? 1 : 0)
        );
    }

    @Override
    public void updateGptModel(final UpdateGptModelDto dto) {
        tsGptModelMapper.updateById(new TsGptModel()
                .setModelName(dto.getModelName())
                .setTopP(dto.getTopP())
                .setTemperature(dto.getTemperature())
                .setIsSeniorModel(dto.getIsSeniorModel() ? 1 : 0)
                .setGptModelId(dto.getGptModelId())
        );
    }

    @Override
    public void deleteGptModel(final DeleteGptModelDto dto) {
        tsGptModelMapper.delete(new QueryWrapper<TsGptModel>()
                .lambda()
                .eq(TsGptModel::getGptModelId, dto.getGptModelId())
        );
    }


}
