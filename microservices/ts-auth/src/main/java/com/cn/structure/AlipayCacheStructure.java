package com.cn.structure;

import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * 支付宝订单信息缓存结构
 *
 * @author 时间海 @github dulaiduwang003
 * @version 1.0
 */
@Data
@Accessors(chain = true)
public class AlipayCacheStructure {

    private String url;

    private LocalDateTime createdTime;

    private String ordersId;

    private Double price;

    private String productName;

}
