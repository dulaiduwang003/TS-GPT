package com.cn.app.superbot.utils;

import java.util.List;
import java.util.Objects;


/**
 * The type String utils.
 *
 * @author bdth
 * @email 2074055628 @qq.om
 */
@SuppressWarnings("all")
public final class StringUtils extends org.apache.commons.lang3.StringUtils {


    /**
     * Is legal boolean.
     *
     * @param str the str
     * @return the boolean
     */
    public static boolean isLegal(String str) {
        // 空字符串返回true
        if (isEmpty(str)) {
            return true;
        }
        // “|”为非法字符
        return !str.contains("|");
    }

    /**
     * Not empty boolean.
     *
     * @param str the str
     * @return the boolean
     */
    public static boolean notEmpty(String str) {
        if (Objects.nonNull(str) && str.length() > 0) {
            return true;
        }

        return false;
    }


    /**
     * Join string.
     *
     * @param <T>  the type parameter
     * @param list the list
     * @return the string
     */
    public static <T> String join(List<T> list) {
        return join(list.toArray());
    }


    /**
     * Join string.
     *
     * @param <T>       the type parameter
     * @param list      the list
     * @param separator the separator
     * @return the string
     */
    public static <T> String join(List<T> list, String separator) {
        return join(list.toArray(), separator);
    }

}
