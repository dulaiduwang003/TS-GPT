package com.cn.vo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Accessors(chain = true)
public class UserPageVo implements Serializable {

    private Long userId;

    private String nickName;

    private String email;

    private String type;

    private Boolean isMember;

    private LocalDateTime expirationTime;

    private Integer daysRemaining;

    private LocalDateTime createdTime;

    private LocalDateTime updateTime;



}
