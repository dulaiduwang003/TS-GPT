package com.cn.vo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

/**
 * The type Micro app vo.
 */
@Data
@Accessors(chain = true)
public class MicroAppVo implements Serializable {

    /**
     * The Category name.
     */
    private String categoryName;

    /**
     * The El icon.
     */
    private String elIcon;

    /**
     * The List.
     */
    private List<Vo> list;


    /**
     * The type Vo.
     */
    @Data
    @Accessors(chain = true)
    public static class Vo {

        private Long microAppId;

        private String icon;

        private String chineseIssue;

        private String englishIssue;

        private String chineseAnswer;

        private String englishAnswer;

        private String title;

        private String introduce;

        private Long visits;

        private boolean isSelected = false;
    }


}
