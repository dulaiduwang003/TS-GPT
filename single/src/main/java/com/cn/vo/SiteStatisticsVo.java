package com.cn.vo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * The type Site statistics vo.
 */
@Data
@Accessors(chain = true)
public class SiteStatisticsVo implements Serializable {

    private Integer newUsers;

    private Long totalUsers;

    private Double newRevenue;

    private Double totalRevenue;

    private Integer newTrade;

    private Long totalTrade;

    private Integer newVisits;

    private Integer totalVisits;
}
