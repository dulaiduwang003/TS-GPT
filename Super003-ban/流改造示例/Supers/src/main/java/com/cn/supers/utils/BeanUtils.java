package com.cn.supers.utils;

import com.alibaba.fastjson.JSON;

import java.util.List;


/**
 * The type Bean utils.
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

}
