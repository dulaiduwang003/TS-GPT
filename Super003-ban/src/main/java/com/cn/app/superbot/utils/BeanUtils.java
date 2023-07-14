package com.cn.app.superbot.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;

import java.util.List;


/**
 * The type Bean utils.
 *
 * @author bdth
 * @email 2074055628 @qq.om
 */
public final class BeanUtils {


    /**
     * Copy class proper ties t.
     *
     * @param <S>    the type parameter
     * @param <T>    the type parameter
     * @param source the source
     * @param target the target
     * @return the t
     */
    public static <S, T> T copyClassProperTies(S source, Class<T> target) {
        return JSON.parseObject(JSON.toJSONString(source), target);
    }


    /**
     * Copy array proper ties list.
     *
     * @param <T>    the type parameter
     * @param source the source
     * @param target the target
     * @return the list
     */
    public static <T> List<T> copyArrayProperTies(Object source, Class<T> target) {
        return JSON.parseArray(JSON.toJSONString(source), target);
    }


    /**
     * Copy class proper ties write date t.
     *
     * @param <S>    the type parameter
     * @param <T>    the type parameter
     * @param source the source
     * @param target the target
     * @return the t
     * @author bdth
     * @email 2074055628 @qq.om
     */
    public static <S, T> T copyClassProperTiesWriteDate(S source, Class<T> target) {
        return JSON.parseObject(
                JSONObject.toJSONStringWithDateFormat(source, "yyyy-MM-dd HH:mm:ss", SerializerFeature.WriteDateUseDateFormat), target
        );

    }


    /**
     * Copy array proper ties write date list.
     *
     * @param <T>    the type parameter
     * @param source the source
     * @param target the target
     * @return the list
     */
    public static <T> List<T> copyArrayProperTiesWriteDate(Object source, Class<T> target) {
        return JSON.parseArray(
                JSONObject.toJSONStringWithDateFormat(source, "yyyy-MM-dd HH:mm:ss", SerializerFeature.WriteDateUseDateFormat), target
        );
    }
}
