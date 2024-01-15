package com.cn.entity;


import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * dall密钥表
 */
@Data
@TableName(value = "ts_dall_key")
@Accessors(chain = true)
public class TsDallKey {

  /**
   * id
   */
  @TableId(type = IdType.AUTO)
  private Long dallKeyId;

  /**
   * 密钥
   */
  private String openAiKey;


  @TableField(fill = FieldFill.INSERT)
  private LocalDateTime createdTime;

  @TableField(fill = FieldFill.INSERT_UPDATE)
  private LocalDateTime updateTime;


}
