package com.cn.controller.admin;

import com.cn.dto.DeleteOrdersDto;
import com.cn.msg.Result;
import com.cn.service.OrdersService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 订单管理 api
 *
 * @author 时间海 @github dulaiduwang003
 * @version 1.0
 */
@Slf4j
@RestController
@RequestMapping("/orders-management")
@RequiredArgsConstructor
public class OrdersManagementController {


    private final OrdersService ordersService;

    /**
     * 分页获取订单信息
     */
    @GetMapping(value = "/get/orders/page", produces = MediaType.APPLICATION_JSON_VALUE)
    public Result getOrdersPage(@RequestParam(defaultValue = "1") final int pageNum, @RequestParam String prompt) {
        return Result.data(ordersService.getOrdersPage(pageNum, prompt));
    }


    /**
     * 删除指定订单
     */
    @PostMapping(value = "/delete/orders", produces = MediaType.APPLICATION_JSON_VALUE)
    public Result deleteOrders(@RequestBody @Validated DeleteOrdersDto dto) {
        ordersService.deleteOrders(dto);
        return Result.ok();
    }


}
