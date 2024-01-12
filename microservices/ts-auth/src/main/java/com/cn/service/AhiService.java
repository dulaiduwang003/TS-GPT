package com.cn.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.cn.dto.DeleteExceptionDto;
import com.cn.vo.ExceptionPageVo;
import com.cn.vo.SiteStatisticsVo;

/**
 * 终端类 业务
 *
 * @author bdth @github dulaiduwang003
 * @version 1.0
 */
public interface AhiService {


    /**
     * 获取站点数据
     *
     * @return the site statistics
     */
    SiteStatisticsVo getSiteStatistics();


    /**
     * 删除指定异常信息
     *
     * @param dto the dto
     */
    void deleteException(final DeleteExceptionDto dto);


    /**
     * 分页获取异常信息
     *
     * @param pageNum the page num
     * @return the exception page
     */
    IPage<ExceptionPageVo> getExceptionPage(final int pageNum);
}
