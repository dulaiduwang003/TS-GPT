package com.cn.vo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * The type Micro page vo.
 */
@Data
@Accessors(chain = true)
public class MicroPageVo implements Serializable {


    /**
     * The Category name.
     */
    private String categoryName;

    /**
     * The Micro app id.
     */
    private Long microAppId;

    /**
     * The Micro category id.
     */
    private Long microCategoryId;

    /**
     * The Icon.
     */
    private String icon;

    /**
     * The Chinese issue.
     */
    private String chineseIssue;

    /**
     * The English issue.
     */
    private String englishIssue;

    /**
     * The Chinese answer.
     */
    private String chineseAnswer;

    /**
     * The English answer.
     */
    private String englishAnswer;

    /**
     * The Title.
     */
    private String title;

    /**
     * The Introduce.
     */
    private String introduce;

    /**
     * The Created time.
     */
    private LocalDateTime createdTime;

    /**
     * The Update time.
     */
    private LocalDateTime updateTime;


}
