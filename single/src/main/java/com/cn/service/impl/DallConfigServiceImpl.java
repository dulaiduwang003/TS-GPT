package com.cn.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cn.common.DallCommon;
import com.cn.constant.ChatGptConstant;
import com.cn.dto.AddDallKeyDto;
import com.cn.dto.DeleteDallKeyDto;
import com.cn.dto.SetDallExtraDto;
import com.cn.dto.UpdateDallKeyDto;
import com.cn.entity.TsDallKey;
import com.cn.mapper.TsDallKeyMapper;
import com.cn.service.DallConfigService;
import com.cn.utils.RedisUtils;
import com.cn.utils.StringUtils;
import com.cn.vo.DallExtraVo;
import com.cn.vo.DallKeyPageVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * The type Dall config service.
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class DallConfigServiceImpl implements DallConfigService {

    /**
     * The Ts dall key mapper.
     */
    private final TsDallKeyMapper tsDallKeyMapper;

    /**
     * The Redis utils.
     */
    private final RedisUtils redisUtils;


    @Override
    public DallExtraVo getDallExtraConfig() {

        final String requestUrl;
        final Object value = redisUtils.getValue(ChatGptConstant.DALL_REQUEST_API);
        if (value != null) {
            requestUrl = String.valueOf(value);
        } else requestUrl = null;
        return new DallExtraVo()
                .setRequestUrl(requestUrl);
    }

    @Override
    public void setDallExtraConfig(final SetDallExtraDto dto) {
        redisUtils.setValue(ChatGptConstant.DALL_REQUEST_API, dto.getRequestUrl());
    }

    @Override
    public IPage<DallKeyPageVo> getDallKeyPage(int pageNum, final String prompt) {
        return tsDallKeyMapper.selectPage(new Page<>(pageNum, 15), new QueryWrapper<TsDallKey>()
                .lambda()
                .eq(StringUtils.notEmpty(prompt), TsDallKey::getOpenAiKey, pageNum)
        ).convert(s -> new DallKeyPageVo()
                .setDallKeyId(s.getDallKeyId())
                .setOpenAiKey(s.getOpenAiKey())
                .setCreatedTime(s.getCreatedTime())
                .setUpdateTime(s.getUpdateTime()));
    }

    @Override
    public void updateDallKey(UpdateDallKeyDto dto) {
        tsDallKeyMapper.updateById(new TsDallKey()
                .setDallKeyId(dto.getDallKeyId())
                .setOpenAiKey(dto.getOpenAiKey())
        );
    }

    @Override
    public void deleteDallKey(final DeleteDallKeyDto dto) {
        tsDallKeyMapper.delete(new QueryWrapper<TsDallKey>()
                .lambda()
                .eq(TsDallKey::getDallKeyId, dto.getDallKeyId())
        );
    }

    @Override
    public void addDallKey(final AddDallKeyDto dto) {
        tsDallKeyMapper.insert(new TsDallKey().setOpenAiKey(dto.getOpenAiKey()));
    }

    @Override
    public void loadDallStructure() {
        final List<String> keyList = tsDallKeyMapper.selectList(null)
                .parallelStream()
                .map(TsDallKey::getOpenAiKey).toList();
        DallCommon.STRUCTURE
                .setKeyList(keyList);

        final Object value = redisUtils.getValue(ChatGptConstant.DALL_REQUEST_API);
        if (value != null) {
            DallCommon.STRUCTURE.setRequestUrl(String.valueOf(value));
        }
    }
}
