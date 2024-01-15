package com.cn.common;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cn.common.structure.ChatGptStructure;
import com.cn.constant.ChatGptConstant;
import com.cn.entity.TsException;
import com.cn.entity.TsGptKey;

import com.cn.enums.LevelEnum;
import com.cn.enums.ServerEnum;
import com.cn.mapper.TsExceptionMapper;
import com.cn.mapper.TsGptKeyMapper;
import com.cn.mapper.TsGptModelMapper;
import com.cn.utils.RedisUtils;
import jakarta.annotation.PostConstruct;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * gpt组件工具类
 *
 * @author 时间海 @github dulaiduwang003
 * @version 1.0
 */
@Component
@RequiredArgsConstructor

public class ChatGptCommon {

    private final RedisUtils redisUtils;

    private final TsGptKeyMapper tsGptKeyMapper;

    private final TsGptModelMapper tsGptModelMapper;

    private final TsExceptionMapper tsExceptionMapper;

    public static final ChatGptStructure STRUCTURE = new ChatGptStructure();
    private static final AtomicInteger counter = new AtomicInteger(0);

    /**
     * 随机获取一个key
     *
     * @param modelIndex 模型坐标
     * @param isSenior   为true则指定获取增强KEY 为false时 根据模型来指定获取
     * @return the model obj
     */
    public static ModelObj pollGetKey(final Integer modelIndex, final boolean isSenior) {
        ChatGptStructure.Model model = null;
        Boolean isSeniorModel = isSenior;
        if (modelIndex != null) {
            final List<ChatGptStructure.Model> modelList = STRUCTURE.getModelList();
            //获取具体模型
            model = modelList.get(modelIndex);
            if (model == null) {
                model = modelList.get(0);
            }
            //是否为增强模型
            isSeniorModel = model.getIsSeniorModel();
        }
        //获取集合KEY
        List<String> keyCistern = (isSeniorModel ? STRUCTURE.getSeniorKeyList() : STRUCTURE.getLowLevelKeyList());
        //遍历获取
        int index = counter.getAndIncrement() % keyCistern.size();
        final ModelObj modelObj = new ModelObj()
                //KEY
                .setKey(keyCistern.get(index));

        if (model != null) {
            //模型名称
            modelObj
                    .setTemperature(model.getTemperature())
                    .setMax_tokens(model.getMax_tokens())
                    .setTop_p(model.getTop_p())
                    .setModelName(model.getModelName());
        }

        return modelObj;
    }

    /**
     * 初始化. 把配置载入服务器内存中
     */
    @PostConstruct
    public void init() {
        final List<ChatGptStructure.Model> modelList = tsGptModelMapper.selectList(null)
                .parallelStream()
                .map(s -> new ChatGptStructure.Model()
                        .setTop_p(s.getTopP())
                        .setTemperature(s.getTemperature())
                        .setMax_tokens(s.getMaxTokens())
                        .setIsSeniorModel(s.getIsSeniorModel() == 1)
                        .setModelName(s.getModelName())).toList();
        //设置模型列表
        STRUCTURE
                .setModelList(modelList);
        STRUCTURE
                //低级模型KEY
                .setLowLevelKeyList(getGptKey(0))
                //高级模型KEY
                .setSeniorKeyList(getGptKey(1));

        //获取API地址
        final Object value = redisUtils.getValue(ChatGptConstant.GPT_REQUEST_API);
        if (value != null) {
            STRUCTURE.setRequestUrl(String.valueOf(value));
        } else {
            tsExceptionMapper.insert(new TsException()
                    .setLevel(LevelEnum.HEIGHT.getDesc())
                    .setServerName(ServerEnum.CHAT.getDesc())
                    .setCause("当前服务器未配置GPT请求链,使用GPT对话类功能时会报错, 请尽快前往控制台配置")
            );
        }
        //添加图片识别配置
        final Object bean = redisUtils.getValue(ChatGptConstant.IMAGE_RECOGNITION);
        if (bean != null) {
            STRUCTURE.setImageRecognitionConfig((ChatGptStructure.ImageRecognitionConfig) bean);
        } else {
            tsExceptionMapper.insert(new TsException()
                    .setLevel(LevelEnum.MIDDLE.getDesc())
                    .setServerName(ServerEnum.CHAT.getDesc())
                    .setCause("当前服务器未配置GPT识别图片配置,使用时会报错, 请尽快前往控制台配置")
            );
        }
    }

    private List<String> getGptKey(Integer isSeniorKey) {
        final List<String> list = tsGptKeyMapper.selectList(new QueryWrapper<TsGptKey>()
                .lambda()
                .eq(TsGptKey::getIsSeniorModel, isSeniorKey)
                .select(TsGptKey::getOpenAiKey)
        ).parallelStream().map(TsGptKey::getOpenAiKey).toList();
        if (list.isEmpty()) {
            tsExceptionMapper.insert(new TsException()
                    .setLevel(LevelEnum.HEIGHT.getDesc())
                    .setServerName(ServerEnum.CHAT.getDesc())
                    .setCause(isSeniorKey == 1 ? "当前服务器未配置任何GPT用于高级模型密钥,请尽快前往控制台配置" : "当前服务器未配置任何GPT用于低级模型密钥,请尽快前往控制台配置")
            );
        }
        return list;
    }


    /**
     * 模型结构
     */
    @Data
    @Accessors(chain = true)
    public static class ModelObj {

        private String key;

        private String modelName;

        private Double top_p;

        private Long max_tokens;

        private Double temperature;


    }

}
