package com.cn.app.superbot.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * The interface Block keywords.
 *
 * @author bdth
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface BlockKeywords {

    /**
     * Limit int
     *
     * @return the int
     */
    int limit() default 1;

    /**
     * Check boolean.
     *
     * @return the boolean
     */
    boolean check() default true;

}
