package com.cn.common;

import com.cn.common.structure.DallStructure;
import com.cn.constant.ChatGptConstant;
import com.cn.entity.TsDallKey;
import com.cn.entity.TsException;
import com.cn.enums.LevelEnum;
import com.cn.enums.ServerEnum;
import com.cn.mapper.TsDallKeyMapper;
import com.cn.mapper.TsExceptionMapper;
import com.cn.utils.RedisUtils;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;


/**
 * Dall组件类
 *
 * @author 时间海 @github dulaiduwang003
 * @version 1.0
 */
@Component
@RequiredArgsConstructor
public class DallCommon {

    private final TsDallKeyMapper tsDallKeyMapper;

    private final RedisUtils redisUtils;

    private final TsExceptionMapper tsExceptionMapper;
    public static final DallStructure STRUCTURE = new DallStructure();
    private static final AtomicInteger counter = new AtomicInteger(0);

    /**
     * 轮询获取KEY配置
     *
     * @return the string
     */
    public static String pollGetKey() {
        //获取集合KEY
        List<String> keyCistern = STRUCTURE.getKeyList();
        //遍历获取
        int index = counter.getAndIncrement() % keyCistern.size();
        return keyCistern.get(index);
    }

    /**
     * 初始化.
     */
    @PostConstruct
    public void init() {
        final List<String> keyList = tsDallKeyMapper.selectList(null)
                .parallelStream()
                .map(TsDallKey::getOpenAiKey).toList();
        STRUCTURE
                .setKeyList(keyList);
        if (keyList.isEmpty()) {
            tsExceptionMapper.insert(new TsException()
                    .setServerName(ServerEnum.DRAWING.getDesc())
                    .setLevel(LevelEnum.HEIGHT.getDesc())
                    .setCause("当前未配置任何可用于DALL绘图的密钥 请及时前往控制台配置!")
            );
        }

        final Object value = redisUtils.getValue(ChatGptConstant.DALL_REQUEST_API);
        if (value != null) {
            STRUCTURE.setRequestUrl(String.valueOf(value));
        } else {
            tsExceptionMapper.insert(new TsException()
                    .setServerName(ServerEnum.DRAWING.getDesc())
                    .setLevel(LevelEnum.HEIGHT.getDesc())
                    .setCause("当前未配置DALL绘图的请求链接 请及时配置!")
            );
        }

    }


}
