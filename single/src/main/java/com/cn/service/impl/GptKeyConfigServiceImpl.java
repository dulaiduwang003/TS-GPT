package com.cn.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cn.dto.AddGptKeyDto;
import com.cn.dto.DeleteGptKeyDto;
import com.cn.dto.UpdateGptKeyDto;
import com.cn.entity.TsGptKey;
import com.cn.mapper.TsGptKeyMapper;
import com.cn.service.GptKeyConfigService;
import com.cn.utils.StringUtils;
import com.cn.vo.GptKeyPageVo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * gpt密钥 业务
 *
 * @author 时间海 @github dulaiduwang003
 * @version 1.0
 */
@Service
@RequiredArgsConstructor
public class GptKeyConfigServiceImpl implements GptKeyConfigService {


    private final TsGptKeyMapper tsGptKeyMapper;


    @Override
    public IPage<GptKeyPageVo> getGptKeyPage(final int pageNum, final String prompt) {
        return tsGptKeyMapper.selectPage(new Page<>(pageNum, 15), new QueryWrapper<TsGptKey>()
                .lambda()
                .eq(StringUtils.notEmpty(prompt), TsGptKey::getOpenAiKey, prompt)
        ).convert(t -> new GptKeyPageVo()
                .setGptKeyId(t.getGptKeyId())
                .setOpenAiKey(t.getOpenAiKey())
                .setCreatedTime(t.getCreatedTime())
                .setUpdateTime(t.getUpdateTime())
                .setIsSeniorModel(t.getIsSeniorModel() == 1));
    }

    @Override
    public void updateGptKey(final UpdateGptKeyDto dto) {
        tsGptKeyMapper.updateById(new TsGptKey()
                .setGptKeyId(dto.getGptKeyId())
                .setIsSeniorModel(dto.getIsSeniorModel() ? 1 : 0)
                .setOpenAiKey(dto.getOpenAiKey())
        );
    }

    @Override
    public void deleteGptKey(final DeleteGptKeyDto dto) {
        tsGptKeyMapper.delete(new QueryWrapper<TsGptKey>()
                .lambda()
                .eq(TsGptKey::getGptKeyId, dto.getGptKeyId())
        );
    }

    @Override
    public void addGptKey(final AddGptKeyDto dto) {
        tsGptKeyMapper.insert(new TsGptKey()
                .setOpenAiKey(dto.getOpenAiKey())
                .setIsSeniorModel(dto.getIsSeniorModel() ? 1 : 0)
        );
    }

}
