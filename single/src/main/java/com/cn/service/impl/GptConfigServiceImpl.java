package com.cn.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cn.common.ChatGptCommon;
import com.cn.common.structure.ChatGptStructure;
import com.cn.constant.ChatGptConstant;
import com.cn.dto.SetGptExtraDto;
import com.cn.entity.TsGptKey;
import com.cn.mapper.TsGptKeyMapper;
import com.cn.mapper.TsGptModelMapper;
import com.cn.service.GptConfigService;
import com.cn.utils.RedisUtils;
import com.cn.vo.GptExtraVo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * GPT配置类 业务 实现
 *
 * @author 时间海 @github dulaiduwang003
 * @version 1.0
 */
@Service
@RequiredArgsConstructor
public class GptConfigServiceImpl implements GptConfigService {

    private final TsGptKeyMapper tsGptKeyMapper;

    private final TsGptModelMapper tsGptModelMapper;

    private final RedisUtils redisUtils;

    @Override
    public void setGptExtraConfig(final SetGptExtraDto dto) {
        //设置请求地址
        redisUtils.setValue(ChatGptConstant.GPT_REQUEST_API, dto.getRequestUrl());
        //设置图片预览
        redisUtils.setValue(ChatGptConstant.IMAGE_RECOGNITION, dto.getPicturePreview());
    }

    @Override
    public GptExtraVo getGptExtraConfig() {
        final GptExtraVo gptExtraVo = new GptExtraVo();
        //设置请求地址
        final Object req = redisUtils.getValue(ChatGptConstant.GPT_REQUEST_API);

        //设置图片预览
        final Object value = redisUtils.getValue(ChatGptConstant.IMAGE_RECOGNITION);
        if (req == null || value == null) {
            return null;
        }

        return gptExtraVo.setRequestUrl(String.valueOf(req)).setPicturePreview((ChatGptStructure.ImageRecognitionConfig) value);
    }

    @Override
    public void loadGptStructure() {
        final List<ChatGptStructure.Model> modelList = tsGptModelMapper.selectList(null)
                .parallelStream()
                .map(s -> new ChatGptStructure.Model()
                        .setTop_p(s.getTopP())
                        .setTemperature(s.getTemperature())
                        .setMax_tokens(s.getMaxTokens())
                        .setIsSeniorModel(s.getIsSeniorModel() == 1)
                        .setModelName(s.getModelName())).toList();
        ChatGptCommon.STRUCTURE
                .setSeniorKeyList(getGptKey(1))
                .setLowLevelKeyList(getGptKey(0))
                //模型列表
                .setModelList(modelList);
        //获取API地址
        final Object value = redisUtils.getValue(ChatGptConstant.GPT_REQUEST_API);
        if (value != null) {
            ChatGptCommon.STRUCTURE.setRequestUrl(String.valueOf(value));
        } else {
            //填充一个默认配置
            ChatGptCommon.STRUCTURE.setRequestUrl("");
        }
        //添加图片识别配置
        final Object bean = redisUtils.getValue(ChatGptConstant.IMAGE_RECOGNITION);
        if (bean == null) {
            //填充一个默认配置
            ChatGptCommon.STRUCTURE.setImageRecognitionConfig(
                    new ChatGptStructure
                            .ImageRecognitionConfig()
                            .setDetail("low")
                            .setMax_tokens(300)
                            .setModel("gpt-4-vision-preview")
            );
        } else {
            ChatGptCommon.STRUCTURE.setImageRecognitionConfig((ChatGptStructure.ImageRecognitionConfig) bean);
        }
    }

    private List<String> getGptKey(final Integer isSeniorKey) {
        return tsGptKeyMapper.selectList(new QueryWrapper<TsGptKey>()
                .lambda()
                .eq(TsGptKey::getIsSeniorModel, isSeniorKey)
                .select(TsGptKey::getOpenAiKey)
        ).parallelStream().map(TsGptKey::getOpenAiKey).toList();
    }
}
