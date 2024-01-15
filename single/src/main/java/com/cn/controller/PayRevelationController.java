package com.cn.controller;

import com.cn.service.ProductPayService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 支付状态回调 API
 *
 * @author 时间海 @github dulaiduwang003
 * @version 1.0
 */
@Slf4j
@RestController
@RequestMapping("/revelation")
@RequiredArgsConstructor
public class PayRevelationController {


    private final ProductPayService productPayService;


    /**
     * 支付宝官方回调接口
     *
     * @return the all product card
     */
    @PostMapping(value = "/alipay/callback", produces = MediaType.APPLICATION_JSON_VALUE)
    public String alipayCallback(HttpServletRequest request) {
        return productPayService.alipayPullback(request);
    }
}
