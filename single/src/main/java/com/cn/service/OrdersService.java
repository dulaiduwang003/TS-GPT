package com.cn.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.cn.dto.DeleteOrdersDto;
import com.cn.vo.OrdersPageVo;

/**
 * 订单业务层
 *
 * @author 时间海 @github dulaiduwang003
 * @version 1.0
 */
public interface OrdersService {


    /**
     * 分页获取订单信息
     *
     * @param pageNum the page num
     * @param prompt  the prompt
     * @return the orders page
     */
    IPage<OrdersPageVo> getOrdersPage(final int pageNum, final String prompt);


    /**
     * 删除指定订单
     *
     * @param dto the dto
     */
    void deleteOrders(DeleteOrdersDto dto);

}
