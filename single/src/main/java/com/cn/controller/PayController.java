package com.cn.controller;

import com.cn.dto.CreatedPayAlipayDto;
import com.cn.msg.Result;
import com.cn.service.ProductPayService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


/**
 * 商品支付下单 api
 *
 * @author 时间海 @github dulaiduwang003
 * @version 1.0
 */
@Slf4j
@RestController
@RequestMapping("/pay")
@RequiredArgsConstructor
public class PayController {


    private final ProductPayService productPayService;


    /**
     * 创建支付宝订单
     *
     * @param dto the dto
     * @return the all product card
     */
    @PostMapping(value = "/created/alipay", produces = MediaType.APPLICATION_JSON_VALUE)
    public Result createdAliPayOrders(@RequestBody @Validated CreatedPayAlipayDto dto) {
        return Result.data(productPayService.createdAliPayOrders(dto));
    }

    /**
     * 获取订单状态
     *
     * @return the all product card
     */
    @GetMapping(value = "/orders/status/{ordersId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Result getOrdersPayStatus(@PathVariable String ordersId) {
        return Result.data(productPayService.getOrdersPayStatus(ordersId));
    }


    /**
     * 获取所有商品信息
     *
     * @return the all product card
     */
    @GetMapping(value = "/get/all/product", produces = MediaType.APPLICATION_JSON_VALUE)
    public Result getAllProductCard() {
        return Result.data(productPayService.getAllProductCard());
    }
}
