package com.cn.vo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Accessors(chain = true)
public class OrdersPageVo implements Serializable {

    private String ordersId;

    private String email;

    private String productName;

    private Double price;

    private Long days;

    private String status;

    private LocalDateTime createdTime;

    private LocalDateTime updateTime;
}
